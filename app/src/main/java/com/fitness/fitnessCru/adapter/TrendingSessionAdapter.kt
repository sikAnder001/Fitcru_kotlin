package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.activities.SubscriptionPlanActivity
import com.fitness.fitnessCru.databinding.TrendingSessionListBinding
import com.fitness.fitnessCru.response.DashBoardResponse
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.Session

class TrendingSessionAdapter(val context: Context?, val isPaid: Int) :
    RecyclerView.Adapter<TrendingSessionAdapter.ViewHolder>() {

    private var list = listOf<DashBoardResponse.Data.TrendingSession>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrendingSessionAdapter.ViewHolder {
        return ViewHolder(
            TrendingSessionListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrendingSessionAdapter.ViewHolder, position: Int) =
        holder.bind(list[position])

    fun setList(list: List<DashBoardResponse.Data.TrendingSession>) {
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

        fun bind(data: DashBoardResponse.Data.TrendingSession) {
            binding.apply {
                /* tvViews.text = "${data.total_view} Views"*/
                tvSessionCount.text = "${data.total_session} session"
                tvTitle.text = data.name
                tvDuration.text = data.total_time
                tvIntensity.text = data.fitness_level?.name
                tvCalories.text = "${data.total_kcal} Cal"
                Glide.with(context!!).load(data.image_url).placeholder(R.drawable.place_holder)
                    .into(imgView)
            }

            Log.v("ispaduserdetail", userDetail.ispaid.toString())
            itemView.setOnClickListener {
                Log.v("ispaidkey", isPaid.toString())
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
                        intent.putExtra("program_id", -1)
                        context!!.startActivity(intent)

                    }

                } else {
                    val intent =
                        Intent(context!!.applicationContext, SetupAllActivity::class.java)
                    intent.putExtra(Constants.DESTINATION, Constants.WORKOUT2)
                    intent.putExtra("id", data.id)
                    intent.putExtra("program_id", -1)
                    context.startActivity(intent)

                }
            }
//            itemView.setOnClickListener {
//                val intent = Intent(context, ElitePlanActivity::class.java)
//                intent.putExtra("id", data.id)
//                intent.putExtra("name", data.name)
//                context!!.startActivity(intent)
//            }
        }
    }
}
