package com.fitness.fitnessCru.adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.response.WorkoutByFitnessResponse

class WorkoutByFitnessAdapter(
    var context: Context?
) :
    RecyclerView.Adapter<WorkoutByFitnessAdapter.ViewHolder>() {
    var listData = ArrayList<WorkoutByFitnessResponse.WorkoutByFitnessData>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var thumbnailImage: ImageView = itemView.findViewById(R.id.thumbnail_image)
        val description: TextView = itemView.findViewById(R.id.description)
        val title: TextView = itemView.findViewById(R.id.title)
        val cardioCategory: TextView = itemView.findViewById(R.id.cardio_category)
    }

    fun setList(listData: List<WorkoutByFitnessResponse.WorkoutByFitnessData>) {
        this.listData =
            listData as ArrayList<WorkoutByFitnessResponse.WorkoutByFitnessData>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutByFitnessAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardio_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutByFitnessAdapter.ViewHolder, position: Int) {
        Glide.with(context!!)
            .load(listData[position].banner)
            .placeholder(R.drawable.place_holder)
            .into(holder.thumbnailImage)
        holder.description.text = listData[position].description
        holder.title.text = listData[position].session_name
        holder.cardioCategory.text = "${listData[position].fitness_level[0].name}"
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("workout_id", listData!![position].id!!)
            bundle.putInt("program_id", -1)
            Navigation.findNavController(
                context as Activity,
                R.id.nutrition_setup_fragment_container
            ).navigate(R.id.sncSessionFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

}