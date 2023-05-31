package com.fitness.fitnessCru.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.model.YogaModel

class YogaAdapter(
    var yogaModel: ArrayList<YogaModel>, var context: Context?
) :
    RecyclerView.Adapter<YogaAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var thumbnailImage: ImageView = itemView.findViewById(R.id.thumbnail_image)
        var views: TextView = itemView.findViewById(R.id.views)
        val yogaTitle: TextView = itemView.findViewById(R.id.yoga_title)
        val yogaChoose: TextView = itemView.findViewById(R.id.yoga_choose)
        val yogaDuration: TextView = itemView.findViewById(R.id.yoga_duration)
        val yogaCategory: TextView = itemView.findViewById(R.id.yoga_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YogaAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.yoga_rv_item, parent, false)
        return YogaAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: YogaAdapter.ViewHolder, position: Int) {
        Glide.with(context!!)
            .load(R.drawable.place_holder/*yogaModel[position].thumbnailImage*/)
            .placeholder(R.drawable.place_holder)
            .into(holder.thumbnailImage)
        holder.views.text = yogaModel[position].views.toString()
        holder.yogaTitle.text = yogaModel[position].yogaTitle.toString()
        holder.yogaChoose.text = yogaModel[position].yogaChoose.toString()
        holder.yogaDuration.text = yogaModel[position].yogaDuration.toString()
        holder.yogaCategory.text = yogaModel[position].yogaCategory.toString()
        holder.itemView.setOnClickListener {
            Navigation.findNavController(
                context as Activity,
                R.id.nutrition_setup_fragment_container
            ).navigate(R.id.sncProgramFragment)
        }
    }

    override fun getItemCount(): Int {
        return yogaModel.size
    }
}