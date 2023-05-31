package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.model.SNCSessionModel

class SNCSessionAdapter(
    var sncSessionModel: ArrayList<SNCSessionModel>, var context: Context?
) :
    RecyclerView.Adapter<SNCSessionAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = itemView.findViewById(R.id.snc_session_icon)
        var planText: TextView = itemView.findViewById(R.id.snc_session_plan_text)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SNCSessionAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.s_n_c_session_rv_item, parent, false)
        return SNCSessionAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SNCSessionAdapter.ViewHolder, position: Int) {
        Glide.with(context!!)
            .load(sncSessionModel[position].icon)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.icon)
        holder.planText.text = sncSessionModel[position].planText.toString()
    }

    override fun getItemCount(): Int {
        return sncSessionModel.size
    }
}