package com.fitness.fitnessCru.adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.fragment.Nutrition
import com.fitness.fitnessCru.holder.MealCalendarViewHolder
import com.fitness.fitnessCru.utility.CalendarUtils
import java.time.LocalDate

class MealCalendarAdapter(days: List<LocalDate>, year: List<LocalDate>, onItemListener: Nutrition) :
    RecyclerView.Adapter<MealCalendarViewHolder>() {

    private val days: List<LocalDate>
    private val year: List<LocalDate>
    private val onItemListener: OnItemListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealCalendarViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.getContext())
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams: ViewGroup.LayoutParams = view.getLayoutParams()
        return MealCalendarViewHolder(view, onItemListener, days)
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate?)
    }

    init {
        this.days = days
        this.year = year
        this.onItemListener = onItemListener
    }

    override fun getItemCount(): Int {
        return days.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MealCalendarViewHolder, position: Int) {
        val date: LocalDate = days[position]
        if (date == null) {
            holder.dayOfMonth.setText("")
            // remove views which are not visible
            holder.cal.setVisibility(View.GONE)
            holder.dayOfMonth.setVisibility(View.GONE)
            holder.weekDay.setVisibility(View.GONE)
            holder.parentView.setVisibility(View.GONE)
        } else {
            holder.cal.setVisibility(View.VISIBLE)
            holder.dayOfMonth.setVisibility(View.VISIBLE)
            holder.weekDay.setVisibility(View.VISIBLE)
            holder.parentView.setVisibility(View.VISIBLE)
            holder.dayOfMonth.setText(date.getDayOfMonth().toString())
            holder.weekDay.setText(date.getDayOfWeek().toString().substring(0, 3))
            if (date == CalendarUtils.selectedDate) {
                holder.dayOfMonth.setTextColor(Color.BLACK)
                holder.weekDay.setTextColor(Color.BLACK)
                holder.parentView.setBackgroundResource(R.drawable.date_selected)
            } else {
                holder.parentView.setBackgroundResource(0)
                holder.dayOfMonth.setTextColor(Color.WHITE)
                holder.weekDay.setTextColor(Color.WHITE)
                holder.parentView.setBackgroundResource(R.drawable.back_cal)
            }
        }
    }
}