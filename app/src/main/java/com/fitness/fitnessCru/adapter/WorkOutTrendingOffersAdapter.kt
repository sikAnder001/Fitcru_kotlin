package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.activities.SubscriptionPlanActivity
import com.fitness.fitnessCru.databinding.WorkoutTrandingOfferesItemBinding
import com.fitness.fitnessCru.response.WorkoutProgramResponse
import com.fitness.fitnessCru.utility.Constants

class WorkOutTrendingOffersAdapter(private val context: Context?, val isPaid: Int) :
    RecyclerView.Adapter<WorkOutTrendingOffersAdapter.ViewHolder>() {

    private var list = ArrayList<WorkoutProgramResponse.Data.TrendingOffer>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkOutTrendingOffersAdapter.ViewHolder {
        return ViewHolder(
            WorkoutTrandingOfferesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WorkOutTrendingOffersAdapter.ViewHolder, position: Int) =
        holder.bind(list[position])

    fun setList(list: ArrayList<WorkoutProgramResponse.Data.TrendingOffer>) {

        this.list = list

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    inner class ViewHolder(val binding: WorkoutTrandingOfferesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: WorkoutProgramResponse.Data.TrendingOffer) {
            binding.apply {

                userCategory.text = data.fitness_level?.name

                fitCruEliteTv.text = data.name

                Glide.with(context!!).load(data?.image_url)
                    .placeholder(R.drawable.place_holder).into(posterImg)

                /*sessionsTv.text = "${data?.phases} Sessions"*/

                if (isPaid == 1) {
                    lockImg.visibility = View.GONE
                    getStartedBtn.setOnClickListener {
                        val intent =
                            Intent(context.applicationContext, SetupAllActivity::class.java)
                        intent.putExtra(Constants.DESTINATION, Constants.WORKOUT2)
                        intent.putExtra("id", data.id)
                        context.startActivity(intent)
                    }
                } else {
                    lockImg.visibility = View.VISIBLE
                    getStartedBtn.setOnClickListener {
                        val intent = Intent(context, SubscriptionPlanActivity::class.java)
                        intent.putExtra("num", 3)
                        context!!.startActivity(intent)
                    }
                }

            }
        }
    }

}