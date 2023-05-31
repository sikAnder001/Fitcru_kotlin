package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.model.StudioFilterModel

class StudioFilterChildAdapter(
    var filterChildItems: ArrayList<StudioFilterModel.StudioFilterChildItem>, var context: Context?
) :
    RecyclerView.Adapter<StudioFilterChildAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filterTypes: TextView = itemView.findViewById(R.id.filter_types_tv)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudioFilterChildAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.studio_filter_child_rv_item, parent, false)
        return StudioFilterChildAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudioFilterChildAdapter.ViewHolder, position: Int) {

        holder.filterTypes.text = filterChildItems[position].filterTypes.toString()
    }

    override fun getItemCount(): Int {
        return filterChildItems.size
    }

}