package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.CoachSpecializationBinding
import com.fitness.fitnessCru.interfaces.SpecializationSelection
import com.fitness.fitnessCru.response.GetCoachSpecializationsResponse

class CoachSpecializationsAdapter(private val context: Context?) :
    RecyclerView.Adapter<CoachSpecializationsAdapter.ViewHolder>() {

    private var list: List<GetCoachSpecializationsResponse.SpecializationsData> = ArrayList()
    private var click: SpecializationSelection? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CoachSpecializationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    fun setList(lists: List<GetCoachSpecializationsResponse.SpecializationsData>) {
        list = lists
        notifyDataSetChanged()
    }

    fun setOnClick(click: SpecializationSelection) {
        this.click = click
    }

    inner class ViewHolder(val binding: CoachSpecializationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(getCoachSpecializations: GetCoachSpecializationsResponse.SpecializationsData) {
            with(binding) {
                Glide.with(context!!).load(getCoachSpecializations.image_url)
                    .placeholder(R.drawable.place_holder)
                    .into(imgIMG)
                coachname.text = getCoachSpecializations.name
            }
            binding.root.setOnClickListener { click?.onClick(getCoachSpecializations.id) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position])


}