package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ChipSquareBinding
import com.fitness.fitnessCru.response.AvailableTimeSlotResponse
import com.fitness.fitnessCru.utility.CalendarUtils

class SlotsAdapter(val context: Context?, private val listener: AvailabilityInterface) :
    RecyclerView.Adapter<SlotsAdapter.ViewHolder>() {

    private var list = ArrayList<AvailableTimeSlotResponse.Data.AvailableTimeSlot>()
    private var pos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotsAdapter.ViewHolder {
        return ViewHolder(
            ChipSquareBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SlotsAdapter.ViewHolder, position: Int) =
        holder.bind(list[position], position)

    fun setList(list: ArrayList<AvailableTimeSlotResponse.Data.AvailableTimeSlot>) {

        this.list = list

        notifyDataSetChanged()
    }

    interface AvailabilityInterface {
        fun onClick(slotId: Int)
    }

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    inner class ViewHolder(val binding: ChipSquareBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: AvailableTimeSlotResponse.Data.AvailableTimeSlot, position: Int) {
            binding.apply {
                chipTV.text = CalendarUtils.timeFormatFit(data.slotTime)
                binding.chipCategory.setBackgroundResource(if (pos == position) R.drawable.square_back2 else R.drawable.square_back)
                itemView.setOnClickListener {
                    pos = position
                    listener.onClick(data.coachTimeSlotId)
                    notifyDataSetChanged()
                }
            }
        }
    }
}
