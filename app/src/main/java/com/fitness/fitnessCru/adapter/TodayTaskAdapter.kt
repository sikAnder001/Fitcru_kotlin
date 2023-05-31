package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.MealDetailActivity
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.databinding.DashTodayTaskListBinding
import com.fitness.fitnessCru.interfaces.ConsumedMealInterface
import com.fitness.fitnessCru.response.DashBoardResponse
import com.fitness.fitnessCru.utility.Constants
import java.text.SimpleDateFormat
import java.util.*

class TodayTaskAdapter(private val context: Context?, val date: String) :
    RecyclerView.Adapter<TodayTaskAdapter.ViewHolder>() {

    private var list = ArrayList<DashBoardResponse.Data.TodayTask>()
    private lateinit var click: ConsumedMealInterface

    private var list2 = ArrayList<DashBoardResponse.Data.CookingToday>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayTaskAdapter.ViewHolder {
        return ViewHolder(
            DashTodayTaskListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TodayTaskAdapter.ViewHolder, position: Int) =
        holder.bind(list[position])

    fun setList(list: ArrayList<DashBoardResponse.Data.TodayTask>) {

        this.list = list

        notifyDataSetChanged()
    }

    fun setConsumed(consumedMealInterface: ConsumedMealInterface) {
        this.click = consumedMealInterface
    }

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    inner class ViewHolder(val binding: DashTodayTaskListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DashBoardResponse.Data.TodayTask) {
            binding.apply {
                try {
                    tvMealType.text = data.meal_type_name.mealtype
                    tvMealName.text = data.meal_name

                    var time = getTime(data.time)
                    tvTime.text = time

                    Glide.with(context!!).load(data.meal_type_name.foods.image_url)
                        .placeholder(R.drawable.place_holder)
                        .into(imageView)

                    checkbox.isChecked = data.is_complete.toInt() == 1

                    checkbox.setOnClickListener {
                        //   click.click(data)
                        var intent = Intent(context, MealDetailActivity::class.java)
                        var bundle = Bundle()
                        bundle.putInt("data", data.meal_type_id)
                        bundle.putString("date", date)
                        bundle.putString("time", data.time)
                        bundle.putString("back_location", "dashboard")
                        intent.putExtras(bundle)
                        context?.startActivity(intent)

                    }

                    imageView.setOnClickListener {
                        val intent =
                            Intent(context.applicationContext, SetupAllActivity::class.java)
                        intent.putExtra(Constants.DESTINATION, Constants.RECIPE)
                        intent.putExtra("id", data.meal_type_name.foods.id)
                        context.startActivity(intent)
                    }
                } catch (e: Exception) {
                }
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

    fun setList2(list2: ArrayList<DashBoardResponse.Data.CookingToday>) {
        this.list2 = list2
        notifyDataSetChanged()
    }
}
