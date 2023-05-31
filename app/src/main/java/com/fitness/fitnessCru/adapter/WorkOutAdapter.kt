package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.EditProfileActivity
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.databinding.DashWorkOutListBinding
import com.fitness.fitnessCru.response.DashBoardResponse
import com.fitness.fitnessCru.utility.Constants

class WorkOutAdapter(private val context: Context?, val listener: SelectVideoListener) :
    RecyclerView.Adapter<WorkOutAdapter.ViewHolder>() {

    private var list = ArrayList<DashBoardResponse.Data.Workout>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkOutAdapter.ViewHolder {
        return ViewHolder(
            DashWorkOutListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WorkOutAdapter.ViewHolder, position: Int) =
        holder.bind(list[position])

    fun setList(list: ArrayList<DashBoardResponse.Data.Workout>) {

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

    inner class ViewHolder(val binding: DashWorkOutListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DashBoardResponse.Data.Workout) {
            binding.apply {

                tvWorkoutName.text = data.session_name

                Glide.with(context!!).load(data.banner).placeholder(R.drawable.place_holder)
                    .into(imageView)

                if (data.session_type == "rest") {
                    imageView.visibility = View.GONE
                    targetTv.visibility = View.GONE
                    equipTv.visibility = View.GONE
                    playBtn.visibility = View.GONE
                    startworkoutTV.visibility = View.GONE
                    tvWorkoutName.text = "Rest"

                } else if (data.session_type == "body_stat") {
                    equipTv.visibility = View.GONE
                    targetTv.visibility = View.GONE

                    imageView.visibility = View.GONE

                    startworkoutTV.visibility = View.GONE

                    playBtn.visibility = View.GONE
                    tvWorkoutName.text = "Update your statistics"
                } else if (data.session_type == "photo") {
                    startworkoutTV.visibility = View.GONE

                    imageView.visibility = View.GONE

                    playBtn.visibility = View.GONE
                    equipTv.visibility = View.GONE
                    targetTv.visibility = View.GONE
                    tvWorkoutName.text = "Upload photo"

                } else if (data.session_type == "cardio") {
                    startworkoutTV.visibility = View.GONE

                    if (!data.video_url.isNullOrEmpty()) {
                        playBtn.visibility = View.VISIBLE
                    } else {
                        playBtn.visibility = View.GONE
                    }

                    if (data.equipments != null) {
                        equipTv.visibility = View.VISIBLE
                        equipTv.text = data.equipments
                    } else {
                        equipTv.visibility = View.GONE
//                        targetDuration.visibility = View.GONE
                    }

                    if (!data.cardio_target.isNullOrEmpty()) {
                        targetTv.visibility = View.VISIBLE
                        targetTv.text = "Target- ${data.cardio_target} ${data.cardio_val}"
                    } else targetTv.visibility = View.GONE

                    imageView.visibility = View.VISIBLE
                    Glide.with(context!!)
                        .load(list!![position].img)
                        .placeholder(R.drawable.place_holder)
                        .into(imageView)

                } else {

                    equipTv.visibility = View.GONE
                    targetTv.visibility = View.GONE

                    imageView.visibility = View.VISIBLE
                    startworkoutTV.visibility = View.VISIBLE
                    Glide.with(context!!)
                        .load(list!![position].banner)
                        .placeholder(R.drawable.place_holder)
                        .into(imageView)
                }

                itemView.setOnClickListener {
                    if (data.session_type == "body_stat") {
                        val intent =
                            Intent(context?.applicationContext, EditProfileActivity::class.java)
                        intent.putExtra("pos", "snc")
                        context?.startActivity(intent)
                    } else if (data.session_type == "photo") {
                        val intent =
                            Intent(context?.applicationContext, EditProfileActivity::class.java)
                        intent.putExtra("pos2", "sncHQ")
                        context?.startActivity(intent)

                    } else if (data.session_type == "cardio") {
                        // run video
                        if (data.video_url != null) {
                            listener.onClick(data.video_url.toString())
                        }
                    }
                }

                startworkoutTV.setOnClickListener {
                    val intent = Intent(context, SetupAllActivity::class.java)
                    intent.putExtra(Constants.DESTINATION, Constants.HOME_TO_WORKOUT)
                    intent.putExtra("workout_id", data.id)
                    intent.putExtra("program_id", -1)
                    context.startActivity(intent)
                }
            }
        }
    }

    interface SelectVideoListener {
        fun onClick(video: String)
    }
}