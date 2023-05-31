package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.databinding.CoachInfoRvItemBinding
import com.fitness.fitnessCru.model.CoachInfoModel

class CoachInfoAdapter(
    private val coachInfoModel: ArrayList<CoachInfoModel>,
    private val context: Context?
) : RecyclerView.Adapter<CoachInfoAdapter.ViewHolder>() {
    inner class ViewHolder(coachInfoBinding: CoachInfoRvItemBinding) :
        RecyclerView.ViewHolder(coachInfoBinding.root) {
        var coachInfoBinding = coachInfoBinding

        fun bind(coachInfoModel: CoachInfoModel, position: Int) {
            with(coachInfoBinding) {
                coachInfoTitle.text = coachInfoModel.coachInfoText
                Glide.with(context!!).load(coachInfoModel.coachInfoIcon)
                    .into(coachInfoIcon)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoachInfoAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val coachInfoBinding = CoachInfoRvItemBinding.inflate(inflater, parent, false)
        return ViewHolder(coachInfoBinding)
    }

    override fun onBindViewHolder(holder: CoachInfoAdapter.ViewHolder, position: Int) {
        holder.bind(coachInfoModel[position], position)
    }

    override fun getItemCount(): Int {
        return coachInfoModel.size
    }
}