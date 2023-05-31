package com.fitness.fitnessCru.adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.MySummary
import com.fitness.fitnessCru.holder.CalendarViewHolder
import com.fitness.fitnessCru.utility.CalendarUtils
import java.time.LocalDate

class CalendarAdapter(days: List<LocalDate>, year: List<LocalDate>, onItemListener: MySummary) :
    RecyclerView.Adapter<CalendarViewHolder>() {


    private val days: List<LocalDate>
    private val year: List<LocalDate>
    private val onItemListener: OnItemListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.getContext())
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams: ViewGroup.LayoutParams = view.getLayoutParams()
        return CalendarViewHolder(view, onItemListener, days)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
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
            holder.weekDay.setText(date.getDayOfWeek().toString())
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
}