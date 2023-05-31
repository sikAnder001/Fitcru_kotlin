package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.WhiteBoardTwoActivity
import com.fitness.fitnessCru.activities.WhiteBoardWorkoutActivity
import com.fitness.fitnessCru.databinding.FragmentSNCSessionBinding
import com.fitness.fitnessCru.model.ExerciseByWorkoutModel
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.WorkoutProgramViewModel
import com.fitness.fitnessCru.vm_factory.WorkoutProgramVMFactory


class SNCSessionFragment : Fragment() {

    private lateinit var sncSessionBinding: FragmentSNCSessionBinding

    private lateinit var repository: Repository
    private lateinit var viewModel: WorkoutProgramViewModel
    private lateinit var factory: WorkoutProgramVMFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sncSessionBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_s_n_c_session, container, false)

        repository = Repository()

        factory = WorkoutProgramVMFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(WorkoutProgramViewModel::class.java)

        val args = arguments?.getInt("workout_id", 0)
        val args2 = arguments?.getInt("program_id", 0)

        val textShader: Shader = LinearGradient(
            510f,
            400f,
            sncSessionBinding.startWhiteboardWorkoutBtn.textSize,
            sncSessionBinding.startWhiteboardWorkoutBtn.textSize,
            intArrayOf(
                Color.parseColor("#FF6105"),
                Color.parseColor("#FF02BD")
            ),
            null,
            Shader.TileMode.CLAMP
        )
        sncSessionBinding.startWhiteboardWorkoutBtn.paint.shader = textShader

        sncSessionBinding.startWorkoutBtn.setOnClickListener {
            var bundle = Bundle()
            bundle.putInt("work_id", args!!)
            bundle.putInt("program_id", args2!!)
            val intent = Intent(context, WhiteBoardWorkoutActivity::class.java)
            intent.putExtras(bundle)
            requireContext()!!.startActivity(intent)
        }

        sncSessionBinding.startWhiteboardWorkoutBtn.setOnClickListener {
            var bundle = Bundle()
            bundle.putInt("work_id", args!!)
            bundle.putInt("program_id", args2!!)
            val intent = Intent(context, WhiteBoardTwoActivity::class.java)
            intent.putExtras(bundle)
            requireContext()!!.startActivity(intent)
        }



        getDetail()

        sncSessionBinding.backBtn.setOnClickListener { activity?.onBackPressed() }
        return sncSessionBinding.root
    }

    private fun getDetail() {
        viewModel.getWorkoutDetail(arguments?.getInt("workout_id", 0))

        viewModel.response1.observe(viewLifecycleOwner) {

            if (it.isSuccessful && it.code() == 200 && it.body() != null) {
                sncSessionBinding.apply {
                    try {
                        if (it.isSuccessful && it.body() != null) {
                            var data = it.body()!!.data

                            Glide.with(requireContext()).load(data.banner)
                                .placeholder(R.drawable.place_holder)
                                .into(sncSessionThumbnailImage)
                            sncSessionTrainingTitle.text = data.name
                            /* first.text = data.firstline*/
                            second.text = data.secondline
                            third.text = data.thirdline
                            sncSessionDescription.text = data.description

                        } else if (!it.isSuccessful && it.errorBody() != null) {
                            Util.error(it.errorBody(), ExerciseByWorkoutModel::class.java).message
                        } else Toast.makeText(
                            requireContext(),
                            "Something went wrong",
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        Log.v("Tag", e.message.toString())
                    }

                }
            }
        }
    }

/*    private fun sessionDataSet() {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        sncSessionBinding.sncSessionTrainingItemsRv.layoutManager = linearLayout
        sncSessionBinding.sncSessionTrainingItemsRv.setHasFixedSize(true)
        sncSessionModel = setDataInSessionList()
        sncSessionAdapter = SNCSessionAdapter(sncSessionModel, context)
        sncSessionBinding.sncSessionTrainingItemsRv.adapter = sncSessionAdapter
    }*/

    /*  private fun setDataInSessionList(): ArrayList<SNCSessionModel> {
          val items: ArrayList<SNCSessionModel> = ArrayList()
          items.apply {
              add(
                  SNCSessionModel(
                      R.drawable.part_time_icon,
                      "30 Mins, Beginner",
                  )
              )
              add(
                  SNCSessionModel(
                      R.drawable.body_icon,
                      "Total-Body Strength, Conditioning"
                  )
              )
              add(
                  SNCSessionModel(
                      R.drawable.ic_intermediate,
                      "None (Mat Optional)"
                  )
              )
              add(
                  SNCSessionModel(
                      R.drawable.music_icon,
                      "Dance, Electronic"
                  )
              )
          }

          return items
      }*/


}