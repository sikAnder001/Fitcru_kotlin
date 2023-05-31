package com.fitness.fitnessCru.fragment

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.body.UpdateQuestionBody2
import com.fitness.fitnessCru.databinding.FragmentEHealthBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.GetQuestionsByUserIdResponse
import com.fitness.fitnessCru.response.SignUpWithEmailResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.LimitDecimal
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.GetQuestionsByUserIdViewModel
import com.fitness.fitnessCru.viewmodel.UserDetailsViewModel
import com.fitness.fitnessCru.vm_factory.GetQuestionsByUserIdVMFactory
import com.fitness.fitnessCru.vm_factory.UserDetailsVMFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class EHealthFragment : Fragment() {

    private var healthBinding: FragmentEHealthBinding? = null
    private val binding get() = healthBinding!!
    private lateinit var loading: CustomProgressLoading
    private lateinit var viewModel: UserDetailsViewModel
    private lateinit var viewModel1: GetQuestionsByUserIdViewModel
    private var foodPref = ArrayList<String>()
    private var foodPref1 = ArrayList<String>()
    private var fitnessLevel = ArrayList<String>()
    private var fitnessLevel1 = ArrayList<String>()
    private var dailyActivity = ArrayList<String>()
    private var dailyActivity1 = ArrayList<String>()
    private var workoutVibeData = ArrayList<String>()
    private var workoutVibeData1 = ArrayList<String>()
    private var yourGoal = ArrayList<String>()
    private var yourGoal1 = ArrayList<String>()
    private var food: Int = 0
    private var fitness: Int = 0
    private var activity: Int = 0
    private var goal: Int = 0
    private var workoutVibe: Int = 0
    private var flags = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        healthBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_e_health, container, false)
        loading = CustomProgressLoading(requireContext())

        val repository by lazy { Repository() }

        val factory by lazy { UserDetailsVMFactory(repository) }

        val factory1 by lazy { GetQuestionsByUserIdVMFactory(repository) }

        viewModel = ViewModelProvider(this, factory)[UserDetailsViewModel::class.java]

        viewModel1 = ViewModelProvider(this, factory1)[GetQuestionsByUserIdViewModel::class.java]

        hittingViews()

        getQuestionByUserID()

        observer()

        viewModel.apply {
            getFoodPref()
            getFitnessLevel()
            getDailyActivity()
            getYourGoal()
            getWorkoutVibe()
        }

        binding.apply {
            tvContinue.setOnClickListener {
                if (isValid()) {
                    loading.showProgress()
                    viewModel.setQuestionnaireSelect(
                        UpdateQuestionBody2(
                            heightEt.text.toString(),
                            weightEt.text.toString(),
                            targetWeightEt.text.toString(),
                            stepsEt.text.toString(),
                            waterEt.text.toString(),
                            goal,
                            food,
                            fitness,
                            activity,
                            workoutVibe,
                            sleepEtPm.text.toString(),
                            wakeEtAm.text.toString()
                        )
                    )
                }
            }
        }

        return healthBinding!!.root
    }

    private fun observer() {
        viewModel.response2.observe(viewLifecycleOwner) {
            loading.hideProgress()
            val res = it.body()
            try {
                if (res != null && it.isSuccessful && res.error_code ?: 1 == 0) {

                    viewModel1.getQuestionByUserID()

                    Util.toast(requireContext(), res.message)

                } else {
                    if (it.code() == 400 || it.code() == 401) {
                        val error =
                            Util.error(it.errorBody(), SignUpWithEmailResponse::class.java)
                        Toast.makeText(
                            requireContext(),
                            error.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error : ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isValid(): Boolean {
        var pair: Pair<Boolean, String>
        binding.apply {
            pair = if (weightEt.text.toString().trim().isEmpty())
                Pair(false, "Please Fill Weight")
            else if (targetWeightEt.text.toString().trim().isEmpty())
                Pair(false, "Please Fill Target Weight")
            else if (heightEt.text.toString().trim().isEmpty())
                Pair(false, "Please Fill Height")
            else if (stepsEt.text.toString().trim().isEmpty())
                Pair(false, "Please Fill Steps")
            else if (waterEt.text.toString().trim().isEmpty())
                Pair(false, "Please Fill Water")
            else if (food == 0)
                Pair(false, "Please Select Food Preferences")
            else if (fitness == 0)
                Pair(false, "Please Select Fitness Level")
            else if (activity == 0)
                Pair(false, "Please Select Daily Activity")
            else if (workoutVibe == 0)
                Pair(false, "Please Select Workout Vibe")
            else if (goal == 0)
                Pair(false, "Please Select Your Goal")
            else if (sleepEtPm.text.toString().trim().isEmpty())
                Pair(false, "Please Select Sleep Time")
            else if (wakeEtAm.text.toString().trim().isEmpty())
                Pair(false, "Please Select WakeUp Time")
            else Pair(true, "")
        }
        return if (!pair.first) {
            Util.toast(requireContext(), pair.second)
            false
        } else true
    }

    @SuppressLint("ResourceAsColor")
    private fun hittingViews() {
        binding.apply {

            weightEt.filters = arrayOf(LimitDecimal(5, 2))

            targetWeightEt.filters = arrayOf(LimitDecimal(5, 2))

            heightEt.filters = arrayOf(LimitDecimal(5, 2))

            sleepEtPm.setOnClickListener {
                flags = 1
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
            wakeEtAm.setOnClickListener {
                flags = 2
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
        }
    }

    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val time = "$hourOfDay:$minute"
            val fmt = SimpleDateFormat("hh:mm");
            var date: Date? = null;
            try {
                date = fmt.parse(time);
            } catch (e: ParseException) {
                e.printStackTrace();
            }
            var formattedTime = SimpleDateFormat("yyyy-MM-dd,hh:mm a", Locale.ENGLISH).format(date)
            formattedTime = formattedTime.split(",")[1]
            binding.apply {
                formattedTime.also {
                    (if (flags == 1) sleepEtPm else wakeEtAm).text = it
                }
            }
        }

    private fun foodPref(d: GetQuestionsByUserIdResponse.GetQuestionsByUserIdData) {
        viewModel.foodPref.observe(viewLifecycleOwner) {
            binding.spFoodPreference.apply {
                if (it.isSuccessful && it.code() == 200) {
                    val body = it.body()!!.data
                    foodPref.clear()
                    foodPref.add("Select Food")
                    for (e in body) {
                        foodPref.add(e.name)
                    }
                    val adapter: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireContext(),
                            android.R.layout.simple_list_item_activated_1,
                            foodPref as List<Any?>
                        )
                    setAdapter(adapter)
                    body?.forEachIndexed { index, element ->
                        if (element?.id == d?.diet_type_select_id) {
                            setSelection(index + 1)
                        }
                    }
                    if (food != 0) {
                        run breaking@{
                            body.forEachIndexed { index, element ->
                                if (element.id == food) {
                                    food = 0
                                    food = element.id
                                    setSelection(index + 1)
                                    return@breaking
                                }
                            }
                        }
                    }
                    onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {

                            (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)

                            if (position > 0) {
                                for (element in body) {
                                    if (parent.selectedItem.toString() == element.name) {
                                        food = element.id
                                    }
                                }
                            } else {
                                (parent?.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                } else {
                    Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun fitnessLevel(d: GetQuestionsByUserIdResponse.GetQuestionsByUserIdData) {
        viewModel.fitnessLevel.observe(viewLifecycleOwner) {
            binding.spFitnessLevel.apply {
                if (it.isSuccessful && it.code() == 200) {
                    val body = it.body()!!.data
                    fitnessLevel.clear()
                    fitnessLevel.add("Select Fitness Level")
                    for (e in body) {
                        fitnessLevel.add(e.name)
                    }
                    val adapter: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireContext(),
                            android.R.layout.simple_list_item_activated_1,
                            fitnessLevel as List<Any?>
                        )
                    setAdapter(adapter)
                    body.forEachIndexed { index, element ->
                        if (element?.id == d?.fitness_level_select_id) {
                            setSelection(index + 1)
                        }
                    }
                    if (fitness != 0) {
                        run breaking@{
                            body.forEachIndexed { index, element ->
                                if (element.id == fitness) {
                                    fitness = 0
                                    fitness = element.id
                                    setSelection(index + 1)
                                    return@breaking
                                }
                            }
                        }
                    }
                    onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {

                            (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)

                            if (position > 0) {
                                for (element in body) {
                                    if (parent.selectedItem.toString() == element.name) {
                                        fitness = element.id
                                    }
                                }
                            } else {
                                (parent?.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                } else {
                    Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun dailyActivity(d: GetQuestionsByUserIdResponse.GetQuestionsByUserIdData) {
        viewModel.dailyActivity.observe(viewLifecycleOwner) {
            binding.spActivity.apply {
                if (it.isSuccessful && it.code() == 200) {
                    val body = it.body()!!.data
                    dailyActivity.clear()
                    dailyActivity.add("Select Daily Activity")
                    for (e in body) {
                        dailyActivity.add(e.name)
                    }
                    val adapter: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireContext(),
                            android.R.layout.simple_list_item_activated_1,
                            dailyActivity as List<Any?>
                        )
                    setAdapter(adapter)
                    body.forEachIndexed { index, element ->
                        if (element?.id == d?.how_active_select_id) {
                            setSelection(index + 1)
                        }
                    }
                    if (activity != 0) {
                        run breaking@{
                            body.forEachIndexed { index, element ->
                                if (element.id == activity) {
                                    activity = 0
                                    activity = element.id
                                    setSelection(index + 1)
                                    return@breaking
                                }
                            }
                        }
                    }
                    onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {

                            (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)

                            if (position > 0) {
                                for (element in body) {
                                    if (parent.selectedItem.toString() == element.name) {
                                        activity = element.id
                                    }
                                }
                            } else {
                                (parent?.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                } else {
                    Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun workoutVibe(d: GetQuestionsByUserIdResponse.GetQuestionsByUserIdData) {
        viewModel.workoutVibe.observe(viewLifecycleOwner) {
            binding.spYourGoal.apply {
                if (it.isSuccessful && it.code() == 200) {
                    val body = it.body()!!.data
                    workoutVibeData.clear()
                    workoutVibeData.add("Select Goal")
                    for (e in body) {
                        workoutVibeData.add(e.title.toString())
                    }
                    val adapter: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireContext(),
                            android.R.layout.simple_list_item_activated_1,
                            workoutVibeData as List<Any?>
                        )
                    setAdapter(adapter)
                    body.forEachIndexed { index, element ->
                        if (element?.id == d?.what_bring_select_id) {
                            setSelection(index + 1)
                        }
                    }
                    if (goal != 0) {
                        run breaking@{
                            body.forEachIndexed { index, element ->
                                if (element.id == goal) {
                                    goal = 0
                                    goal = element.id
                                    setSelection(index + 1)
                                    return@breaking
                                }
                            }
                        }
                    }
                    onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {

                            (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)

                            if (position > 0) {
                                for (e in body) {
                                    if (parent.selectedItem.toString() == e.title.toString()) {
                                        goal = e.id
                                    }
                                }
                            } else {
                                (parent?.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                } else {
                    Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun yourGoal(d: GetQuestionsByUserIdResponse.GetQuestionsByUserIdData) {
        viewModel.yourGoal.observe(viewLifecycleOwner) {
            binding.spWorkoutVibe.apply {
                if (it.isSuccessful && it.code() == 200) {
                    val body = it.body()!!.data
                    yourGoal.clear()
                    yourGoal.add("Select Workout")
                    for (e in body) {
                        yourGoal.add(e.name ?: "")
                    }
                    val adapter: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireContext(),
                            android.R.layout.simple_list_item_activated_1,
                            yourGoal as List<Any?>
                        )
                    setAdapter(adapter)
                    body.forEachIndexed { index, element ->
                        if (element?.id == d?.workout_vibe_select_id) {
                            setSelection(index + 1)
                        }
                    }
                    if (workoutVibe != 0) {
                        run breaking@{
                            body.forEachIndexed { index, element ->
                                if (element.id == workoutVibe) {
                                    workoutVibe = 0
                                    workoutVibe = element.id
                                    setSelection(index + 1)
                                    return@breaking
                                }
                            }
                        }
                    }
                    onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {

                            (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                            if (position > 0) {
                                for (element in body) {
                                    if (parent.selectedItem.toString() == element.name.toString()) {
                                        workoutVibe = element.id
                                    }
                                }
                            } else {
                                (parent?.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                } else {
                    Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun foodPref1() {
        viewModel.foodPref.observe(viewLifecycleOwner) {
            binding.spFoodPreference.apply {
                if (it.isSuccessful && it.code() == 200) {
                    val body = it.body()!!.data
                    foodPref1.clear()
                    foodPref1.add("Select Food")
                    for (e in body) {
                        foodPref1.add(e.name)
                    }
                    val adapter: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireContext(),
                            android.R.layout.simple_list_item_activated_1,
                            foodPref1 as List<Any?>
                        )
                    setAdapter(adapter)
                    if (food != 0) {
                        run breaking@{
                            body.forEachIndexed { index, element ->
                                if (element.id == food) {
                                    food = 0
                                    food = element.id
                                    setSelection(index)
                                    return@breaking
                                }
                            }
                        }
                    }
                    onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {

                            (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                            if (position > 0) {
                                for (element in body) {
                                    if (parent.selectedItem.toString() == element.name) {
                                        food = element.id
                                    }
                                }
                            } else {
                                (parent?.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                } else {
                    Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun fitnessLevel1() {
        viewModel.fitnessLevel.observe(viewLifecycleOwner) {
            binding.spFitnessLevel.apply {
                if (it.isSuccessful && it.code() == 200) {
                    val body = it.body()!!.data
                    fitnessLevel1.clear()
                    fitnessLevel1.add("Select Fitness Level")
                    for (e in body) {
                        fitnessLevel1.add(e.name)
                    }
                    val adapter: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireContext(),
                            android.R.layout.simple_list_item_activated_1,
                            fitnessLevel1 as List<Any?>
                        )
                    setAdapter(adapter)
                    if (fitness != 0) {
                        run breaking@{
                            body.forEachIndexed { index, element ->
                                if (element.id == fitness) {
                                    fitness = 0
                                    fitness = element.id
                                    setSelection(index)
                                    return@breaking
                                }
                            }
                        }
                    }
                    onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {

                            (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                            if (position > 0) {
                                for (element in body) {
                                    if (parent.selectedItem.toString() == element.name) {
                                        fitness = element.id
                                    }
                                }
                            } else {
                                (parent?.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                } else {
                    Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun dailyActivity1() {
        viewModel.dailyActivity.observe(viewLifecycleOwner) {
            binding.spActivity.apply {
                if (it.isSuccessful && it.code() == 200) {
                    val body = it.body()!!.data
                    dailyActivity1.clear()
                    dailyActivity1.add("Select Daily Activity")
                    for (e in body) {
                        dailyActivity1.add(e.name)
                    }
                    val adapter: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireContext(),
                            android.R.layout.simple_list_item_activated_1,
                            dailyActivity1 as List<Any?>
                        )
                    setAdapter(adapter)
                    if (activity != 0) {
                        run breaking@{
                            body.forEachIndexed { index, element ->
                                if (element.id == activity) {
                                    activity = 0
                                    activity = element.id
                                    setSelection(index)
                                    return@breaking
                                }
                            }
                        }
                    }
                    onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {

                            (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                            if (position > 0) {
                                for (e in body) {
                                    if (parent.selectedItem.toString() == e.name) {
                                        activity = e.id
                                    }
                                }
                            } else {
                                (parent?.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                } else {
                    Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun workoutVibe1() {
        viewModel.workoutVibe.observe(viewLifecycleOwner) {
            binding.spYourGoal.apply {
                if (it.isSuccessful && it.code() == 200) {
                    val body = it.body()!!.data
                    workoutVibeData1.clear()
                    workoutVibeData1.add("Select Goal")
                    for (e in body) {
                        workoutVibeData1.add(e.title.toString())
                    }
                    val adapter: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireContext(),
                            android.R.layout.simple_list_item_activated_1,
                            workoutVibeData1 as List<Any?>
                        )
                    setAdapter(adapter)
                    if (goal != 0) {
                        run breaking@{
                            body.forEachIndexed { index, element ->
                                if (element.id == goal) {
                                    goal = 0
                                    goal = element.id
                                    setSelection(index)
                                    return@breaking
                                }
                            }
                        }
                    }
                    onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {

                            (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                            if (position > 0) {
                                for (e in body) {
                                    if (parent.selectedItem.toString() == e.title.toString()) {
                                        goal = e.id
                                    }
                                }
                            } else {
                                (parent?.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                } else {
                    Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun yourGoal1() {
        try {
            viewModel.yourGoal.observe(viewLifecycleOwner) {
                binding.spWorkoutVibe.apply {
                    if (it.isSuccessful && it.code() == 200) {
                        val body = it.body()!!.data
                        yourGoal1.clear()
                        yourGoal1.add("Select Workout")
                        for (e in body) {
                            yourGoal1.add(e.name ?: "")
                        }
                        val adapter: ArrayAdapter<*> =
                            ArrayAdapter<Any?>(
                                requireContext(),
                                android.R.layout.simple_list_item_activated_1,
                                yourGoal1 as List<Any?>
                            )
                        setAdapter(adapter)
                        if (workoutVibe != 0) {
                            run breaking@{
                                body.forEachIndexed { index, element ->
                                    if (element.id == workoutVibe) {
                                        workoutVibe = 0
                                        workoutVibe = element.id
                                        setSelection(index)
                                        return@breaking
                                    }
                                }
                            }
                        }
                        onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View,
                                position: Int,
                                id: Long
                            ) {

                                (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                                if (position > 0) {
                                    for (element in body) {
                                        if (parent.selectedItem.toString() == element.name.toString()) {
                                            workoutVibe = element.id
                                        }
                                    }
                                } else {
                                    (parent?.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                    } else {
                        Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "yourGoal1: $e")
        }
    }

    private fun getQuestionByUserID() {

        loading.showProgress()

        viewModel1.getQuestionByUserID()

        viewModel1.response.observe(viewLifecycleOwner) {

            loading.hideProgress()

            try {
                if (it.isSuccessful && it.code() == 200 && it.body()!! != null) {

                    binding?.apply {

                        val data = it.body()!!.data

                        (weightEt as TextView).text = data.weight ?: null

                        targetWeightEt.setText(data?.target_weight ?: "")

                        heightEt.setText(data.height)

                        foodPref(data)

                        yourGoal(data)

                        fitnessLevel(data)

                        dailyActivity(data)

                        workoutVibe(data)

                        stepsEt.setText(data.target_step)

                        waterEt.setText(data.target_water)

                        sleepEtPm.text = data.mySummary?.userSleep?.sleep_time

                        wakeEtAm.text = data.mySummary?.userSleep?.wakeup_time

                    }

                } else {
                    if (it.code() == 404) {
                        foodPref1()

                        fitnessLevel1()

                        dailyActivity1()

                        workoutVibe1()

                        yourGoal1()

                    }
                    /*Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG)
                        .show()*/
                }
            } catch (e: Exception) {
            }
        }
    }

}
