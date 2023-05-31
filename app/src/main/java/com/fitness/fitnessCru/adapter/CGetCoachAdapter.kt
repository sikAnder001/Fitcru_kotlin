package com.fitness.fitnessCru.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.CoachGetCoachRvItemBinding
import com.fitness.fitnessCru.interfaces.CoachPlanClickInterface
import com.fitness.fitnessCru.model.CGetCoachModel


class CGetCoachAdapter(
    private val interfaces: CoachPlanClickInterface
) : RecyclerView.Adapter<CGetCoachAdapter.ViewHolder>() {

    private var list: List<CGetCoachModel> = ArrayList()

    inner class ViewHolder(getCoachBinding: CoachGetCoachRvItemBinding) :
        RecyclerView.ViewHolder(getCoachBinding.root) {
        var getCoachBinding = getCoachBinding

        fun bind(getCoachModel: CGetCoachModel, position: Int) {
            with(getCoachBinding) {
                actualPrice.text = "₹${getCoachModel.price} / ${getCoachModel.duration}"
                priceTv.text = "₹${getCoachModel.currentPrice} / ${getCoachModel.duration}"
                actualPrice.setPaintFlags(actualPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                root.setOnClickListener {
                    interfaces.onCoachClick(
                        getCoachModel.key,
                        getCoachModel.currentPrice ?: "",
                        getCoachModel.coach_category_id,
                        getCoachModel.feeId,
                        getCoachModel.slab_type.toString(),
                        getCoachModel.duration
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CGetCoachAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val getCoachBinding = CoachGetCoachRvItemBinding.inflate(inflater, parent, false)
        return ViewHolder(getCoachBinding)
    }

    fun setList(lists: List<CGetCoachModel>) {
        list = lists
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CGetCoachAdapter.ViewHolder, position: Int) =
        holder.bind(list[position], position)


    override fun getItemCount(): Int {
        return list.size
    }
}