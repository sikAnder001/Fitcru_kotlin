package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.databinding.TrainswithcoachlistBinding
import com.fitness.fitnessCru.response.TrendingOfferResponse
import com.fitness.fitnessCru.utility.Constants


class TraniwithCoach(val context: Context?, val list: List<TrendingOfferResponse.Coaches>) :
    RecyclerView.Adapter<TraniwithCoach.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TrainswithcoachlistBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(val binding: TrainswithcoachlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TrendingOfferResponse.Coaches) {
            with(binding) {
                Glide.with(context!!).load(data.image_url)
                    .into(imgIMG)
                coachname.text = data.coach_name
                //   coachdetail.setText(s.detail) coach specialization is required

                itemView.setOnClickListener {
                    val intent = Intent(context, SetupAllActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra(Constants.DESTINATION, Constants.COACH_PROFILE)
                    intent.putExtra("coachId", data.id)
                    Log.e("TAG", "bind: ${data.id.toString()}")
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position])


}
