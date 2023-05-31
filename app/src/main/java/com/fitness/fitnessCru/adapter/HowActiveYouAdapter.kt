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
import com.fitness.fitnessCru.response.HowActiveYouResponse
import com.fitness.fitnessCru.utility.Util

class HowActiveYouAdapter(var context: Context?) :
    RecyclerView.Adapter<HowActiveYouAdapter.ViewHolder>() {
    lateinit var questionaryInterface: QuestionaryInterface
    var howActiveYou = ArrayList<HowActiveYouResponse.HowActiveYouData>()
    private var itemPos: Int = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var how_active_icon: ImageView = itemView.findViewById(R.id.how_active_icon)
        var how_active_name: TextView = itemView.findViewById(R.id.how_active_name)
        var how_active_description: TextView = itemView.findViewById(R.id.how_active_description)
        var parent: ConstraintLayout = itemView.findViewById(R.id.what_brings_you_item_container)

    }

    fun setListData(howActiveYou: List<HowActiveYouResponse.HowActiveYouData>) {
        this.howActiveYou.addAll(howActiveYou)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HowActiveYouAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.how_active_rec_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.parent.setBackgroundResource(if (itemPos == position) R.drawable.round_back_border else R.drawable.round_back_transparent)
        Util.loadImage(
            context!!,
            holder.how_active_icon,
            howActiveYou[position].image_url
        )
        holder.how_active_name.text = howActiveYou[position].name.toString()
        holder.how_active_description.text = howActiveYou[position].detail.toString()
        holder.itemView.setOnClickListener {
            itemPos = position
            notifyDataSetChanged()
            questionaryInterface.onClick(howActiveYou[position].id)
        }
    }

    fun setOnClick(questionaryInterface: QuestionaryInterface) {
        this.questionaryInterface = questionaryInterface
    }

    override fun getItemCount(): Int {
        return try {
            howActiveYou.size
        } catch (e: Exception) {
            0
        }
    }
}

