package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.databinding.HowtomakelistBinding
import com.fitness.fitnessCru.model.PlanDetailedHowToMakeRVModel


class HowtoMake(
    private val howToMakeModel: ArrayList<PlanDetailedHowToMakeRVModel>,
    private val context: Context?
) :
    RecyclerView.Adapter<HowtoMake.ViewHolder>() {

    inner class ViewHolder(howToMakeBinding: HowtomakelistBinding) :

        RecyclerView.ViewHolder(howToMakeBinding.root) {

        var howToMakeBinding = howToMakeBinding

        fun bind(howToMakeModel: PlanDetailedHowToMakeRVModel, position: Int) {

            with(howToMakeBinding) {

                planText.text = howToMakeModel.text

                Glide.with(context!!).load(howToMakeModel.icon)
                    .into(circleIcon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HowtoMake.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val howToMakeBinding =
            HowtomakelistBinding.inflate(inflater, parent, false)

        return ViewHolder(howToMakeBinding)

    }

    override fun onBindViewHolder(holder: HowtoMake.ViewHolder, position: Int) {
        holder.bind(howToMakeModel[position], position)
    }

    override fun getItemCount(): Int {
        return howToMakeModel.size
    }

}
