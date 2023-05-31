package com.fitness.fitnessCru.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.MembershiPlanlistBinding
import com.fitness.fitnessCru.response.MembershipPlanResponse
import java.text.SimpleDateFormat

class MembershipPlanAdapter(val context: Context?) :
    RecyclerView.Adapter<MembershipPlanAdapter.ViewHolder>() {

    private var list = ArrayList<MembershipPlanResponse.MPlans>()

    inner class ViewHolder(val binding: MembershiPlanlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(data: MembershipPlanResponse.MPlans, position: Int) {

            binding.apply {

                Glide.with(context!!).load(data.image_url)
                    .placeholder(R.drawable.place_holder).into(img)

                planName.visibility = View.GONE

                dayLeftTv.text = "${data.days_left} Days Left"

                programNameTv.text = data.name

                val start = changeFormat(data.start_date)
                startDateTv.text = start

                val end = changeFormat(data.end_date)
                endDateTv.text = end
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun changeFormat(join: String?): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateTime = simpleDateFormat.parse(join)
        val simpleDateFormat1 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("dd MMM YYYY")
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val date = simpleDateFormat1.format(dateTime)

        return date
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MembershipPlanAdapter.ViewHolder {
        return ViewHolder(
            MembershiPlanlistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: MembershipPlanAdapter.ViewHolder, position: Int) =
        holder.bind(list[position], position)

    fun setList(list: ArrayList<MembershipPlanResponse.MPlans>) {
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
}
