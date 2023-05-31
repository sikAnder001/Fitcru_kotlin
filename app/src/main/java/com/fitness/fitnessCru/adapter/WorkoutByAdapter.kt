package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.databinding.WorkoutByListBinding
import com.fitness.fitnessCru.response.StudioResponse
import com.fitness.fitnessCru.utility.Constants

class WorkoutByAdapter(val context: Context?) :
    RecyclerView.Adapter<WorkoutByAdapter.ViewHolder>() {

    private var list = listOf<StudioResponse.WorkoutByIntensity>()

    inner class ViewHolder(val binding: WorkoutByListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StudioResponse.WorkoutByIntensity) {
            binding.apply {
                titleTv.text = data.name
                detail.text = "${data.total_session} Sessions"
                Glide.with(context!!).load(data.image_url)
                    .placeholder(R.drawable.place_holder).into(imgIMG)
            }

            itemView.setOnClickListener {
                val intent = Intent(context?.applicationContext, SetupAllActivity::class.java)
                intent.putExtra(Constants.DESTINATION, Constants.WORKOUT_BY_FITNESS)
                intent.putExtra("id", data.fitness_level_id)
                intent.putExtra("title", data.name)
                context?.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutByAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WorkoutByListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutByAdapter.ViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    fun setList(list: List<StudioResponse.WorkoutByIntensity>) {
        this.list = list

        notifyDataSetChanged()
    }


}
