package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.model.SubPlanChildModel

class SubPlanChildAdapter2(var childModel: List<SubPlanChildModel>, var context: Context) :
    RecyclerView.Adapter<SubPlanChildAdapter2.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.sub_plan_child_rec_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.circularImg.setImageResource(childModel[position].image)
        holder.planText.text = childModel[position].planText
    }

    override fun getItemCount(): Int {
        return childModel.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val circularImg: ImageView
        val planText: TextView

        init {
            circularImg = itemView.findViewById(R.id.circle_icon)
            planText = itemView.findViewById(R.id.plan_text)
        }
    }
}