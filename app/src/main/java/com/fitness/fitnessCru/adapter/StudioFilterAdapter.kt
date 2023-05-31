package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.model.StudioFilterModel

class StudioFilterAdapter(
    var studioFilterModel: ArrayList<StudioFilterModel>, var context: Context?
) :
    RecyclerView.Adapter<StudioFilterAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var filterTitle: TextView = itemView.findViewById(R.id.filter_title)
        var filterChildRV: RecyclerView = itemView.findViewById(R.id.studio_filter_child_rv)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudioFilterAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.studio_filter_main_rv_item, parent, false)
        return StudioFilterAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudioFilterAdapter.ViewHolder, position: Int) {
        holder.filterTitle!!.text = studioFilterModel[position].filterTitle
        setStudioFilterChildItemRV(
            holder.filterChildRV,
            studioFilterModel[position].filterChildItems
        )
    }

    override fun getItemCount(): Int {
        return studioFilterModel.size
    }

    private fun setStudioFilterChildItemRV(
        recyclerView: RecyclerView,
        filterChildItems: ArrayList<StudioFilterModel.StudioFilterChildItem>
    ) {
        val itemRecyclerAdapter = StudioFilterChildAdapter(filterChildItems, context)
        recyclerView.layoutManager =
            GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = itemRecyclerAdapter

    }
}