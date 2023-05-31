package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.databinding.PlanDetailedHowToMakeRvItemBinding
import com.fitness.fitnessCru.model.PlanDetailedHowToMakeRVModel

class PlanDetailedHowToMakeRVAdapter(
    private val planDetailedHowToMakeModel: ArrayList<PlanDetailedHowToMakeRVModel>,
    private val context: Context?
) : RecyclerView.Adapter<PlanDetailedHowToMakeRVAdapter.ViewHolder>() {
    inner class ViewHolder(planDetailedHowToMakeBinding: PlanDetailedHowToMakeRvItemBinding) :
        RecyclerView.ViewHolder(planDetailedHowToMakeBinding.root) {
        var planDetailedHowToMakeBinding = planDetailedHowToMakeBinding
        fun bind(planDetailedHowToMakeModel: PlanDetailedHowToMakeRVModel, position: Int) {
            with(planDetailedHowToMakeBinding) {
                planText.text = planDetailedHowToMakeModel.text
                Glide.with(context!!).load(planDetailedHowToMakeModel.icon)
                    .into(circleIcon)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlanDetailedHowToMakeRVAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val planDetailedHowToMakeBinding =
            PlanDetailedHowToMakeRvItemBinding.inflate(inflater, parent, false)
        return ViewHolder(planDetailedHowToMakeBinding)
    }

    override fun onBindViewHolder(
        holder: PlanDetailedHowToMakeRVAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(planDetailedHowToMakeModel[position], position)
    }

    override fun getItemCount(): Int {
        return planDetailedHowToMakeModel.size
    }
}