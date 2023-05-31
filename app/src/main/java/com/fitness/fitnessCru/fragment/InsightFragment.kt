package com.fitness.fitnessCru.fragment

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.ViewProfileActivity
import com.fitness.fitnessCru.adapter.InsightPageAdapter
import com.fitness.fitnessCru.body.ActivityBody
import com.fitness.fitnessCru.databinding.ActivityGoalsBinding
import com.fitness.fitnessCru.databinding.FragmentInsightBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.ActivityResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.MySummaryViewModel
import com.fitness.fitnessCru.vm_factory.MySummaryVMFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class InsightFragment : Fragment() {

    private lateinit var loading: CustomProgressLoading
    private lateinit var insightBinding: FragmentInsightBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var binding: ActivityGoalsBinding
    private lateinit var viewModel: MySummaryViewModel
    private var flag = 0
    private var wake_time: String = ""
    private var sleep_time: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        insightBinding = FragmentInsightBinding.inflate(inflater, container, false)
        loading = CustomProgressLoading(requireContext())


        insightBinding.apply {
            viewPager.adapter = InsightPageAdapter(this@InsightFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = arrayOf("My Orders", "My Summary", "My Reminders")[position]
            }.attach()


            if (requireArguments()!!.getString("tab_id") != null)
                tabLayout.getTabAt(requireArguments().getString("tab_id")!!.toInt())!!.select()

//            var userDetail = Session.getUserDetails()
//
//            if (userDetail.name == null) {
//                if (userDetail.email != null) {
//                    var n = userDetail.email[0].toString().uppercase()
//                    gobackbtn.text = n
//                } else {
//                    gobackbtn.visibility = View.GONE
//                    placeholder.visibility = View.VISIBLE
//                }
//            } else {
//                var n = userDetail.name[0].toString().uppercase()
//                gobackbtn.text = n
//            }
//
//            placeholder.setOnClickListener {
//                val intent = Intent(requireContext()!!, ViewProfileActivity::class.java)
//                intent.putExtra("key", "id")
//                startActivity(intent)
//                /*startActivity(
//                    Intent(
//                        requireContext()!!,
//                        ViewProfileActivity::class.java
//                    )
//                )*/
//            }

            goBackBtn.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext()!!,
                        ViewProfileActivity::class.java
                    )
                )
                requireActivity().finish()
            }
        }

        val repository by lazy { Repository() }
        val factory by lazy { MySummaryVMFactory(repository) }
        viewModel = ViewModelProvider(this, factory).get(MySummaryViewModel::class.java)
        openBottomSheet()

        return insightBinding.root
    }

    private fun openBottomSheet() {
        binding = ActivityGoalsBinding.inflate(layoutInflater)

        bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetDialog.setContentView(binding.root)

//        insightBinding.gobackbtn.setOnClickListener {
//
//            bottomSheetDialog.show()
//
//        }

        pickDate()

        binding.crossBTN.setOnClickListener { bottomSheetDialog.hide() }

        binding.updateBtn.setOnClickListener { if (isValid()) activity() }

    }

    @SuppressLint("ResourceAsColor")
    private fun pickDate() {
        binding.sleepEtPm.setOnClickListener {
            flag = 1
            val timePicker = TimePickerDialog(
                requireContext(),
                timePickerDialogListener,
                12,
                10,
                false
            )
            timePicker.show()
            timePicker.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.two)
            timePicker.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.two)
        }
        binding.wakeEtAm.setOnClickListener {
            flag = 2
            val timePicker = TimePickerDialog(
                requireContext(),
                timePickerDialogListener,
                0,
                0,
                false
            )
            timePicker.show()
            timePicker.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.two)
            timePicker.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.two)
        }
    }

    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val time = "$hourOfDay:$minute";
            val fmt = SimpleDateFormat("HH:mm");
            var date: Date? = null;
            try {
                date = fmt.parse(time);
            } catch (e: ParseException) {
                e.printStackTrace();
            }
            val formattedTime = SimpleDateFormat("hh:mm aa").format(date)
            binding.apply {
                formattedTime.also {
                    (if (flag == 1) sleepEtPm else wakeEtAm).text = it
                    if (flag == 1)
                        sleep_time = it
                    else wake_time = it
                }
            }
        }

    private fun isValid(): Boolean {
        val pair: Pair<Boolean, String>
        binding.apply {
            pair = if (weightEt.text.toString().trim().isEmpty())
                Pair(false, "Please Fill Weight")
            else if (bmiEt.text.toString().trim().isEmpty())
                Pair(false, "Please Fill BMI")
            else if (stepsEt.text.toString().trim().isEmpty())
                Pair(false, "Please Fill Steps")
            else if (waterEt.text.toString().trim().isEmpty())
                Pair(false, "Please Fill Intake Water")
            else if (sleepEtPm.text.toString().trim().isEmpty())
                Pair(false, "Please Select Sleep Time")
            else if (wakeEtAm.text.toString().trim().isEmpty())
                Pair(false, "Please Select Wake up Time")
            else Pair(true, "")
            if (!pair.first) Util.toast(requireContext(), pair.second)
        }
        return pair.first
    }

    private fun activity() {
        binding.apply {
            loading.showProgress()
            viewModel.activityGoals(
                ActivityBody(
                    sleep_time!!,
                    wake_time!!,
                    weightEt.text.toString().trim(),
                    stepsEt.text.toString().trim(),
                    waterEt.text.toString().trim()
                )
            )
        }
        viewModel.response1.observe(viewLifecycleOwner) {
            loading.hideProgress()
            if (it.isSuccessful && it.body() != null) {
                Util.toast(
                    requireContext(),
                    it.body()!!.message
                )
                bottomSheetDialog.hide()
            } else if (!it.isSuccessful) Util.toast(
                requireContext(),
                Util.error(it.errorBody(), ActivityResponse::class.java).message
            )
        }
    }

}