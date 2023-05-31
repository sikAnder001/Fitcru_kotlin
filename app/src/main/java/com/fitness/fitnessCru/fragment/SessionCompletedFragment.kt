package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.DashboardActivity
import com.fitness.fitnessCru.activities.ViewProfileActivity
import com.fitness.fitnessCru.adapter.PersonalRecordAdapter2
import com.fitness.fitnessCru.adapter.RateSessionAdapter
import com.fitness.fitnessCru.body.WorkoutFeedbackBody
import com.fitness.fitnessCru.databinding.FragmentSessionCompletedBinding
import com.fitness.fitnessCru.interfaces.FeedbackInterface
import com.fitness.fitnessCru.interfaces.RateInterface
import com.fitness.fitnessCru.model.CoachSlabResponse
import com.fitness.fitnessCru.model.ExerciseByWorkoutModel
import com.fitness.fitnessCru.model.RateSessionModel
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.Data
import com.fitness.fitnessCru.response.WorkoutFeedbackResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.WorkoutExersiseViewModel
import com.fitness.fitnessCru.viewmodel.WorkoutFeedbackViewModel
import com.fitness.fitnessCru.vm_factory.WorkoutExersiseVMFactory
import com.fitness.fitnessCru.vm_factory.WorkoutFeedbackVMFactory
import kotlinx.android.synthetic.main.fragment_session_completed.*

class SessionCompletedFragment : Fragment() {

    private lateinit var sessionCompletedBinding: FragmentSessionCompletedBinding

    private lateinit var rateSessionAdapter: RateSessionAdapter

    private lateinit var rateSessionModel: ArrayList<RateSessionModel>

    private lateinit var personalRecordAdapter: PersonalRecordAdapter2

    private lateinit var repository: Repository

    private lateinit var factory: WorkoutFeedbackVMFactory

    private lateinit var viewModel: WorkoutFeedbackViewModel

    private lateinit var viewModel1: WorkoutExersiseViewModel

    private lateinit var factory1: WorkoutExersiseVMFactory

    private lateinit var loading: CustomProgressLoading

    private var workoutId = 0
    private var program_id = 0

    private var rate = ""

    lateinit var userDetail: Data

    lateinit var allData: ArrayList<ExerciseByWorkoutModel.Data.ExerciseData>
    var newData = ArrayList<ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise>()

    var doubleBackToExitPressedOnce = false

    var feedbackBody = ArrayList<WorkoutFeedbackBody.ExerciseDataFeedback>()
    var feedbackBody2 = ArrayList<WorkoutFeedbackBody.ExerciseDataFeedback>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sessionCompletedBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_session_completed, container, false)

        program_id = requireArguments().getInt("program_id")
        workoutId = requireArguments().getInt("work_id")

        backPress()

        userDetail = Session.getUserDetails()

        if (userDetail.name == null) {
            if (userDetail.email != null) {
                var n = userDetail.email[0].toString().uppercase()
                sessionCompletedBinding.backBtn.text = n
            } else {
                sessionCompletedBinding.backBtn.visibility = View.GONE
                sessionCompletedBinding.placeholder.visibility = View.VISIBLE

            }
        } else {
            var n = userDetail.name[0].toString().uppercase()
            sessionCompletedBinding.backBtn.text = n
        }

        Log.v("show", Session.getUserDetails().toString())

        sessionCompletedBinding.placeholder.setOnClickListener {
            startActivity(
                Intent(
                    requireContext()!!,
                    ViewProfileActivity::class.java
                )
            )
        }

        sessionCompletedBinding.backBtn.setOnClickListener {
            startActivity(
                Intent(
                    requireContext()!!,
                    ViewProfileActivity::class.java
                )
            )
        }

        loading = CustomProgressLoading(requireContext())

        repository = Repository()

        factory1 = WorkoutExersiseVMFactory(repository)

        viewModel1 = ViewModelProvider(this, factory1)[WorkoutExersiseViewModel::class.java]

        factory = WorkoutFeedbackVMFactory(repository)

        viewModel = ViewModelProvider(this, factory)[WorkoutFeedbackViewModel::class.java]

        sessionCompleted()

        setRateSessionRV()

        setPersonalRecord()

        observer()

        editSection(feedbackBody as ArrayList<WorkoutFeedbackBody.ExerciseDataFeedback>)

        return sessionCompletedBinding.root
    }

    private fun sessionCompleted() {
        viewModel.sessionCompleted(workoutId, "workout")
        viewModel.sessionCompleted.observe(requireActivity()) {
            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0) {
                Log.v("coachtyupe", it.body()!!.message)
            } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                Util.toast(
                    requireContext(), it.body()!!.message
                )
            } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                requireContext(),
                Util.error(it.errorBody(), CoachSlabResponse::class.java).message
            )
        }
    }

    private fun backPress() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!doubleBackToExitPressedOnce) {
                        startActivity(Intent(context, DashboardActivity::class.java))
                        activity!!.finish()
                    }
