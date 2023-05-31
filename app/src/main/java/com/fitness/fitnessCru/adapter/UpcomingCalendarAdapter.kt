package com.fitness.fitnessCru.adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.fragment.UpcomingFragment
import com.fitness.fitnessCru.utility.CalendarUtils
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class UpcomingCalendarAdapter(
    days: List<LocalDate>,
    year: List<LocalDate>,
    onItemListener: UpcomingFragment
) :
    RecyclerView.Adapter<UpcomingCalendarAdapter.UpcomingCalendarViewHolder>() {

    private val days: List<LocalDate>
    private val year: List<LocalDate>
    private val onItemListener: OnItemListener
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpcomingCalendarViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.getContext())
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams: ViewGroup.LayoutParams = view.getLayoutParams()
        return UpcomingCalendarViewHolder(view, onItemListener, days)
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


    class UpcomingCalendarViewHolder internal constructor(
        itemView: View,
        onItemListener: OnItemListener,
        days: List<LocalDate>
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val days: List<LocalDate>
        var parentView: ConstraintLayout
        var dayOfMonth: TextView
        var weekDay: TextView
        var cal: LinearLayoutCompat
        private val onItemListener: UpcomingCalendarAdapter.OnItemListener

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onClick(view: View) {


            val day1 = "${days[adapterPosition]}"
            val day2 = "${LocalDate.now()}"
            val parse1: Date = SimpleDateFormat("yyyy-MM-dd").parse(day1)
            val parse2: Date = SimpleDateFormat("yyyy-MM-dd").parse(day2)
            val cmp = parse1.compareTo(parse2)
            if (cmp >= 0)
                onItemListener.onItemClick(adapterPosition, days[adapterPosition])
        }

        init {
            parentView = itemView.findViewById(R.id.parentView)
            dayOfMonth = itemView.findViewById(R.id.cellDayText)
            weekDay = itemView.findViewById(R.id.weekDay)
            cal = itemView.findViewById(R.id.cal)
            this.onItemListener = onItemListener
            itemView.setOnClickListener(this)
            this.days = days
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: UpcomingCalendarViewHolder, position: Int) {
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