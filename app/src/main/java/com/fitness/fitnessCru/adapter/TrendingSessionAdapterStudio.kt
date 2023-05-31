package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.activities.SubscriptionPlanActivity
import com.fitness.fitnessCru.databinding.TrendingSessionListBinding
import com.fitness.fitnessCru.response.StudioResponse
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.Session

class TrendingSessionAdapterStudio(val context: Context?, val isPaid: Int) :
    RecyclerView.Adapter<TrendingSessionAdapterStudio.ViewHolder>() {

    private var list = listOf<StudioResponse.TrendingSeasons>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrendingSessionAdapterStudio.ViewHolder {
        return ViewHolder(
            TrendingSessionListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrendingSessionAdapterStudio.ViewHolder, position: Int) =
        holder.bind(list[position], position)

    fun setList(list: List<StudioResponse.TrendingSeasons>) {
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

    inner class ViewHolder(val binding: TrendingSessionListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val userDetail = Session.getUserDetails()

        fun bind(data: StudioResponse.TrendingSeasons, position: Int) {
            binding.apply {
                tvSessionCount.text = ("${data.total_session} Sessions")

                tvTitle.text = ("${data.name}")

                tvDuration.text = ("${data.total_time}")

                tvCalories.text = ("${data.total_kcal} Cal")

                tvIntensity.text = data.fitness_level?.name

                /* tvViews.text = ("${data.view} Views")*/

                Glide.with(context!!).load(data.image_url)
                    .placeholder(R.drawable.place_holder)
                    .into(imgView)
//                itemView.setOnClickListener {
                /*  val intent = Intent(context, ElitePlanActivity::class.java)
                  intent.putExtra("id", data.id)
                  intent.putExtra("name", data.name)
                  context!!.startActivity(intent)*/
                itemView.setOnClickListener {
                    if (userDetail.ispaid == 0) {
                        if (data.price != null) {
                            val intent = Intent(context, SubscriptionPlanActivity::class.java)
                            intent.putExtra("num", 3)
                            context!!.startActivity(intent)
                        } else {
                            val intent =
                                Intent(context, SetupAllActivity::class.java)
                            intent.putExtra(Constants.DESTINATION, Constants.WORKOUT2)
                            intent.putExtra("id", data.id)
                            intent.putExtra("program_id", -2)
                            context!!.startActivity(intent)
                        }
                    } else {
                        val intent =
                            Intent(context!!.applicationContext, SetupAllActivity::class.java)
                        intent.putExtra(Constants.DESTINATION, Constants.WORKOUT2)
                        intent.putExtra("id", data.id)
                        intent.putExtra("program_id", -2)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}
