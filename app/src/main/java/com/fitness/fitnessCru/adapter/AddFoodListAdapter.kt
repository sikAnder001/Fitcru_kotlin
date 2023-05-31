package com.fitness.fitnessCru.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.SelectFoodBinding
import com.fitness.fitnessCru.response.FoodListResposne


class AddFoodListAdapter(
    val pos: Int,
    var SelectedfoodInterface: SelectedfoodInterface
) :
    RecyclerView.Adapter<AddFoodListAdapter.ViewHolder>() {

    var foodListN = ArrayList<FoodListResposne.Data>()


    inner class ViewHolder(addFoodListBinding: SelectFoodBinding) :
        RecyclerView.ViewHolder(addFoodListBinding.root) {
        var addFoodListBinding = addFoodListBinding

        fun bind(data: FoodListResposne.Data, position: Int) {
            with(addFoodListBinding) {
                foodItemName.text = data.name
                mainL.setOnClickListener {
                    SelectedfoodInterface.onClick(pos, data)
                }
            }
        }


    }

    fun sitOnClick(selectedfoodInterface: SelectedfoodInterface) {
        //     this.selectedfoodInterface = selectedfoodInterface
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddFoodListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val addFoodBinding = SelectFoodBinding.inflate(inflater, parent, false)
        return ViewHolder(addFoodBinding)

    }

    fun setFoodList(foodList: ArrayList<FoodListResposne.Data>) {
        this.foodListN = foodList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return try {
            foodListN.size
        } catch (e: Exception) {
            0
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(foodListN[position], position)

}

interface SelectedfoodInterface {
    fun onClick(position: Int, data: FoodListResposne.Data)

}