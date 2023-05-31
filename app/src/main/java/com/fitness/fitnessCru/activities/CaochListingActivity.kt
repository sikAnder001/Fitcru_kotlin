package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.LegenderCoachesAdapter
import com.fitness.fitnessCru.adapter.LegenderCoachesAdapter.SelectedCoach
import com.fitness.fitnessCru.databinding.ActivityCaochListingBinding
import com.fitness.fitnessCru.databinding.CoachesFilterBinding
import com.fitness.fitnessCru.model.CoachSlabResponse
import com.fitness.fitnessCru.model.DataChip
import com.fitness.fitnessCru.model.LegenderCoachesModel
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.CoachListingResponse
import com.fitness.fitnessCru.response.GetAllReminderResponse
import com.fitness.fitnessCru.response.GradeAndSpecialResponse
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.CoachCategoryViewModel
import com.fitness.fitnessCru.vm_factory.CoachCategoryVMFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.android.synthetic.main.activity_caoch_listing.*
import kotlinx.android.synthetic.main.alert_dialog.*

class CaochListingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaochListingBinding

    private lateinit var setReminderBinding: CoachesFilterBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var loading: CustomProgressLoading
    private lateinit var coachesadapter: LegenderCoachesAdapter
    private var flag = false
    private var planId = 0
    private var catId = 0
    private var num = ""

    private lateinit var repository: Repository
    private lateinit var viewModel: CoachCategoryViewModel
    private lateinit var factory: CoachCategoryVMFactory

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaochListingBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        loading = CustomProgressLoading(this)

        planId = intent.getIntExtra("planId", 0)
        catId = intent.getIntExtra("cat_id", 0)
        num = intent.getStringExtra("num")!!

        repository = Repository()
        factory = CoachCategoryVMFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(CoachCategoryViewModel::class.java)

        binding.constraint.setOnClickListener {
            dialog()
        }

        binding.apply {
            gobackbtn.setOnClickListener {
                onBackPressed()
            }
            skipTv.setOnClickListener {
                val gotoDashBoard = Intent(this@CaochListingActivity, DashboardActivity::class.java)
                startActivity(gotoDashBoard)
            }

        }

        setCoach()
        callBottomSheet()
    }

    private fun setCoachFilter(minValue: String, maxValue: String) {
        val grade = setReminderBinding?.groupGrade?.children
            ?.toList()
            ?.filter { (it as Chip).isChecked }
            ?.joinToString(", ") { (it as Chip).id.toString() }

        val gender = setReminderBinding?.group?.children
            ?.toList()
            ?.filter { (it as Chip).isChecked }
            ?.joinToString(", ") { (it as Chip).id.toString() }

        val specialization = setReminderBinding?.groupSpecialization?.children
            ?.toList()
            ?.filter { (it as Chip).isChecked }
            ?.joinToString(", ") { (it as Chip).id.toString() }

        Log.v(
            "filterdata",
            gender.toString() + grade + "-" + specialization + "-" + minValue + "++" + maxValue
        )

        //filter API call

        viewModel.coachFilter(
            catId,
            minValue,
            maxValue,
            if (gender == "2") null else gender,
            if (planId == 4) "4" else grade,
            specialization
        )
        viewModel.coachFilter.observe(this@CaochListingActivity) {
            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0) {
                val data = it.body() as CoachListingResponse
                coachesadapter.setList(data.data)
                coachesadapter.notifyDataSetChanged()
                Log.v("coachtyupe", it.body()!!.data.toString())
            } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                Util.toast(
                    applicationContext, it.body()!!.message
                )
            } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                applicationContext,
                Util.error(it.errorBody(), CoachSlabResponse::class.java).message
            )
        }

    }

    private fun dialog() {
        val alert: AlertDialog.Builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)

        alert.setTitle("Request a callback")
        alert.setMessage("Are you sure you want our team to call you back ?")

        alert.setPositiveButton(
            "Yes"
        ) { dialog, whichButton ->
            dialog2()
        }

        alert.setNegativeButton(
            "No"
        ) { dialog, whichButton ->

        }

        alert.show()
    }

    private fun dialog2() {
        val alert2: AlertDialog.Builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)

        val view: View = LayoutInflater.from(this).inflate(
            R.layout.alert_dialog,
            findViewById(R.id.layoutDialogContainer)
        )

        alert2.setView(view)

        alert2.setPositiveButton(
            "Submit"
        ) { dialog, whichButton -> }

        alert2.setNegativeButton(
            "Cancel"
        ) { dialog, whichButton -> }

        val dialog: AlertDialog = alert2.create()

        dialog
            .show()
        dialog.setCancelable(false)

        var userDetail = Session.getUserDetails()

        if (userDetail.name != null) {
            (dialog.nameTv as TextView).text = userDetail.name
        }
        if (userDetail.phone_number != null) {
            (dialog.numberTv as TextView).text = userDetail.phone_number
        }

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            var wantToCloseDialog = false

            val nameT = dialog.nameTv.text.toString().trim()
            val numT = dialog.numberTv.text.toString().trim()

            if (nameT.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter your name", Toast.LENGTH_SHORT)
                    .show()

            } else if (numT.isEmpty() || numT.length < 10 || numT.length > 10)
                Toast.makeText(
                    applicationContext,
                    "Please enter your mobile no.",
                    Toast.LENGTH_SHORT
                ).show()
            else {
                wantToCloseDialog = true
                //API call
                viewModel.callBack(nameT, numT)
                viewModel.callBack.observe(this@CaochListingActivity) {

                    if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0) {
                        val data = it.body() as CoachListingResponse
                        Toast.makeText(
                            applicationContext,
                            it.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this, DashboardActivity::class.java))
                    } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                        Util.toast(
                            applicationContext, it.body()!!.message
                        )
                    } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                        applicationContext,
                        Util.error(it.errorBody(), CoachSlabResponse::class.java).message
                    )
                }
            }
            if (wantToCloseDialog)
                dialog.dismiss()
            //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
        }

    }

    private fun setCoach() {
        coachesadapter = LegenderCoachesAdapter(this@CaochListingActivity,
            object : SelectedCoach {
                override fun onClick(id: Int, coachName: String, i: Int) {
                    Log.v("numcoachlist", num)
                    if (i == 1) {
                        // dialogue box work
                        val userDetail = Session.getUserDetails()

                        var sameCoach = 0
                        var diffCoach = 0

                        if (userDetail.planDetail != null)
                            for (element in userDetail.planDetail!!) {
                                if (element.coach_id == id) {
                                    sameCoach = 1
                                    break
                                } else {
                                    diffCoach = 1
                                }
                            }

                        if (sameCoach == 1) {
                            val builder = AlertDialog.Builder(
                                this@CaochListingActivity,
                                R.style.AlertDialogTheme
                            )
                            builder.setTitle("You've already subscribed this coach")
                            builder.setMessage("Please click on 'Continue', if you want to extent further.")
                            builder.setCancelable(false)

                            builder.setPositiveButton("Continue") { dialog, which ->
                                val intent =
                                    Intent(applicationContext, SetupAllActivity::class.java)
                                intent.putExtra(Constants.DESTINATION, Constants.COACH_PLAN_DETAIL)
                                intent.putExtra("planId", planId)
                                intent.putExtra("catId", catId)
                                intent.putExtra("coachId", id)
                                intent.putExtra("coachName", coachName)
                                intent.putExtra("num", num)
                                startActivity(intent)
                            }
                            builder.setNegativeButton("Cancel") { dialog, which ->

                            }

                            val alertDialog = builder.create()
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                alertDialog.create()
                                alertDialog.show()
                            }
                        }/*else if(diffCoach==1) {
                            val builder = AlertDialog.Builder(this@CaochListingActivity,R.style.AlertDialogTheme)
                            builder.setTitle("You already subscribed a coach")
                            builder.setMessage("Please click on 'Continue', if you want to buy another coach.")
                            builder.setCancelable(false)

                            builder.setPositiveButton("Continue") { dialog, which ->
                                val intent = Intent(applicationContext, SetupAllActivity::class.java)
                                intent.putExtra(Constants.DESTINATION, Constants.COACH_PLAN_DETAIL)
                                intent.putExtra("planId", planId)
                                intent.putExtra("catId", catId)
                                intent.putExtra("coachId", id)
                                intent.putExtra("coachName", coachName)
                                startActivity(intent)
                            }
                            builder.setNegativeButton("Cancel") { dialog, which ->

                            }

                            val alertDialog = builder.create()
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                alertDialog.create()
                                alertDialog.show()
                            }
                        }*/ else {
                            val intent = Intent(applicationContext, SetupAllActivity::class.java)
                            intent.putExtra(Constants.DESTINATION, Constants.COACH_PLAN_DETAIL)
                            intent.putExtra("planId", planId)
                            intent.putExtra("catId", catId)
                            intent.putExtra("coachId", id)
                            intent.putExtra("coachName", coachName)
                            intent.putExtra("num", num)
                            startActivity(intent)
                        }
                    } else {
                        val intent = Intent(applicationContext, SetupAllActivity::class.java)
                        intent.putExtra(Constants.DESTINATION, Constants.COACH_PROFILE)
                        intent.putExtra("planId", planId)
                        intent.putExtra("catId", catId)
                        intent.putExtra("coachId", id)
                        intent.putExtra("coachName", coachName)
                        intent.putExtra("num", num)
                        startActivity(intent)
                    }
                }
            })

        rvcoachList.layoutManager =
            GridLayoutManager(this, 2)
        rvcoachList.adapter = coachesadapter
        loading.showProgress()