//                    doubleBackToExitPressedOnce = true
//                    Toast.makeText(context, "Please click BACK again to Go Home Page", Toast.LENGTH_SHORT)
//                        .show()

//                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
//                        doubleBackToExitPressedOnce = false
//                    }, 2000)
                }
            }
            )
    }

    private fun setRateSessionRV() {
        val gridLayout = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        sessionCompletedBinding.rateSessionRv.layoutManager = gridLayout
        sessionCompletedBinding.rateSessionRv.setHasFixedSize(true)
        rateSessionModel = getRateSessionData()
        rateSessionAdapter = RateSessionAdapter(rateSessionModel, context, object : RateInterface {
            override fun onRateClick(name: String) {
                rate = name
            }
        })
        sessionCompletedBinding.rateSessionRv.adapter = rateSessionAdapter
    }

    private fun getRateSessionData(): ArrayList<RateSessionModel> {
        val rate: ArrayList<RateSessionModel> = ArrayList()
        rate.apply {
            add(
                RateSessionModel(
                    "Easy"
                )
            )
            add(
                RateSessionModel(
                    "Medium"
                )
            )
            add(
                RateSessionModel(
                    "Difficult"
                )
            )
            add(
                RateSessionModel(
                    "Hard"
                )
            )
        }
        return rate
    }

    private fun setPersonalRecord() {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        sessionCompletedBinding.personalRecordRv.layoutManager = linearLayout
        sessionCompletedBinding.personalRecordRv.setHasFixedSize(true)
        personalRecordAdapter = PersonalRecordAdapter2(context, object : FeedbackInterface {
            override fun onFeedbackClick(
                pos: Int,
                feedId: String?,
                title: String,
                description: String,
                weight: String,
                time: String?,
                raps: String?
            ) {

                try {
                    feedbackBody.set(
                        pos, WorkoutFeedbackBody.ExerciseDataFeedback(
                            feedId!!.toInt(), weight, raps, time
                        )
                    )
                } catch (e: Exception) {
                    feedbackBody.add(
                        pos, WorkoutFeedbackBody.ExerciseDataFeedback(
                            feedId!!.toInt(), weight, raps, time
                        )
                    )
                }

                editSection(feedbackBody)

            }
        })

        sessionCompletedBinding.personalRecordRv.adapter = personalRecordAdapter
    }

    private fun observer() {
        viewModel1.getExercise(workoutId)

        viewModel1.response.observe(viewLifecycleOwner) {
            try {

                if (it.isSuccessful && it.code() == 200 && it.body() != null) {

                    var data = it.body()!!.data

                    shareData(data)

                    allData = ArrayList()
                    newData = ArrayList()

                    sessionCompletedBinding.apply {
                        Glide.with(requireContext()).load(data.banner)
                            .placeholder(R.drawable.place_holder)
                            .into(sessionCompletedThumbnailImage)
                        snc_program_text.text = data.session_name
                    }

                    for (i in data.exercises_data) {

                        var tmpex =
                            arrayListOf<ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise>()

                        var set: Int
                        try {
                            set = i.details?.sets?.toInt()!!
                        } catch (e: Exception) {
                            set = 1
                        }
                        for (toy in 0 until set) {
                            var arrex = i.details?.exercise
                            if (arrex != null) {
                                for (exerciseItem in arrex) {
                                    tmpex.add(exerciseItem)
                                    newData.add(exerciseItem)
                                }
                            } else {
                                continue
                                tmpex.add(
                                    ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise(
                                        null,
                                        null,
                                        null,
                                        null,
                                        "Rest",
                                        null,
                                        null,
                                        i.details.sets,
                                        null,
                                        null,
                                        null,
                                        null
                                    )
                                )
                            }
                        }

                        var tmpNewExDetails =
                            ExerciseByWorkoutModel.Data.ExerciseData.Details(
                                i.details?.sets,
                                i.details?.sets_type,
                                i.details?.description,
                                tmpex
                            )
                        allData.add(
                            ExerciseByWorkoutModel.Data.ExerciseData(
                                i.ex_type,
                                tmpNewExDetails
                            )
                        )
                    }

                    personalRecordAdapter.stylist(newData)

                    for (i in 0 until newData.size) {
                        feedbackBody.add(
                            WorkoutFeedbackBody.ExerciseDataFeedback(
                                null, null, null, null
                            )
                        )
                    }

                    it.body()!!.message

                } else if (!it.isSuccessful && it.errorBody() != null) {

                    Util.error(it.errorBody(), ExerciseByWorkoutModel::class.java).message

                } else Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.d("error", e.localizedMessage.toString())
            }
        }
    }

    private fun shareData(data: ExerciseByWorkoutModel.Data) {
        sessionCompletedBinding.shareBtn.setOnClickListener {
            val text = "Look at my awesome picture"
//            val pictureUri: Uri = Uri.parse("file://my_picture")
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_TEXT, text)
//            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            shareIntent.type = "text/plain"
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(shareIntent, "Share images..."))
        }
    }

    private fun editSection(feedbackBody: ArrayList<WorkoutFeedbackBody.ExerciseDataFeedback>) {

        sessionCompletedBinding.apply {

            Log.v("need", feedbackBody.toString())

            continueBtn.setOnClickListener {
                for (element in feedbackBody) {
                    if (element.exercise_id != null) {
                        feedbackBody2.add(element)
                    }
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    updateFeedback(feedbackBody2)
                }, 1000)
            }
        }
    }

    private fun updateFeedback(feedbackBody2: ArrayList<WorkoutFeedbackBody.ExerciseDataFeedback>) {

        var feedbackMessagge = sessionCompletedBinding.feedbackBox.text.trim().toString()

        loading.showProgress()
        viewModel.workoutFeedback(
            WorkoutFeedbackBody(
                workoutId,
                rate,
                feedbackMessagge,
                feedbackBody2
            )
        )
        viewModel.response.observe(viewLifecycleOwner) {
            loading.hideProgress()
            if (it.isSuccessful && it.code() == 200 && it.body() != null) {
                Toast.makeText(context, "Feedback saved successfully!", Toast.LENGTH_SHORT).show()
                /*if (userDetail.ispaid == 1) {
                    startActivity(Intent(context, DashboardActivity::class.java))
                } else */
                when (program_id) {
                    -1 -> {
                        startActivity(Intent(context, DashboardActivity::class.java))
                    }
                    -2 -> {
                        val intent = Intent(context, DashboardActivity::class.java)
                        intent.putExtra("studio", "done")
                        requireContext().startActivity(intent)
                        requireActivity().finishAffinity()
                    }
                    else -> {
                        val intent = Intent(context, DashboardActivity::class.java)
                        intent.putExtra("studio", "program")
                        requireContext().startActivity(intent)
                        requireActivity().finishAffinity()
                    }
                }
            } else if (!it.isSuccessful && it.errorBody() != null) {
                Util.error(it.errorBody(), WorkoutFeedbackResponse::class.java).message
            } else Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

}


