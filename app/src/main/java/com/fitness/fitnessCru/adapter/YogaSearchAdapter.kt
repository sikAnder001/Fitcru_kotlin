package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.model.YogaSearchChildItem
import com.fitness.fitnessCru.model.YogaSearchModel

class YogaSearchAdapter(
    var yogaSearchModel: ArrayList<YogaSearchModel>, var context: Context?
) :
    RecyclerView.Adapter<YogaSearchAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var yogaSearchTitle: TextView = itemView.findViewById(R.id.yoga_search_title)
        var yogaSearchChildRV: RecyclerView = itemView.findViewById(R.id.child_yoga_search_rv)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): YogaSearchAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.yoga_search_rv_item, parent, false)
        return YogaSearchAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: YogaSearchAdapter.ViewHolder, position: Int) {
        holder.yogaSearchTitle!!.text = yogaSearchModel[position].yogaSearchTitle
        setYogaSearchChildItemRV(
            holder.yogaSearchChildRV,
            yogaSearchModel[position].yogaSearchChildItem
        )
    }

    override fun getItemCount(): Int {
        return yogaSearchModel.size
    }

    private fun setYogaSearchChildItemRV(
        recyclerView: RecyclerView,
        yogaSearchChildItem: ArrayList<YogaSearchChildItem>
    ) {
        val itemRecyclerAdapter = YogaSearchChildAdapter(yogaSearchChildItem, context)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = itemRecyclerAdapter

    }
}