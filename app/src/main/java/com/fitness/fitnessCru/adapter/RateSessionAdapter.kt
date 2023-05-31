package com.fitness.fitnessCru.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.RateSessionRvItemBinding
import com.fitness.fitnessCru.interfaces.RateInterface
import com.fitness.fitnessCru.model.RateSessionModel

class RateSessionAdapter(
    private val rateSessionModel: ArrayList<RateSessionModel>,
    context: Context?,

    private val rateInterface: RateInterface
) : RecyclerView.Adapter<RateSessionAdapter.ViewHolder>() {
    private var itemPos = -1

    inner class ViewHolder(rateSessionBinding: RateSessionRvItemBinding) :
        RecyclerView.ViewHolder(rateSessionBinding.root) {
        var rateSessionBinding = rateSessionBinding

        fun bind(rateSessionModel: RateSessionModel, position: Int) {
            with(rateSessionBinding) {
                rateSessionTv.text = rateSessionModel.rateSessionText
                if (itemPos == position) rateSessionBinding.rateSessionTv.setTextColor(Color.BLACK) else
                    rateSessionBinding.rateSessionTv.setTextColor(Color.WHITE)
                rateSessionBinding.parent.setBackgroundResource(
                    if (itemPos == position)
                        R.drawable.background_for_rate_session_rv_item_after_select else
                        R.drawable.background_for_rate_session_rv_item
                )

                itemView.setOnClickListener {
                    itemPos = position
                    rateInterface.onRateClick(
                        rateSessionModel.rateSessionText ?: ""
                    )
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RateSessionAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rateSessionBinding = RateSessionRvItemBinding.inflate(inflater, parent, false)
        return ViewHolder(rateSessionBinding)
    }

    override fun onBindViewHolder(holder: RateSessionAdapter.ViewHolder, position: Int) {
        holder.bind(rateSessionModel[position], position)
    }

    override fun getItemCount(): Int {
        return rateSessionModel.size
    }


}