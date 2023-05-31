package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.databinding.TrainswithcoachlistBinding
import com.fitness.fitnessCru.response.StudioResponse
import com.fitness.fitnessCru.utility.Constants
import java.util.*

class StudioTrainWithBestCoachAdapter(private val context: Context?) :
    RecyclerView.Adapter<StudioTrainWithBestCoachAdapter.ViewHolder>() {

    private var list = listOf<StudioResponse.TrainWithBestCoach>()

    inner class ViewHolder(val binding: TrainswithcoachlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(data: StudioResponse.TrainWithBestCoach) {
            binding.apply {

                coachname.text = data.coach_name

                Glide.with(context!!).load(data.image_url).placeholder(R.drawable.place_holder)
                    .into(imgIMG)

                val ids = StringJoiner(" ,")
                for (element in data.specializations!!) {
                    ids.add(element.name)
                }
                coachdetail.text = ids.toString()

            }
            itemView.setOnClickListener {
                val intent = Intent(context?.applicationContext, SetupAllActivity::class.java)
                intent.putExtra(Constants.DESTINATION, Constants.COACH_PROFILE)
                intent.putExtra("coachId", data.id)
                context?.startActivity(intent)
            }

        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudioTrainWithBestCoachAdapter.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = TrainswithcoachlistBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(
        holder: StudioTrainWithBestCoachAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    fun setList(list: List<StudioResponse.TrainWithBestCoach>) {
        this.list = list

        notifyDataSetChanged()
    }
}