//        coachesadapter.setList(getCoach())

        viewModel.coachListing(catId, planId)
        viewModel.coachListing.observe(this@CaochListingActivity) {
            loading.hideProgress()
            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0) {
                val data = it.body() as CoachListingResponse
//                selectCoachAdapter.setList(data.data)
                coachesadapter.setList(data.data)
                Log.v("coachtyupe", it.body()!!.data.toString())
            } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                Util.toast(
                    applicationContext, it.body()!!.message
                )
            } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                applicationContext,

                Util.error(it.errorBody(), CoachSlabResponse::class.java).message
            )
        }
    }

    private fun getCoach(): ArrayList<LegenderCoachesModel> {
        val array = ArrayList<LegenderCoachesModel>()
        array.add(
            LegenderCoachesModel(
                R.drawable.place_holder,
                "Jarrad Markham",
                "Strength conditioning",
                "Celebrity"
            )
        )
        array.add(
            LegenderCoachesModel(
                R.drawable.place_holder,
                "Jarrad Markham",
                "Strength conditioning",
                "Celebrity"
            )
        )
        array.add(
            LegenderCoachesModel(
                R.drawable.place_holder,
                "Jarrad Markham",
                "Strength conditioning",
                "Celebrity"
            )
        )
        array.add(
            LegenderCoachesModel(
                R.drawable.place_holder,
                "Jarrad Markham",
                "Strength conditioning",
                "Celebrity"
            )
        )
        array.add(
            LegenderCoachesModel(
                R.drawable.place_holder,
                "Jarrad Markham",
                "Strength conditioning",
                "Celebrity"
            )
        )
        array.add(
            LegenderCoachesModel(
                R.drawable.place_holder,
                "Jarrad Markham",
                "Strength conditioning",
                "Celebrity"
            )
        )
        return array
    }

    private fun setBottomList() {
        viewModel.getGradeAndSpecial()
        viewModel.getGradeAndSpecial.observe(this@CaochListingActivity) {

            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0) {
                val data = it.body() as GradeAndSpecialResponse

                data.data.grade.forEach { its ->
                    setReminderBinding.groupGrade.addView(
                        createTagChip(
                            its.coach_type,
                            its.id
                        )
                    )
                }
                data.data.specialization.forEach { its ->
                    setReminderBinding.groupSpecialization.addView(
                        createTagChip(
                            its.name, its.id
                        )
                    )
                }
            } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                Util.toast(
                    applicationContext, it.body()!!.message
                )
            } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                applicationContext,
                Util.error(it.errorBody(), CoachSlabResponse::class.java).message
            )
        }

        setGender().forEach { its ->
            setReminderBinding.group.addView(
                createTagChip(
                    its.name,
                    its.id
                )
            )
        }
    }

    private fun createTagChip(chipName: String, id2: Int?): Chip {
        return Chip(this@CaochListingActivity).apply {
            text = "$chipName"

            if (id2 != null) {
                id = id2
            }

            val drawable = ChipDrawable.createFromAttributes(
                context,
                null,
                0,
                R.style.CustomChipChoice
            )
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
            setChipDrawable(drawable)
            setEnsureMinTouchTargetSize(false)
            chipStartPadding = 40F
            chipEndPadding = 40F
            chipMinHeight = 70f


            setTextColor(
                AppCompatResources.getColorStateList(
                    context,
                    R.color.text_color_chip_state_list
                )
            )
            //  chip.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_chip_state_list));
            //  chip.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_chip_state_list));
            chipBackgroundColor = AppCompatResources.getColorStateList(
                context,
                R.color.background_color_chip_state_list
            )
        }
    }

    private fun setGender(): ArrayList<DataChip> {
        val chipData = ArrayList<DataChip>()
        chipData.add(DataChip(0, R.drawable.place_holder, "Male"))
        chipData.add(DataChip(1, R.drawable.place_holder, "Female"))
        chipData.add(DataChip(2, R.drawable.place_holder, "Prefer not to say"))
        return chipData
    }

    private fun setGrade(): ArrayList<DataChip> {
        val chipData = ArrayList<DataChip>()
        chipData.add(DataChip(1, R.drawable.place_holder, "normal"))
        chipData.add(DataChip(2, R.drawable.place_holder, "pro"))
        chipData.add(DataChip(3, R.drawable.place_holder, "super"))
        chipData.add(DataChip(3, R.drawable.place_holder, "legendary"))
        return chipData
    }

    private fun showBottomSheet(data: GetAllReminderResponse.Data) {
        binding.apply {
            /*addTitleEt.setText(data.reminder_title)
            remindMeEt.setText(data.reminder_description)
            addTimeTv.text = data.reminder_time
            remindMeBeforeTv.text = data.reminder_before_time
            waterTypeSpinner.setSelection(
                if (data.repeat == "0") 0 else 1
            )*/
            bottomSheetDialog.show()
        }
        bottomSheetDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun callBottomSheet() {
        setReminderBinding = CoachesFilterBinding.inflate(layoutInflater)

        bottomSheetDialog = BottomSheetDialog(this@CaochListingActivity)

        bottomSheetDialog.setContentView(setReminderBinding.root)

        setBottomList()


        binding.filterBtn.setOnClickListener {
            flag = true
            showBottomSheet(
                GetAllReminderResponse.Data("", 0, "", "", "", "", "", "", 0)
            )
        }

        if (planId == 4) {
            setReminderBinding.pricingLl.visibility = View.GONE
            setReminderBinding.rangeSeekBar.visibility = View.GONE
            setReminderBinding.gradeLl.visibility = View.GONE
            setReminderBinding.groupGrade.visibility = View.GONE
        } else {
            setReminderBinding.pricingLl.visibility = View.VISIBLE
            setReminderBinding.rangeSeekBar.visibility = View.VISIBLE
            setReminderBinding.gradeLl.visibility = View.VISIBLE
            setReminderBinding.groupGrade.visibility = View.VISIBLE
        }

        var minValue = ""
        var maxValue = ""

        setReminderBinding.rangeSeekBar.addOnChangeListener { slider, value, fromUser ->
            setReminderBinding.tvRange.text =
                "₹${slider.values[0].toInt()} - ${slider.values[1].toInt()}"
            minValue = slider.values[0].toInt().toString()
            maxValue = slider.values[1].toInt().toString()
        }

        setReminderBinding.crossBTN.setOnClickListener { bottomSheetDialog.hide() }

        setReminderBinding.apply.setOnClickListener {
            setCoachFilter(minValue, maxValue)
            bottomSheetDialog.hide()
        }
        setReminderBinding.reset.setOnClickListener {
            setReminderBinding.rangeSeekBar.setValues(0f, 999999f)

            for (i in 0 until setReminderBinding.group.getChildCount()) {
                val chip: Chip = setReminderBinding.group.getChildAt(i) as Chip
                chip.isChecked = false
            }
            for (i in 0 until setReminderBinding.groupGrade.getChildCount()) {
                val chip: Chip = setReminderBinding.groupGrade.getChildAt(i) as Chip
                chip.isChecked = false
            }
            for (i in 0 until setReminderBinding.groupSpecialization.getChildCount()) {
                val chip: Chip = setReminderBinding.groupSpecialization.getChildAt(i) as Chip
                chip.isChecked = false
            }
            viewModel.coachListing(catId, planId)
            coachesadapter.notifyDataSetChanged()
        }
    }
}