package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.MealTypeRvItemBinding
import com.fitness.fitnessCru.interfaces.FragInterface
import com.fitness.fitnessCru.response.MealTypeResponse

class MealTypeAdapter(val context: Context?) : RecyclerView.Adapter<MealTypeAdapter.ViewHolder>() {

    lateinit var fragInterface: FragInterface
    private var list = ArrayList<MealTypeResponse.Data>()
    private var itemPos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealTypeAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val mealTypeRVBinding = MealTypeRvItemBinding.inflate(inflater, parent, false)
        return ViewHolder(mealTypeRVBinding)
    }

    override fun onBindViewHolder(holder: MealTypeAdapter.ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return try {
            return list.size
        } catch (e: Exception) {
            0
        }
    }

    fun setList(list: ArrayList<MealTypeResponse.Data>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setOnClick(fragInterface: FragInterface) {
        this.fragInterface = fragInterface
    }

    inner class ViewHolder(mealTypeRvBinding: MealTypeRvItemBinding) :
        RecyclerView.ViewHolder(mealTypeRvBinding.root) {
        var mealTypeRVBinding = mealTypeRvBinding

        fun bind(mealTypeModel: MealTypeResponse.Data, position: Int) {
            with(mealTypeRVBinding) {
                mealTypeTitle.text = mealTypeModel.mealtype
                mealTypeItemContainer.setBackgroundResource(if (itemPos == position) R.drawable.straight_back_border else R.drawable.staright_back_transparent)
                itemView.setOnClickListener {
                    itemPos = position
                    fragInterface.onClick(mealTypeModel.id, mealTypeModel.mealtype)
                    notifyDataSetChanged()
                }
            }
        }
    }
}