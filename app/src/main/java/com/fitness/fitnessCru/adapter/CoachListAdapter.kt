package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.ChatActivity
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.databinding.CoachListBinding
import com.fitness.fitnessCru.response.CoachListResponse
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.Session


class CoachListAdapter(
    val context: Context?,
    val listner: CoachListInterface
) :
    RecyclerView.Adapter<CoachListAdapter.ViewHolder>() {

    private var list = listOf<CoachListResponse.Datum>()
    var date: String = ""

    inner class ViewHolder(val binding: CoachListBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: CoachListResponse.Datum) {
            binding.apply {
                Glide.with(context!!)
                    .load(data.image_url)
                    .placeholder(R.drawable.place_holder)
                    .into(imageView)

                if (data.location != null && data.location.isNotEmpty()) {
                    locationView.visibility = View.VISIBLE
                    location.text = data.location
                } else {
                    locationView.visibility = View.GONE
                }

                tvCoachName.text = data.coach_name


                /*if (data.experience != null) {
                    tvDuration.text = "${data.experience} Years of experience"
                } else {
                    tvDuration.text = "${0} Years of experience"
                }*/

                if (data.total_clients!! > 100) {
                    tvClients.text = "Coached ${data.total_clients}+ clients"
                } else {
                    tvClients.text = "Coached ${data.total_clients} clients"
                }

                tvCoachName.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt("coachId", data.id)
                    val intent = Intent(context, SetupAllActivity::class.java)
                    intent.putExtra(Constants.DESTINATION, Constants.COACH_PROFILE)
                    intent.putExtras(bundle)
                    context.startActivity(intent)
                }

                chat.setOnClickListener {
                    context.startActivity(Intent(context, ChatActivity::class.java).apply {
                        putExtra("coach_name", data.coach_name)
                        putExtra("coach_profile", data.image_url)
                        putExtra("coach_id", data.id)
                        putExtra("user_id", Session.getUserDetails().id)
                    })
                }

                next.setOnClickListener {
                    listner.onClick(
                        data.id,
                        data.coach_name,
                        data.image_url,
                        data.location,
                        data.experience,
                        data.total_clients,
                        null
                    )
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CoachListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position])

    fun setList(list: List<CoachListResponse.Datum>?) {
        this.list = list!!
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    interface CoachListInterface {
        fun onClick(
            coachId: Int,
            coach_name: String?,
            image_url: String?,
            location: String?,
            years_experience: Int?,
            clients_experience: Int?,
            rating: Int?
        )
    }
}


