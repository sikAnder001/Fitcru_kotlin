package com.fitness.fitnessCru.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.PlanDetailedPageChildRvItemBinding
import com.fitness.fitnessCru.model.PlanDetailedPageModel

class PlanDetailedPageChildAdapter(
    private val planDetailedChildModel: ArrayList<PlanDetailedPageModel.PlanDetailedChildItemModel>
) : RecyclerView.Adapter<PlanDetailedPageChildAdapter.ViewHolder>() {
    inner class ViewHolder(planDetailedChildBinding: PlanDetailedPageChildRvItemBinding) :
        RecyclerView.ViewHolder(planDetailedChildBinding.root) {
        var planDetailedChildBinding = planDetailedChildBinding
        fun bind(
            planDetailedChildModel: PlanDetailedPageModel.PlanDetailedChildItemModel,
            position: Int
        ) {
            planDetailedChildBinding.apply {
                textTv.text = planDetailedChildModel.text
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlanDetailedPageChildAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val planDetailedChildBinding =
            PlanDetailedPageChildRvItemBinding.inflate(inflater, parent, false)
        return ViewHolder(planDetailedChildBinding)
    }

    override fun onBindViewHolder(holder: PlanDetailedPageChildAdapter.ViewHolder, position: Int) {
        holder.bind(planDetailedChildModel[position], position)
    }

    override fun getItemCount(): Int {
        return planDetailedChildModel.size
    }
}