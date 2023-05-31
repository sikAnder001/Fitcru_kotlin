package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.TCoachSpecializationRvItemBinding
import com.fitness.fitnessCru.response.GetCoachDetailsResponse

class TCoachSpecializationAdapter(val context: Context?) :
    RecyclerView.Adapter<TCoachSpecializationAdapter.ViewHolder>() {

    private var list: List<GetCoachDetailsResponse.CoachSpecializations> = ArrayList()

    inner class ViewHolder(coachSpecializationsBinding: TCoachSpecializationRvItemBinding) :
        RecyclerView.ViewHolder(coachSpecializationsBinding.root) {

        var coachSpecializationsBinding = coachSpecializationsBinding

        fun bind(data: GetCoachDetailsResponse.CoachSpecializations) {

            coachSpecializationsBinding.apply {

                Glide.with(context!!).load(data.image_url)
                    .placeholder(R.drawable.place_holder)
                    .into(coachSpecializationsBinding.specializationsImg)

                specializationsTitle.text = data.name
            }
        }

    }

    fun setList(lists: List<GetCoachDetailsResponse.CoachSpecializations>) {
        list = lists
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TCoachSpecializationAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val coachSpecializationsBinding =
            TCoachSpecializationRvItemBinding.inflate(inflater, parent, false)

        return ViewHolder(coachSpecializationsBinding)
    }

    override fun onBindViewHolder(holder: TCoachSpecializationAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}