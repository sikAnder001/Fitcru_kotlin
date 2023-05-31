package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.databinding.CoachDashboardRvItemBinding
import com.fitness.fitnessCru.response.DashBoardResponse
import com.fitness.fitnessCru.utility.Util

class CoachDashboardAdapter(

    private val context: Context?,

    ) : RecyclerView.Adapter<CoachDashboardAdapter.ViewHolder>() {

    private var list: List<DashBoardResponse.Data.YourCoach> = ArrayList()

    inner class ViewHolder(coachDashboardBinding: CoachDashboardRvItemBinding) :
        RecyclerView.ViewHolder(coachDashboardBinding.root) {

        var coachDashboardBinding = coachDashboardBinding

        fun bind(coachModel: DashBoardResponse.Data.YourCoach, position: Int) {

            coachDashboardBinding.apply {

                Glide.with(context!!).load(coachModel.image_url)
                    .into(coachImage)

                coachNameTv.text = coachModel.coach_name

                coachDetailTv.text = coachModel.coach_biodata

            }

            itemView.setOnClickListener {
                Util.toast(context!!, "Coach")
            }
        }
    }

    fun setList(lists: List<DashBoardResponse.Data.YourCoach>) {
        list = lists
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoachDashboardAdapter.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val coachDashboardBinding = CoachDashboardRvItemBinding.inflate(inflater, parent, false)

        return ViewHolder(coachDashboardBinding)
    }

    override fun onBindViewHolder(holder: CoachDashboardAdapter.ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

}