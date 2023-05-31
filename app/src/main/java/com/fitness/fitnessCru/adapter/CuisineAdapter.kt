package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.CuisineListBinding
import com.fitness.fitnessCru.model.CuisinePojo

class CuisineAdapter(val context: Context?) : RecyclerView.Adapter<CuisineAdapter.ViewHolder>() {

    private var list = ArrayList<CuisinePojo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuisineAdapter.ViewHolder {
        return ViewHolder(
            CuisineListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position])

    fun setList(list: ArrayList<CuisinePojo>) {
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

    inner class ViewHolder(val binding: CuisineListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CuisinePojo) {
            binding.apply {
                try {

                } catch (e: Exception) {

                }
            }
        }
    }

}

