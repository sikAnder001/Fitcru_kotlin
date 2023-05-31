package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.databinding.DashCookingTodayBinding
import com.fitness.fitnessCru.response.DashBoardResponse
import com.fitness.fitnessCru.utility.Constants

class WhatCookingTodayAdapter(private val context: Context?) :
    RecyclerView.Adapter<WhatCookingTodayAdapter.ViewHolder>() {

    private var list = ArrayList<DashBoardResponse.Data.CookingToday>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WhatCookingTodayAdapter.ViewHolder {
        return ViewHolder(
            DashCookingTodayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: WhatCookingTodayAdapter.ViewHolder,
        position: Int
    ) = holder.bind(list[position])

    fun setList(list: ArrayList<DashBoardResponse.Data.CookingToday>) {

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

    inner class ViewHolder(val binding: DashCookingTodayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DashBoardResponse.Data.CookingToday) {
            binding.apply {
                tvName.text = data.name
                tvCookingtime.text = "Total Time: ${data.total_time} Min"

                /* tvCookingtime.text = "Cooking Time: 20 Mins"*/
                Glide.with(context!!).load(data.image_url).placeholder(R.drawable.place_holder)
                    .into(imageView)
                itemView.setOnClickListener {
                    val intent = Intent(context.applicationContext, SetupAllActivity::class.java)
                    intent.putExtra(Constants.DESTINATION, Constants.RECIPE)
                    intent.putExtra("id", data.id)
                    context.startActivity(intent)
                }
            }
        }
    }

}
