package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.databinding.CoachHowItWorksBinding
import com.fitness.fitnessCru.model.CoachHowItWorksModel

class CoachHowItWorksAdapter(
    private val howItWorksModel: ArrayList<CoachHowItWorksModel>,
    private val context: Context?
) : RecyclerView.Adapter<CoachHowItWorksAdapter.ViewHolder>() {
    inner class ViewHolder(howItWorksBinding: CoachHowItWorksBinding) :
        RecyclerView.ViewHolder(howItWorksBinding.root) {
        var howItWorksBinding = howItWorksBinding

        fun bind(howItWorksModel: CoachHowItWorksModel, position: Int) {
            with(howItWorksBinding) {
                detailTV.text = howItWorksModel.details
                Glide.with(context!!).load(howItWorksModel.image)
                    .into(image)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoachHowItWorksAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val howItWorksBinding = CoachHowItWorksBinding.inflate(inflater, parent, false)
        return ViewHolder(howItWorksBinding)
    }

    override fun onBindViewHolder(holder: CoachHowItWorksAdapter.ViewHolder, position: Int) {
        holder.bind(howItWorksModel[position], position)
    }

    override fun getItemCount(): Int {
        return howItWorksModel.size
    }
}