package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.CoachSlabRvItemBinding
import com.fitness.fitnessCru.interfaces.CoachClickInterface
import com.fitness.fitnessCru.model.CoachSlabResponse

class CoachSlabAdapter(
    val context: Context?,
    val interfaces: CoachClickInterface
) : RecyclerView.Adapter<CoachSlabAdapter.ViewHolder>() {

    private var list = listOf<CoachSlabResponse.Data>()
    private var itemPos = 3

    inner class ViewHolder(coachSlabBinding: CoachSlabRvItemBinding) :
        RecyclerView.ViewHolder(coachSlabBinding.root) {

        var coachSlabBinding = coachSlabBinding

        fun bind(data: CoachSlabResponse.Data, position: Int) {

            coachSlabBinding.apply {

                coachSlabType.text = data.coach_type

                coachSlabBinding.coachSlab.setBackgroundResource(
                    if (itemPos == position) {
                        interfaces.onCoachClick(data.id, data.coach_type)
                        R.drawable.circle_after_selected
                    } else
                        R.drawable.circle_background

                )
            }

            itemView.setOnClickListener {

                itemPos = position

                notifyDataSetChanged()

            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoachSlabAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val coachSlabBinding = CoachSlabRvItemBinding.inflate(inflater, parent, false)

        return ViewHolder(coachSlabBinding)
    }

    override fun onBindViewHolder(holder: CoachSlabAdapter.ViewHolder, position: Int) =
        holder.bind(list[position], position)


    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    fun setList(list: List<CoachSlabResponse.Data>) {
        this.list = list
        notifyDataSetChanged()
    }
}