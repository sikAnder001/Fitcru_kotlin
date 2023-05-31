package com.fitness.fitnessCru.activities

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.adapter.CalendarAdapter
import com.fitness.fitnessCru.databinding.ActivityMySummaryBinding
import com.fitness.fitnessCru.utility.CalendarUtils.daysInMonthArray
import com.fitness.fitnessCru.utility.CalendarUtils.formattedDate
import com.fitness.fitnessCru.utility.CalendarUtils.getWeekDayName
import com.fitness.fitnessCru.utility.CalendarUtils.monthFromDate
import com.fitness.fitnessCru.utility.CalendarUtils.selectedDate
import com.fitness.fitnessCru.utility.CalendarUtils.yearFromDate
import java.time.LocalDate

class MySummary : AppCompatActivity(), CalendarAdapter.OnItemListener {
    private var binding: ActivityMySummaryBinding? = null

    lateinit var calendarAdapter: CalendarAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMySummaryBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDate = LocalDate.now()
            this.setMonthView()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        binding!!.monthYearTV.setText(selectedDate?.let { monthFromDate(it) })
        val daysInMonth: List<LocalDate?> = daysInMonthArray(selectedDate)

        binding!!.yearTV.setText(selectedDate?.let { yearFromDate(it) })
        val year: List<LocalDate?> = daysInMonthArray(selectedDate)

        calendarAdapter = CalendarAdapter(
            daysInMonth as List<LocalDate>,
            year as List<LocalDate>, this
        )
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )
        binding!!.calendarRecyclerView.setLayoutManager(layoutManager)
        binding!!.calendarRecyclerView.setAdapter(calendarAdapter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, date: LocalDate?) {
        if (date != null) {
            selectedDate = date
            val day: String = formattedDate(date)
            val weekDay: String = getWeekDayName(day)
            Toast.makeText(this, "" + weekDay, Toast.LENGTH_SHORT).show()
            if (calendarAdapter != null) {
                calendarAdapter.notifyDataSetChanged()
            }
        }
    }

}