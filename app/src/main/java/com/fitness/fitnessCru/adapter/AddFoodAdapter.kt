package com.fitness.fitnessCru.adapter


import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.body.MealBody
import com.fitness.fitnessCru.databinding.AddMealAddFoodRvItemBinding
import com.fitness.fitnessCru.interfaces.QuestionaryInterface
import com.fitness.fitnessCru.response.FoodListResposne
import java.text.DecimalFormat
import kotlin.properties.Delegates


class AddFoodAdapter(private val context: Context?) :
    RecyclerView.Adapter<AddFoodAdapter.ViewHolder>() {

    var foodList2 = ArrayList<FoodListResposne.Data>()
    var position by Delegates.notNull<Int>()
    var addFoodModel = ArrayList<MealBody.FoodData>()
    var foodList = ArrayList<FoodListResposne.Data>()

    var posi: Int = -1

    lateinit var questionaryInterface: QuestionaryInterface
    lateinit var foodSearchInterface: FoodSearchInterface

    var weight: List<FoodListResposne.Data.UnitsList>? = null
    var food = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFoodAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val addFoodBinding = AddMealAddFoodRvItemBinding.inflate(inflater, parent, false)
        return ViewHolder(addFoodBinding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: AddFoodAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return try {
            addFoodModel.size
        } catch (e: Exception) {
            0
        }
    }

    fun setOnClick(questionaryInterface: QuestionaryInterface) {
        this.questionaryInterface = questionaryInterface
    }

    fun setList(foodList2: ArrayList<FoodListResposne.Data>, position: Int) {
        this.foodList2 = foodList2
        this.posi = position
    }


    fun sitOnClick(foodSearchInterface: FoodSearchInterface) {
        this.foodSearchInterface = foodSearchInterface
    }

    inner class ViewHolder(addFoodBinding: AddMealAddFoodRvItemBinding) :
        RecyclerView.ViewHolder(addFoodBinding.root) {
        var addFoodBinding = addFoodBinding

        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(position: Int) {
            with(addFoodBinding) {
                /*if (position == 0) {
                    ivCross.visibility = View.INVISIBLE
                    foodNameRvItemContainer.setLayoutParams(
                        LinearLayoutCompat.LayoutParams(
                            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                        )
                    )
                }*/

                ivCross.setOnClickListener {
                    if (addFoodModel[adapterPosition].id != null) {
                        questionaryInterface.onClick(
                            addFoodModel[adapterPosition].id!!
                        )
                    }
                    if (addFoodModel.size > 0) {
                        addFoodModel.remove(addFoodModel[adapterPosition])
                        notifyDataSetChanged()
                    }
                }
                var adapter =
                    ArrayAdapter<Any?>(
                        context!!,
                        android.R.layout.select_dialog_item,
                        foodList as List<FoodListResposne.Data>
                    )
                /*if(!tList[position].isNullOrEmpty()){*/


                var calorie = 0
                var oneSizeCalorie = 0.0
                var size = 0
                var addedWeight = ""
                var addedCalorie = ""
                var indeces = 0
                var indeces2 = 0
                var indexed = ""
                var weight2: List<FoodListResposne.Data.ServingSize>? = null
                var unitList: List<FoodListResposne.Data.UnitsList>? = null
                var arr = mutableListOf<Int>()
//                var foodName = mutableListOf<String>()

//                mealTypeTv.setAdapter(adapter)
//                mealTypeTv.setText(addFoodModel[position].meal_name)

                var item: FoodListResposne.Data


                etAddMealType.setOnClickListener {
                    foodSearchInterface.onClick(adapterPosition)
                }

                if (adapterPosition == posi && !foodList2.isNullOrEmpty()) {
                    //  Toast.makeText(context, posi.toString(), Toast.LENGTH_SHORT).show()
                    addFoodModel[position].food_id = foodList2[0].id
                    addFoodModel[position].meal_name = foodList2[0].name
                    etAddMealType.setText(foodList2[0].name)

                    var oneSizeCalorie =
                        foodList2[0].servin_size[0].calorie.toDouble() / foodList2[0].servin_size[0].serving_size.toDouble()

                    weight = foodList2[0].units_list

                    if (foodList2[0].servin_size[0].food_unit_id == 1) {
                        addFoodModel[position].oneSize = oneSizeCalorie

                    } else {
                        for (element in foodList2[0].units_list) {
                            if (element.food_unit_id == foodList2[0].servin_size[0].food_unit_id) {
                                addFoodModel[position].oneSize = oneSizeCalorie / element.weight
                            }
                        }
                    }

                    spinnerUnit.adapter = ArrayAdapter<Any?>(
                        context,
                        android.R.layout.simple_list_item_activated_1,
                        weight!!
                    )

                    foodList2.clear()
                } else if (addFoodModel[position].food_id != 0) {

                    etAddMealType.setText(addFoodModel[position].meal_name)

                    if (addFoodModel[position].oneSize == null) {

                        var oneSizeCalorie =
                            addFoodModel[position].serving_sizes!!.calorie.toDouble() / addFoodModel[position].serving_sizes!!.serving_size.toDouble()
                        weight = addFoodModel[position]!!.units_list

                        if (addFoodModel[position].serving_sizes!!.food_unit_id == 1) {
                            addFoodModel[position].oneSize = oneSizeCalorie
                            Log.v("onceSize", oneSizeCalorie.toString())
                        } else {
                            for (element in weight!!) {
                                if (element.food_unit_id == addFoodModel[position].serving_sizes!!.food_unit_id) {
                                    addFoodModel[position].oneSize = oneSizeCalorie / element.weight
                                }
                            }
                        }

                    }
                    foodKcalTv.text = "${addFoodModel[position].total_calorie} Kcal"

                    spinnerUnit.adapter = ArrayAdapter<Any?>(
                        context,
                        android.R.layout.simple_list_item_activated_1,
                        weight!!
                    )


                    for ((index, eleme) in weight!!.withIndex()) {
                        if (addFoodModel[position].food_unit_id == eleme.food_unit_id) {
                            spinnerUnit.setSelection(index)
                            Log.v("UnitTest", index.toString())

                        }
                    }
                }

//                foodName.clear()
                //search filter textmate..............
                /* etAddMealtype.addTextChangedListener(object : TextWatcher {
                     override fun beforeTextChanged(
                         s: CharSequence?,
                         start: Int,
                         count: Int,
                         after: Int
                     ) {

                     }

                     override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                         if (s.toString().isNotEmpty()){
                             filter(s.toString())

                         }else{
                             rvAddFoodList.adapter=AddFoodAdapter(context)
                           //  view.recycler_contact_list_a.adapter=UserListAdapter(requireContext(),list)
                         }
                     }

                     override fun afterTextChanged(s: Editable?) {

                     }

                 })*/


                /*  fun filter(toString: String) {
                        (rvAddFoodList.adapter as AddFoodAdapter).foodList =
                            ArrayList(list.filter {
                                it.profileName?.contains(
                                    text.toString(),
                                    true
                                ) == true
                            })
                        (rvAddFoodList.adapter as AddFoodAdapter).notifyDataSetChanged()

                    }

                  fun searchResturent(searchResturentId: String) {
                      homeViewModel.hitGetResturentList(
                          Authorization = "Bearer " + sharedPreference.access_token,
                          url = "${AppConstant.BASE_URL}api/customer/resturants?lat=$lat&long=$long}&lang=en&page=1&mindeliverytime=&rating=&recent=&freedelivery=&minorder=&sort=&search=${searchResturentId}"
                      )



                      homeViewModel.responseResturentList.observe(viewLifecycleOwner) {
                          if (it.status == true) {
                              it.data.let {
                                  if (it != null) {
                                      searchNameForResturent =
                                          it.map { it.restaurant_name.toString() }.joinToString(",")
                                      setResturentListAdapter(it)
                                  }
                              }

                          }

                          private fun setResturentListAdapter(resturentListt: ArrayList<ResponseResturentList.Data>) {
                              rvResturentList.adapter =
                                  RestaurentListAdapter(requireContext(), resturentListt, object : ResturentClick {
                                      override fun onClickItemResturent(id: String,isResturentOpen:Boolean) {
                                          if (isResturentOpen==true){
                                              startActivity(
                                                  Intent(requireContext(), ResturentDetailActivity::class.java)
                                                      .putExtra("id", id)
                                                      .putExtra("resturant", "0")
                                              )

                                          }else{
                                              showToast("Sorry Resturent is closed,You can't order at this moment")
                                          }

                                      }
                                  })
                          }


 */
                /*   foodName.add("Select Food")
                   for ((index, element) in foodList.withIndex()) {
                       foodName.add(element.name)
                   }

                   spinnermealtype.adapter = ArrayAdapter<Any?>(
                       context,
                       android.R.layout.simple_list_item_activated_1,
                       foodName as List<Any?>
                   )

                   if (addFoodModel[position].meal_name.isNotEmpty()) {
                       addedWeight = addFoodModel[position].quantity.toString()
                       addedCalorie = addFoodModel[position].total_calorie.toString()
                       indeces2 = 1
                       foodKcalTv.text = "${addFoodModel[position].total_calorie} Kcal"

                       for ((index, element) in foodList.withIndex()) {
                           if (element.name == addFoodModel[position].meal_name) {
                               indeces = index + 1

                               spinnermealtype.setSelection(indeces)

                               item = element

                               weight2 = item.servin_size
                           }
                       }

                   } else {
                       if (addFoodModel[position].food_id != 0) {

                           for ((index, element) in foodList.withIndex()) {
                               if (addFoodModel[position].food_id.equals(element.id)) {
                                   spinnermealtype.setSelection(index + 1)

                                   addedWeight = addFoodModel[position].quantity.toString()
                                   addedCalorie = addFoodModel[position].total_calorie.toString()


                                   item = element

                                   weight2 = item.servin_size

   spinnerUnit.adapter = ArrayAdapter<Any?>(
                                       context,
                                       android.R.layout.simple_list_item_activated_1,
                                       item.servin_size
                                   )

                               }
                           }
                       }
                   }


                   spinnermealtype.onItemSelectedListener =
                       object : AdapterView.OnItemSelectedListener {
                           override fun onItemSelected(
                               parent: AdapterView<*>?,
                               view: View?,
                               position_: Int,
                               id: Long
                           ) {
                               (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                               if (position_ > 0) {
                                   foodNameRvWeightContainer.visibility = View.VISIBLE
                                   foodNameRvWeightContainer4.visibility = View.GONE
                                   foodKcalTv.visibility = View.VISIBLE
                                   if (indeces2 != 1) {
                                       for ((index, element) in foodList.withIndex()) {
                                           if (parent.selectedItem.toString().equals(element.name)) {

                                               item = element

                                               addFoodModel[position].food_id = item.id

                                               weight = item.servin_size

                                               spinnerUnit.adapter = ArrayAdapter<Any?>(
                                                   context,
                                                   android.R.layout.simple_list_item_activated_1,
                                                   item.servin_size
                                               )

                                               Log.v("food selected", position.toString())


                                               if (weight2 != null) {
                                                   for ((indec, eleme) in element.servin_size.withIndex()) {
                                                       if (addFoodModel[position].food_unit_id == eleme.food_unit_id) {
                                                           spinnerUnit.setSelection(indec)
                                                           Log.v("UnitTest", indec.toString())
                                                       }
                                                   }
                                               }
                                           }
                                       }
                                   } else {

                                       for ((index, element) in foodList.withIndex()) {
                                           if (parent.selectedItem.toString().equals(element.name)) {


                                               item = element

                                               addFoodModel[position].food_id = item.id

                                               weight2 = item.servin_size

                                               spinnerUnit.adapter = ArrayAdapter<Any?>(
                                                   context,
                                                   android.R.layout.simple_list_item_activated_1,
                                                   item.servin_size
                                               )

                                               Log.v(
                                                   "food selected2",
                                                   addFoodModel[position].total_calorie.toString()
                                               )

                                               for ((indec, eleme) in element.servin_size.withIndex()) {
                                                   if (addFoodModel[position].food_unit_id == eleme.food_unit_id) {
                                                       spinnerUnit.setSelection(indec)
                                                       Log.v("UnitTest", indec.toString())

                                                   }
                                               }

                                           }
                                       }
                                   }
                               } else {
                                   (view as TextView).setTextColor(Color.GRAY)
                                   foodNameRvWeightContainer.visibility = View.GONE
                                   foodNameRvWeightContainer4.visibility = View.VISIBLE
                                   foodKcalTv.visibility = View.GONE
                                   addFoodModel[position].food_id = 0
                                   weight = null
                               }
                           }

                           override fun onNothingSelected(parent: AdapterView<*>?) {

                           }
                       }*/

                spinnerUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position_: Int,
                        id: Long
                    ) {
                        (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                        addFoodModel[position].unit = parent!!.selectedItem.toString()

                        Log.v("unit1", parent.selectedItem.toString())

                        if (weight != null) {
                            for ((index, element) in weight!!.withIndex()) {
                                if (parent.selectedItem.toString().equals(element.food_unit_name)) {
//                                    addFoodModel[position].serving_size_id = element.id
                                    addFoodModel[position].food_unit_id = element.food_unit_id
//                                    var serveweight = element.serving_size.toInt()

//                                    foodKcalTv.text = "${element.calorie} Kcal"
//                                    calorie = element.
                                    size = element.weight

                                    Log.v(
                                        "unitweight",
                                        parent.selectedItem.toString() + ", calori" + calorie + ", size" + size
                                    )


                                    if (addFoodModel[position].unit == element.food_unit_name) {
                                        spinnerUnit.setSelection(index)
                                    }
//                                    arr.clear()
//                                    for (i in element.serving_size.toInt() until 1000) {
//                                        arr.add(serveweight)
//                                        serveweight += element.serving_size.toInt()
//                                        if (serveweight >= 1000)
//                                            break
//                                    }
                                    Log.v("unit 1", addFoodModel[position].total_calorie.toString())

                                    if (addedWeight.isNotEmpty()) {
                                        (spinnerUnitWeightET as TextView).text = addedWeight
                                        /* if (addedCalorie.isNotEmpty()) {
                                             foodKcalTv.text =
                                                 addedCalorie + " Kcal"
                                         } else {*/

                                        if ((((addedWeight.toDouble()) * size) * addFoodModel[position].oneSize!!).isNaN()) {
                                            foodKcalTv.text =
                                                "0.0 Kcal"
                                        } else {

                                            var angle =
                                                (((addedWeight.toDouble()) * size) * addFoodModel[position].oneSize!!)
                                            val df = DecimalFormat("#0.00")
                                            val angleFormated = df.format(angle)
                                            foodKcalTv.text =
                                                angleFormated + " Kcal"
                                            addFoodModel[adapterPosition].total_calorie =
                                                angleFormated.toString().toDouble()
                                        }
                                    } else {
                                        var angle =
                                            (((addFoodModel[position].quantity) * size) * addFoodModel[position].oneSize!!)
                                        val df = DecimalFormat("#0.00")
                                        val angleFormated = df.format(angle)
                                        foodKcalTv.text =
                                            angleFormated + " Kcal"
                                        addFoodModel[adapterPosition].total_calorie =
                                            angleFormated.toString().toDouble()
                                    }


                                    /*  spinnerUnitWeight.adapter = ArrayAdapter<Any?>(
                                          context,
                                          android.R.layout.simple_list_item_activated_1,
                                          arr as List<Any?>
                                      )*/

                                }
                            }
                        } else {

                            for ((index, element) in weight2!!.withIndex()) {
                                if (parent.selectedItem.toString().equals(element.unit)) {
                                    addFoodModel[position].serving_size_id = element.id
                                    addFoodModel[position].food_unit_id = element.food_unit_id
                                    var serveweight = element.serving_size.toInt()

                                    Log.v("unitweight2", parent.selectedItem.toString())

                                    arr.clear()
                                    for (i in element.serving_size.toInt() until 1000) {
                                        arr.add(serveweight)
                                        serveweight += element.serving_size.toInt()
                                        if (serveweight >= 1000)
                                            break
                                    }

                                    /*spinnerUnitWeight.adapter = ArrayAdapter<Any?>(
                                        context,
                                        android.R.layout.simple_list_item_activated_1,
                                        arr as List<Any?>
                                    )*/

                                    calorie = element.calorie.toInt()
                                    size = element.serving_size.toInt()


                                    foodKcalTv.text = "${element.calorie} Kcal"
                                    if (addedWeight.isNotEmpty()) {
                                        (spinnerUnitWeightET as TextView).text = addedWeight
                                        if ((((addedWeight.toDouble()) * size) * addFoodModel[position].oneSize!!).isNaN()) {
                                            foodKcalTv.text =
                                                "0.0 Kcal"

                                        } else {
                                            var angle =
                                                (((addedWeight.toDouble()) * size) * addFoodModel[position].oneSize!!)
                                            val df = DecimalFormat("#0.00")
                                            val angleFormated = df.format(angle)
                                            foodKcalTv.text =
                                                angleFormated + " Kcal"
                                            addFoodModel[adapterPosition].total_calorie =
                                                angleFormated.toString().toDouble()

                                        }

                                    } else {
                                        var angle =
                                            (((addFoodModel[position].quantity) * size) * addFoodModel[position].oneSize!!)
                                        val df = DecimalFormat("#0.00")
                                        val angleFormated = df.format(angle)
                                        foodKcalTv.text =
                                            angleFormated + " Kcal"
                                        addFoodModel[adapterPosition].total_calorie =
                                            angleFormated.toString().toDouble()
                                    }

                                }
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }

                try {
                    spinnerUnitWeightET.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {

                        }

                        override fun afterTextChanged(s: Editable?) {
                            try {
                                if (s!!.isNotEmpty()) {
                                    Log.v("inside", "test")
                                    if ((((s.toString().toDouble()) / size) * calorie).isNaN()) {
                                        foodKcalTv.text = "0.00 Kcal"
                                        addFoodModel[adapterPosition].total_calorie = 0.0
                                    } else {
                                        var angle = ((s.toString()
                                            .toDouble()) * size * addFoodModel[position].oneSize!!)
                                        val df = DecimalFormat("#0.00")
                                        val angleFormated = df.format(angle)

                                        Log.v("totalCalorie", angleFormated.toDouble().toString())
                                        foodKcalTv.text =
                                            angleFormated + " Kcal"
                                        addFoodModel[adapterPosition].total_calorie =
                                            angleFormated.toString().toDouble()
                                    }

                                    addFoodModel[adapterPosition].quantity = s.toString().toDouble()
                                } else {
                                    foodKcalTv.text = "0.00 Kcal"
                                }
                            } catch (e: Exception) {
                                foodKcalTv.text = "0.00 Kcal"
                                Log.e("meal Error", e.toString())
                            }
                        }
                    })
                } catch (e: Exception) {
                }
                spinnerUnitWeightET.setText("${addFoodModel[position].quantity}")
                foodKcalTv.text = "${addFoodModel[position].total_calorie} Kcal"
            }
        }
    }

    interface FoodSearchInterface {
        fun onClick(searchitem: Int)
    }
}