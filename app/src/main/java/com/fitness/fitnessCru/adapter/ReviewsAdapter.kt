package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.ReviewListBinding
import com.fitness.fitnessCru.response.GetCoachDetailsResponse

class ReviewsAdapter(val context: Context?) : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

    private var list: List<GetCoachDetailsResponse.CoachReviews> = ArrayList()

    inner class ViewHolder(reviewListBinding: ReviewListBinding) :
        RecyclerView.ViewHolder(reviewListBinding.root) {

        var reviewListBinding = reviewListBinding

        fun bind(data: GetCoachDetailsResponse.CoachReviews) {

            reviewListBinding.apply {

                //TODO

            }

        }

    }

    fun setList(lists: List<GetCoachDetailsResponse.CoachReviews>) {
        list = lists
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsAdapter.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val reviewListBinding = ReviewListBinding.inflate(inflater, parent, false)

        return ViewHolder(reviewListBinding)
    }

    override fun onBindViewHolder(holder: ReviewsAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


}
