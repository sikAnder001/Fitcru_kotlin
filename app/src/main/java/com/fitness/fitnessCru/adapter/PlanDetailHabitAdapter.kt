package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.HabitRvItemListBinding
import com.fitness.fitnessCru.response.DashBoardResponse

class PlanDetailHabitAdapter(
    private val context: Context?,
    val listener: SelectVideoListener
) : RecyclerView.Adapter<PlanDetailHabitAdapter.ViewHolder>() {

    private var list = listOf<DashBoardResponse.Data.Habit>()

    inner class ViewHolder(habitBinding: HabitRvItemListBinding) :
        RecyclerView.ViewHolder(habitBinding.root) {
        var habitBinding = habitBinding

        fun bind(
            data: DashBoardResponse.Data.Habit,
            position: Int
        ) {
            habitBinding.apply {

                Glide.with(context!!).load(data.banner)
                    .placeholder(R.drawable.place_holder)
                    .into(coachImage)

                workoutType.text = data.name

                descriptionTv.text = data.content

                if (list!![position].video.isNullOrEmpty()) {
                    playBtn.visibility = View.GONE
                } else {
                    playBtn.visibility = View.VISIBLE
                }

                itemView.setOnClickListener {
                    if (list!![position].video != null) {
                        listener.onClick(list!![position].video.toString())
                    } else {

                    }
                }
                /* durationTv.text = data.time.toString()

                 personTv.text = data.cals.toString()

                 if (data.status == 1) {
                     pendingOrCompletedTv.text = "Completed"
                     pendingAndCompletedContainer.setBackgroundResource(R.drawable.completed_background)
                 } else {
                     pendingOrCompletedTv.text = "Pending"
                     pendingAndCompletedContainer.setBackgroundResource(R.drawable.pending_background)
                 }*/
            }

            /*  itemView.setOnClickListener {
                  val position = adapterPosition
                  listener.onClick(position)
              }*/
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlanDetailHabitAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val habitBinding =
            HabitRvItemListBinding.inflate(inflater, parent, false)

        return ViewHolder(habitBinding)
    }

    override fun onBindViewHolder(holder: PlanDetailHabitAdapter.ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<DashBoardResponse.Data.Habit>?) {
        this.list = list!!
        notifyDataSetChanged()
    }

    interface SelectVideoListener {
        fun onClick(video: String)
    }
}