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
import com.fitness.fitnessCru.model.YogaSearchChildItem

class YogaSearchChildAdapter(
    var yogaSearchChildItemModel: ArrayList<YogaSearchChildItem>, var context: Context?
) :
    RecyclerView.Adapter<YogaSearchChildAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnailImage: ImageView = itemView.findViewById(R.id.thumbnail_image)
        var views: TextView = itemView.findViewById(R.id.views)
        val yogaTitle: TextView = itemView.findViewById(R.id.yoga_title)
        val yogaChoose: TextView = itemView.findViewById(R.id.yoga_choose)
        val yogaDuration: TextView = itemView.findViewById(R.id.yoga_duration)
        val yogaCategory: TextView = itemView.findViewById(R.id.yoga_category)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): YogaSearchChildAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.yoga_search_child_rv_item, parent, false)
        return YogaSearchChildAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: YogaSearchChildAdapter.ViewHolder, position: Int) {
        Glide.with(context!!)
            .load(yogaSearchChildItemModel[position].thumbnailImage)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.thumbnailImage)
        holder.views.text = yogaSearchChildItemModel[position].views.toString()
        holder.yogaTitle.text = yogaSearchChildItemModel[position].yogaTitle.toString()
        holder.yogaChoose.text = yogaSearchChildItemModel[position].yogaChoose.toString()
        holder.yogaDuration.text = yogaSearchChildItemModel[position].yogaDuration.toString()
        holder.yogaCategory.text = yogaSearchChildItemModel[position].yogaCategory.toString()
    }

    override fun getItemCount(): Int {
        return yogaSearchChildItemModel.size
    }

}