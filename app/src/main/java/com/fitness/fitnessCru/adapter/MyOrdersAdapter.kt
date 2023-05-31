package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.databinding.SearchFoodRvItemBinding
import com.fitness.fitnessCru.model.MyOrdersModel

class MyOrdersAdapter(

    private val myOrdersModel: ArrayList<MyOrdersModel>,

    private val context: Context?

) : RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>() {

    inner class ViewHolder(myOrdersBinding: SearchFoodRvItemBinding) :
        RecyclerView.ViewHolder(myOrdersBinding.root) {

        var myOrdersBinding = myOrdersBinding

        fun bind(myOrdersModel: MyOrdersModel, position: Int) {

            myOrdersBinding.apply {

                Glide.with(context!!).load(myOrdersModel.foodImg).into(foodImage)

                foodTitle.text = myOrdersModel.foodTitle

                foodPrice.text = myOrdersModel.foodPrice

                foodCalTv.text = myOrdersModel.foodCal

                foodProteinTv.text = myOrdersModel.foodProtein

                foodDateNTimeTv.text = myOrdersModel.foodDateNTime

                reorderBtn.setOnClickListener {
                    Toast.makeText(context, "Clicked BTN", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersAdapter.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val myOrdersBinding = SearchFoodRvItemBinding.inflate(inflater, parent, false)

        return ViewHolder(myOrdersBinding)
    }

    override fun onBindViewHolder(holder: MyOrdersAdapter.ViewHolder, position: Int) {
        holder.bind(myOrdersModel[position], position)
    }

    override fun getItemCount(): Int {
        return myOrdersModel.size
    }
}