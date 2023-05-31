package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.MealDetailItemBinding
import com.fitness.fitnessCru.response.MealTypeDetailResponse

class MealDetailAdapter(
    var context: Context?,
    var list: List<MealTypeDetailResponse.Data.FoodDatum>,
    var listner: AvailabilityInterfaceTwo
) :
    RecyclerView.Adapter<MealDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MealDetailItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    inner class ViewHolder(val binding: MealDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mealData: MealTypeDetailResponse.Data.FoodDatum) {
            binding.apply {
                tvMealType.text = mealData.meal_name
                tvDescription.text = "${mealData.quantity} ${mealData.unit}"
                if (mealData.is_complete.equals("1")) {
                    checkbox.isChecked = true
                }
                checkbox.setOnClickListener {
                    if (checkbox.isChecked) {
                        listner.onClick(
                            mealData.id,
                            mealData.food_id,
                            mealData.date,
                            mealData.time,
                            mealData.consumed_meal_id!!,
                            0
                        )
                    } else {
                        listner.onClick(
                            mealData.id,
                            mealData.food_id,
                            mealData.date,
                            mealData.time,
                            mealData.consumed_meal_id!!,
                            1
                        )
                    }
                }
            }
        }
    }

    interface AvailabilityInterfaceTwo {
        fun onClick(slotId: Int, coachId: Int, date: String, time: String, consume_id: Int, id: Int)
    }

}