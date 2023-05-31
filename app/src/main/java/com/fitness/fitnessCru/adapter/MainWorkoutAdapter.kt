package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.MainworkoutListBinding
import com.fitness.fitnessCru.model.MainWorkoutPojo

class MainWorkoutAdapter(val context: Context?, val list: ArrayList<MainWorkoutPojo>) :
    RecyclerView.Adapter<MainWorkoutAdapter.ViewHolder>() {

    class ViewHolder(val binding: MainworkoutListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mainWorkoutPojo: MainWorkoutPojo) {
            with(binding) {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainworkoutListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int {
        return list.size
    }
}
