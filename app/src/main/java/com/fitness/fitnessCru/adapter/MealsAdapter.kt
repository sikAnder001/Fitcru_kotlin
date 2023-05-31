package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.databinding.MealItemListBinding
import com.fitness.fitnessCru.interfaces.QuestionaryInterface2
import com.fitness.fitnessCru.model.MealsPojo
import com.fitness.fitnessCru.utility.Constants
import java.text.SimpleDateFormat
import java.util.*


class MealsAdapter(val context: Context) :
    RecyclerView.Adapter<MealsAdapter.ViewHolder>() {

    private var list = listOf<MealsPojo.UserSelectedMeal>()
    var date: String = ""

    lateinit var questionaryInterface: QuestionaryInterface2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MealItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    fun setList(lists: List<MealsPojo.UserSelectedMeal>) {
        list = lists
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position], position, context)

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    fun setOnClick(questionaryInterface: QuestionaryInterface2) {
        this.questionaryInterface = questionaryInterface
    }

    inner class ViewHolder(binding: MealItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding = binding

        fun bind(data: MealsPojo.UserSelectedMeal, position: Int, context: Context) {
            binding.apply {
                tvMealType.text = data.meal_type_name.mealtype
                foodName.text = data.meal_name
                var time = getTime(data.time)
                timeTV.text = time

                var unitId = data.food_unit_id
                for (el in data.units_list) {
                    if (el.food_unit_id == unitId) {
                        servingTV.text = "Serving ${data.quantity} ${el.food_unit_name}"
                    }
                }
//                servingTV.text = "Serving ${data.quantity} ${data.meal_type_name.foods!!.servin_size.unit}"

                Glide.with(context!!).load(data.meal_type_name.foods!!.image_url)
                    .placeholder(R.drawable.place_holder)
                    .into(imageView)

                checkbox.isChecked = data.is_complete.toInt() == 1

                checkbox.setOnClickListener {
                    //   click.click(data)
                    questionaryInterface.onClick(data.meal_type_id, data.time)

                }

                editLay.setOnClickListener {
                    val intent = Intent(context.applicationContext, SetupAllActivity::class.java)
                    intent.putExtra(Constants.DESTINATION, Constants.ADD_MEAL)
                    intent.putExtra("meal_id", data.meal_type_id)
                    intent.putExtra("name", data.meal_name)
                    intent.putExtra("time", data.time)
                    intent.putExtra("date", date)
                    intent.putExtra("mealType", data.meal_type_name.mealtype)
                    context.startActivity(intent)
                }
            }
        }

        private fun getTime(time: String): String {
            val sdf = SimpleDateFormat("HH:mm")
            var date: Date? = null
            date = sdf.parse(time)
            val c: Calendar = Calendar.getInstance()
            c.time = date
            val hoour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            var hour = hoour
            var am_pm = ""
            when {
                hour == 0 -> {
                    hour += 12
                    am_pm = "AM"
                }
                hour == 12 -> am_pm = "PM"
                hour > 12 -> {
                    hour -= 12
                    am_pm = "PM"
                }
                else -> am_pm = "AM"
            }
            val hours = if (hour < 10) "0$hour" else hour
            val min = if (minute < 10) "0$minute" else minute

            return "$hours:$min $am_pm"
        }
    }
}