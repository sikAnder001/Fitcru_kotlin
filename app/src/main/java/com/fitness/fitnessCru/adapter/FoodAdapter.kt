package com.fitness.fitnessCru.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.FoodListItemBinding
import com.fitness.fitnessCru.databinding.FoodbottomsheetBinding
import com.fitness.fitnessCru.model.IndianFoodPojo
import com.google.android.material.bottomsheet.BottomSheetDialog

class FoodAdapter(val activity: Activity, val context: Context?) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    private var list = ArrayList<IndianFoodPojo>()

    inner class ViewHolder(val binding: FoodListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: IndianFoodPojo) {
            binding.apply {

                val foodDetailBinding = FoodbottomsheetBinding.inflate(activity.layoutInflater)

                var bottomSheetDialog = BottomSheetDialog(context!!)

                bottomSheetDialog.setContentView(foodDetailBinding.root)

                tvAdd.setOnClickListener { bottomSheetDialog.show() }

                foodDetailBinding.crossBTN.setOnClickListener { bottomSheetDialog.hide() }

                foodDetailBinding.tvAddFood.setOnClickListener { bottomSheetDialog.hide() }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FoodListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodAdapter.ViewHolder, position: Int) =
        holder.bind(list[position])

    fun setList(list: ArrayList<IndianFoodPojo>) {
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
}
