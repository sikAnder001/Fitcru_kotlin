package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.CoachCategoryRvItemBinding
import com.fitness.fitnessCru.model.CoachCategoryModel

class CoachCategoryAdapter(

    private val coachCategoryModel: ArrayList<CoachCategoryModel>,

    private val context: Context?,

    private val listener: CoachCategoryOnClickListener

) : RecyclerView.Adapter<CoachCategoryAdapter.ViewHolder>() {

    private var itemPos = -1

    inner class ViewHolder(coachCategoryBinding: CoachCategoryRvItemBinding) :
        RecyclerView.ViewHolder(coachCategoryBinding.root) {

        var coachCategoryBinding = coachCategoryBinding

        fun bind(coachCategoryModel: CoachCategoryModel, position: Int) {

            coachCategoryBinding.apply {

                Glide.with(context!!).load(coachCategoryModel.categoryImg)
                    .into(coachCategoryImg)

                coachType.text = coachCategoryModel.type

                coachCategoryBinding.coachCategory.setBackgroundResource(

                    if (itemPos == position)
                        R.drawable.circle_after_selected else
                        R.drawable.circle_background
                )

            }
            itemView.setOnClickListener {

                val position = adapterPosition

                itemPos = position

                notifyDataSetChanged()

                listener.onClick(position)

            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoachCategoryAdapter.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val coachCategoryBinding = CoachCategoryRvItemBinding.inflate(inflater, parent, false)

        return ViewHolder(coachCategoryBinding)
    }

    override fun onBindViewHolder(holder: CoachCategoryAdapter.ViewHolder, position: Int) {
        holder.bind(coachCategoryModel[position], position)
    }

    override fun getItemCount(): Int {
        return coachCategoryModel.size
    }

    interface CoachCategoryOnClickListener {

        fun onClick(position: Int)

    }
}