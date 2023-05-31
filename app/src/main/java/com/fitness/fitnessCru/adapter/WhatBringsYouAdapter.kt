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
import com.fitness.fitnessCru.response.WhatBringsYouResponse
import com.fitness.fitnessCru.utility.Util

class WhatBringsYouAdapter(var context: Context?) :
    RecyclerView.Adapter<WhatBringsYouAdapter.ViewHolder>() {

    lateinit var questionaryInterface: QuestionaryInterface
    private var itemPos: Int = -1

    var whatBringsYouModel = ArrayList<WhatBringsYouResponse.WorkoutVibeData>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var brings_icon: ImageView = itemView.findViewById(R.id.brings_icon)
        var brings_name: TextView = itemView.findViewById(R.id.brings_name)
        var brings_description: TextView = itemView.findViewById(R.id.brings_description)
        var parent: ConstraintLayout = itemView.findViewById(R.id.what_brings_you_item_container)

    }

    fun setListData(whatBringsYouModel: List<WhatBringsYouResponse.WorkoutVibeData>) {
        this.whatBringsYouModel.addAll(whatBringsYouModel)
        notifyDataSetChanged()
    }

    fun setOnClick(questionaryInterface: QuestionaryInterface) {
        this.questionaryInterface = questionaryInterface
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WhatBringsYouAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.questionary_four_recy_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: WhatBringsYouAdapter.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.parent.setBackgroundResource(if (itemPos == position) R.drawable.round_back_border else R.drawable.round_back_transparent)
        Util.loadImage(
            context!!,
            holder.brings_icon,
            whatBringsYouModel[position].image_url
        )
        holder.brings_name.text = whatBringsYouModel[position].title.toString()
        holder.brings_description.text = whatBringsYouModel[position].description.toString()
        holder.itemView.setOnClickListener {
            itemPos = position
            notifyDataSetChanged()
//            questionaryInterface.onClick(whatBringsYouModel[position].id)
            questionaryInterface.onClick(1)
        }
    }

    override fun getItemCount(): Int {
        return try {
            whatBringsYouModel.size
        } catch (e: Exception) {
            0
        }
    }
}

