package com.fitness.fitnessCru.fragment

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.AddFoodAdapter
import com.fitness.fitnessCru.adapter.AddFoodListAdapter
import com.fitness.fitnessCru.adapter.MealTypeAdapter
import com.fitness.fitnessCru.adapter.SelectedfoodInterface
import com.fitness.fitnessCru.body.MealBody
import com.fitness.fitnessCru.databinding.FragmentAddMealBinding
import com.fitness.fitnessCru.databinding.LayoutFoodSearchBinding
import com.fitness.fitnessCru.databinding.LayoutMealTypeBinding
import com.fitness.fitnessCru.interfaces.FragInterface
import com.fitness.fitnessCru.interfaces.QuestionaryInterface
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.FoodListResposne
import com.fitness.fitnessCru.response.MealTypeResponse
import com.fitness.fitnessCru.utility.Constants.Companion.TAG
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.MealViewModel
import com.fitness.fitnessCru.vm_factory.MealVMFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*


class AddMealFragment : Fragment() {

    private lateinit var addMealBinding: FragmentAddMealBinding
    private lateinit var mealTypeAdapter: MealTypeAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var loading: CustomProgressLoading
    private lateinit var addFoodAdapter: AddFoodAdapter
    private lateinit var foodListAdapter: AddFoodListAdapter
    private lateinit var date: String
    var mealId: Int = 0
    private lateinit var repository: Repository


