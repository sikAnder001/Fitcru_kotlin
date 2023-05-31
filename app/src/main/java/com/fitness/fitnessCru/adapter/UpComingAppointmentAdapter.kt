package com.fitness.fitnessCru.adapter

import android.app.Activity
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.UpcomingAppointmentListBinding
import com.fitness.fitnessCru.interfaces.AppointmentInterface
import com.fitness.fitnessCru.response.UpcomingAppointmentResponse
import java.text.SimpleDateFormat
import java.util.*

class UpComingAppointmentAdapter(private val activity: Activity) :
    RecyclerView.Adapter<UpComingAppointmentAdapter.ViewHolder>() {

    private var list = listOf<UpcomingAppointmentResponse.ClientAppointments>()
    lateinit var questionaryInterface: AppointmentInterface

    inner class ViewHolder(binding: UpcomingAppointmentListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding = binding

        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(data: UpcomingAppointmentResponse.ClientAppointments, position: Int) {
            binding.apply {
                Glide.with(activity!!).load(data.time_slot.coach.image_url)
                    .placeholder(R.drawable.place_holder)
                    .into(trainerImage)
                trainerName.text = data.time_slot.coach.coach_name

                val date = changeFormat(data.appointment_date)
                dateTv.text = date

                var time = getTime(
                    (data.time_slot.slot_start_time ?: data.time_slot.slot_strdy_strt_time)!!
                )
                appTimeTv.text = time

                cancelAppoint.setOnClickListener {
                    questionaryInterface.onClick(
                        data.id,
                        data.time_slot.coach_id,
                        data.coach_time_slot_id
                    )
                    notifyDataSetChanged()
                }
            }
        }
    }

    fun setOnClick(questionaryInterface: AppointmentInterface) {
        this.questionaryInterface = questionaryInterface
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpComingAppointmentAdapter.ViewHolder {
        return ViewHolder(
            UpcomingAppointmentListBinding.inflate(
                activity.layoutInflater,
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: UpComingAppointmentAdapter.ViewHolder, position: Int) =
        holder.bind(list[position], position)

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    fun setList(list: List<UpcomingAppointmentResponse.ClientAppointments>) {
        this.list = list
        notifyDataSetChanged()
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun changeFormat(join: String?): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateTime = simpleDateFormat.parse(join)
        val simpleDateFormat1 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("dd MMM YYYY")
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val date = simpleDateFormat1.format(dateTime)

        return date
    }

}
