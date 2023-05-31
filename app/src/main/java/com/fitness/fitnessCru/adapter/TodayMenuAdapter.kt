package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.TodayMenuListBinding
import com.fitness.fitnessCru.model.TodayMenuPojo

class TodayMenuAdapter(val context: Context?) :
    RecyclerView.Adapter<TodayMenuAdapter.ViewHolder>() {

    private var list = ArrayList<TodayMenuPojo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayMenuAdapter.ViewHolder {
        return ViewHolder(
            TodayMenuListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position])

    fun setList(list: ArrayList<TodayMenuPojo>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    inner class ViewHolder(val binding: TodayMenuListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TodayMenuPojo) {
            binding.apply {
                try {

                } catch (e: Exception) {

                }
            }
        }
    }

}