    private lateinit var mealVMFactory: MealVMFactory
    private lateinit var mealViewModel: MealViewModel
    private var data = listOf<MealTypeResponse.Data>()
    private var time = ""
    private var mealTypeId = 0
    private var mealTypeId2 = 0
    private var mDate = ""
    private var mTime = ""
    private var mealType = ""
    private var mealTypeLengh = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addMealBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_meal, container, false)

        mealTypeAdapter = MealTypeAdapter(context)

        addMealBinding.mealTypeContainer.setOnClickListener {
            openBottomSheet()
        }
        /* addMealBinding.itemsContainer.setOnClickListener {

         }*/



        mDate = arguments?.getString("date") ?: ""
        mTime = arguments?.getString("time") ?: ""

        repository = Repository()
        mealVMFactory = MealVMFactory(repository)
        loading = CustomProgressLoading(requireContext())
        mealViewModel = ViewModelProvider(this, mealVMFactory).get(MealViewModel::class.java)
        //getFoodList()

        date = activity?.intent?.getStringExtra("date") ?: ""
        Log.e(TAG, "onCreateView: $data")

        addFood()
        getMealType(date)
        timeChangeListener()
        deleteFood()

        searchItem()
        responseObserver()
        mealId = arguments?.getInt("meal_id") ?: 0
        time = arguments?.getString("time") ?: ""
        mealType = arguments?.getString("mealType") ?: ""

        mealTypeId = mealId
        if (mealId > 0) getMealById()
        addMealBinding.updateBtn.text = if (mealId == 0) "Add Meal" else "Update"
        addMealBinding.addMealTv.text = if (mealId == 0) "Add Meal" else "Edit Meal"
        addMealBinding.updateBtn.setOnClickListener {
            if (valid())
                updateMeal()
        }
        addMealBinding.goBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }



        try {
            var rDate = SimpleDateFormat("yyyy-MM-dd")
            var rDate2 = rDate.parse(date)
            var fDate = SimpleDateFormat("dd MMM").format(rDate2)
            addMealBinding.date.text = "Date : $fDate"
        } catch (e: Exception) {

        }

        if (mealType.isNotEmpty()) {
            addMealBinding.mealType.text = mealType
        }
        return addMealBinding.root
    }


    private fun searchItem() {
        addFoodAdapter.sitOnClick(object : AddFoodAdapter.FoodSearchInterface {
            override fun onClick(pos: Int) {
                openBottomSheetFood(pos)

            }
        })
    }

    /* private fun getSelectedData(){
         foodListAdapter.sitOnClick(object : AddFoodListAdapter.SelectedfoodInterface{
             override fun onClick(position: Int, data: FoodListResposne.Data) {
                 addFoodAdapter.setList(data,position)
             }
         })
     }*/

    private fun deleteFood() {
        addFoodAdapter.setOnClick(object : QuestionaryInterface {
            override fun onClick(_id: Int) {
                if (_id != null) {
                    try {
                        mealViewModel.deleteFood(_id)
                        mealViewModel.deleteFood.observe(requireActivity()) {
                            if (it.isSuccessful && it.body()!!.error_code == 0) {
                                Util.toast(requireContext(), it.body()!!.message)
                            } else Util.toast(requireContext(), "Something went wrong...")
                        }
                    } catch (e: Exception) {
                        Log.e(ContentValues.TAG, "recycleMeals: $e")
                    }
                }
            }
        })
    }

    private fun getFoodList(text: String) {
        loading.showProgress()
        mealViewModel.foodListResponse(text)
        mealViewModel.foodListResponse.observe(viewLifecycleOwner) {
            loading.hideProgress()
            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0 && it.body()!!.data != null) {
                //addFoodAdapter.foodList = it.body()!!.data
                // longToast("foodListName")
                foodListAdapter.setFoodList(it.body()!!.data)
                foodListAdapter.notifyDataSetChanged()


                //  addFoodAdapter.notifyDataSetChanged()
            } else {
                if (it.code() == 404) {
                    val error = Util.error(it.errorBody(), FoodListResposne::class.java)
                    Toast.makeText(
                        context,
                        "We couldn't match any of your foods",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun responseObserver() {
        mealViewModel.addMealResponse.observe(viewLifecycleOwner) {
            loading.hideProgress()
            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0 && it.body()!!.data != null) {

                Toast.makeText(context, "Meal added Successfully", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "responseObserver: ${it.body()!!.message}")
                activity?.onBackPressed()
            } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                Util.toast(
                    requireContext(), it.body()!!.message
                )
                Util.error(
                    it.errorBody(), MealTypeResponse::class.java
                )
            } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                requireContext(),
                Util.error(it.errorBody(), MealTypeResponse::class.java).message
            )
        }

        mealViewModel.updateMealResponse.observe(viewLifecycleOwner) {
            loading.hideProgress()
            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0 && it.body()!!.data != null) {
                Toast.makeText(context, "Meal Updated Successfully", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) Util.toast(
                requireContext(),
                it.body()!!.message
            ) else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                requireContext(),
                Util.error(it.errorBody(), MealTypeResponse::class.java).message
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun timeChangeListener() {
        try {
            val sdf = SimpleDateFormat("HH:mm")
            var date: Date? = null
            date = sdf.parse(arguments?.getString("time"))
            val c: Calendar = Calendar.getInstance()
            c.time = date
            addMealBinding.timePicker.hour = c.get(Calendar.HOUR_OF_DAY)
            addMealBinding.timePicker.minute = c.get(Calendar.MINUTE)
        } catch (e: Exception) {
        }
        addMealBinding.timePicker.setOnTimeChangedListener { _, hour, minute ->

            time = "$hour:$minute"

//            var hour = hour
//            var am_pm = ""
//            when {
//                hour == 0 -> {
//                    hour += 12
//                    am_pm = "AM"
//                }
//                hour == 12 -> am_pm = "PM"
//                hour > 12 -> {
//                    hour -= 12
//                    am_pm = "PM"
//                }
//                else -> am_pm = "AM"
//            }
//            val hours = if (hour < 10) "0$hour" else hour
//            val min = if (minute < 10) "0$minute" else minute
//

        }
    }

    private fun valid(): Boolean {
        if (time == "") {
            Util.toast(requireContext(), "Please Select Time")
            return false
        } else if (mealTypeId == 0) {
            Util.toast(requireContext(), "Please Select Meal Type")
            return false
        } else {
            var flag = 0
            for (i in addFoodAdapter.addFoodModel) {
                //   i.time = time
                if (i.food_id == 0) {
                    Util.toast(requireContext(), "Please select food name")
                    break
                } else if (i.quantity <= 0) {
                    Util.toast(requireContext(), "Please enter food weight")
                    break
                }
                flag++
            }
            return flag == addFoodAdapter.addFoodModel.size
        }
        return true
    }

    private fun updateMeal() {
        loading.showProgress()

        //  for (i in addFoodAdapter.addFoodModel) i.time = time

        var mealType = MealBody(mealTypeId, date, time, addFoodAdapter.addFoodModel)
        if (mealId != 0) mealViewModel.updateMealResponse(mealType) else mealViewModel.addMealResponse(
            mealType
        )
    }

    private fun getMealType(selectedDate: String) {
        mealViewModel.responseMealType(selectedDate)
        mealViewModel.responseMealType.observe(viewLifecycleOwner) {
            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0 && it.body()!!.data != null) {
                val res = it.body()
                mealTypeAdapter.setList(res!!.data as ArrayList<MealTypeResponse.Data>)
                mealTypeLengh = if (res.data.isEmpty()) "0" else "1"
                if (arguments != null)
                    for (i in res?.data!!) {
                        if (i.id == arguments?.getInt("meal_id", 0)) {
                            addMealBinding.mealType.text = i.mealtype
                            break
                        }
                    }
                data = it.body()!!.data
            } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) Util.toast(
                requireContext(),
                it.body()!!.message
            ) else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                requireContext(),
                Util.error(it.errorBody(), MealTypeResponse::class.java).message
            )
        }
    }

    private fun getMealById() {
        try {
            mealViewModel.mealByIdResponse(mealId, mDate, mTime)
            mealViewModel.mealByIdResponse.observe(requireActivity()) {
                if (it.isSuccessful && it.body()!!.error_code == 0) {
                    addFoodAdapter.addFoodModel = it.body()!!.data
                    addFoodAdapter.notifyDataSetChanged()
                } else Util.toast(requireContext(), "Something went wrong...")
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "recycleMeals: $e")
        }
        //addFoodAdapter
    }

    private fun openBottomSheet() {
        val mealTypeBinding = LayoutMealTypeBinding.inflate(layoutInflater)

        bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetDialog.setContentView(mealTypeBinding.root)

        if (mealId > 0) else if (mealTypeLengh == "1") bottomSheetDialog.show()
        else Toast.makeText(
            context,
            "You have already added all the Meal Types, Please edit your meals",
            Toast.LENGTH_SHORT
        ).show()


        mealTypeBinding.crossBTN.setOnClickListener { bottomSheetDialog.hide() }

        mealTypeBinding.mealTypeRv.adapter = mealTypeAdapter

        mealTypeBinding.doneBtn.setOnClickListener {
            if (mealTypeId2 != 0) bottomSheetDialog.hide() else Util.toast(
                requireContext(),
                "Select meal type first"
            )
        }


        mealTypeAdapter.setOnClick(object : FragInterface {

            override fun onClick(id: Int, title: String) {

                mealTypeId = id

                mealTypeId2 = id

                addMealBinding.mealType.text = title
            }

        })
    }


    private fun openBottomSheetFood(pos: Int) {
        val foodSearchBinding = LayoutFoodSearchBinding.inflate(layoutInflater)

        bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetDialog.setContentView(foodSearchBinding.root)

        /* addMealBinding.mealTypeContainer.setOnClickListener {
             if (mealId > 0) else if (mealTypeLengh == "1") bottomSheetDialog.show()
             else Toast.makeText(
                 context,
                 "You have already added all the Meal Types, please edit your meals",
                 Toast.LENGTH_SHORT
             ).show()
         }*/
        bottomSheetDialog.show()

        getFoodList("")
        // bottomSheetDialog.behavior.peekHeight = 2000
//        bottomSheetDialog.behavior.isFitToContents = false
//        bottomSheetDialog.behavior.expandedOffset = 100

        foodSearchBinding.crossBTN.setOnClickListener { bottomSheetDialog.hide() }

        foodListAdapter = AddFoodListAdapter(pos, object : SelectedfoodInterface {
            override fun onClick(position: Int, data: FoodListResposne.Data) {
                var addData = ArrayList<FoodListResposne.Data>()
                addData.clear()
                addData.add(data)
                addFoodAdapter.foodList2 = addData
                addFoodAdapter.posi = position
                addFoodAdapter.notifyDataSetChanged()
                bottomSheetDialog.dismiss()
            }

        })

        foodSearchBinding.rvAddFoodList.adapter = foodListAdapter

        foodSearchBinding.searchImage.setOnClickListener {
            var text = foodSearchBinding.etAddMealtype.text.toString()
            getFoodList(text)

        }


        /* foodList = rvAddFoodList(context)

         foodSearchBinding.mealTypeRv.adapter = mealTypeAdapter

         mealTypeBinding.doneBtn.setOnClickListener {
             if (mealTypeId2 != 0) bottomSheetDialog.hide() else Util.toast(
                 requireContext(),
                 "Select meal type first"
             )
         }*/

        mealTypeAdapter.setOnClick(object : FragInterface {

            override fun onClick(id: Int, title: String) {

                mealTypeId = id

                mealTypeId2 = id

                addMealBinding.mealType.text = title
            }

        })
    }

    private fun addFood() {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        addMealBinding.addFoodRv.layoutManager = linearLayout
        addMealBinding.addFoodRv.setHasFixedSize(true)
        addFoodAdapter = AddFoodAdapter(context)
        if (arguments == null) addFoodAdapter.addFoodModel.add(setDataInAddFoodRV())
        addFoodAdapter.notifyDataSetChanged()
        addMealBinding.addFoodRv.adapter = addFoodAdapter
        addMealBinding.addFoodBtn.setOnClickListener {
            addFoodAdapter.addFoodModel.add(setDataInAddFoodRV())
            addFoodAdapter.notifyDataSetChanged()
            addMealBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            /* var i=addFoodAdapter.itemCount
             i--
             Log.v("countItem",i.toString())
             addMealBinding.addFoodRv.smoothScrollToPosition(i)*/
        }
    }

    private fun setDataInAddFoodRV(): MealBody.FoodData {
        return MealBody.FoodData(0, 0.0, "", 0, 0, 0.0, "", 0, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        loading.hideProgress()
    }
}
