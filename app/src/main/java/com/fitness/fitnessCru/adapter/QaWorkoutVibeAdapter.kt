package com.fitness.fitnessCru.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.interfaces.QuestionaryInterface
import com.fitness.fitnessCru.response.WorkoutVibeResponse
import com.fitness.fitnessCru.utility.Util

class QaWorkoutVibeAdapter(
    var context: Context?
) : RecyclerView.Adapter<QaWorkoutVibeAdapter.ViewHolder>() {

    var workoutVibeData = ArrayList<WorkoutVibeResponse.WhatBringsYouData>()
    lateinit var questionaryInterface: QuestionaryInterface
    private var itemPos: Int = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var workoutVibeIcon: ImageView = itemView.findViewById(R.id.workout_vibe_icon)
        var workoutVibeTitle: TextView = itemView.findViewById(R.id.workout_vibe_name)
        var workoutVibeDescription: TextView = itemView.findViewById(R.id.workout_vibe_description)
        var parent: ConstraintLayout = itemView.findViewById(R.id.what_brings_you_item_container)

    }

    fun setWorkoutVibeData(workoutVibeData: List<WorkoutVibeResponse.WhatBringsYouData>) {
        this.workoutVibeData.addAll(workoutVibeData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QaWorkoutVibeAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.qa_workout_vibe_rv_item, parent, false)
        return ViewHolder(view)
    }

    fun setOnClick(questionaryInterface: QuestionaryInterface) {
        this.questionaryInterface = questionaryInterface
    }

    override fun onBindViewHolder(
        holder: QaWorkoutVibeAdapter.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.parent.setBackgroundResource(if (itemPos == position) R.drawable.round_back_border else R.drawable.round_back_transparent)
        Util.loadImage(context!!, holder.workoutVibeIcon, workoutVibeData[position].image_url)
        holder.workoutVibeTitle.text = workoutVibeData[position].name
        holder.workoutVibeDescription.text = workoutVibeData[position].description
        holder.itemView.setOnClickListener {
            itemPos = position
            notifyDataSetChanged()
            questionaryInterface.onClick(workoutVibeData[position].id)
        }
    }

    override fun getItemCount(): Int {
        return workoutVibeData.size
    }


}
