package com.fitness.fitnessCru.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.CartItemListBinding
import com.fitness.fitnessCru.response.WorkoutProgramResponse


class CartItemAdapter :
    RecyclerView.Adapter<CartItemAdapter.ViewHolder>() {

    private var list = ArrayList<WorkoutProgramResponse.Data.GoalsWithProgram>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartItemAdapter.ViewHolder {
        return ViewHolder(
            CartItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CartItemAdapter.ViewHolder,
        position: Int
    ) {
    }// = holder.bind(list[position], position)

    fun setList(list: ArrayList<WorkoutProgramResponse.Data.GoalsWithProgram>) {

        this.list = list

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return try {
            2 //list.size
        } catch (e: Exception) {
            0
        }
    }

    inner class ViewHolder(val binding: CartItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: WorkoutProgramResponse.Data.GoalsWithProgram, pos: Int) {
            binding.apply {
            }
        }
    }

}