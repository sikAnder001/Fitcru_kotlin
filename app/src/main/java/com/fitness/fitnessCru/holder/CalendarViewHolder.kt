package com.fitness.fitnessCru.holder

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.CalendarAdapter
import java.time.LocalDate

class CalendarViewHolder internal constructor(
    itemView: View,
    onItemListener: CalendarAdapter.OnItemListener,
    days: List<LocalDate>
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val days: List<LocalDate>
    var parentView: ConstraintLayout
    var dayOfMonth: TextView
    var weekDay: TextView
    var cal: LinearLayoutCompat
    private val onItemListener: CalendarAdapter.OnItemListener
    override fun onClick(view: View) {
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