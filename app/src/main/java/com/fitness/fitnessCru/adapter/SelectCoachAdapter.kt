package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.CaochListingActivity
import com.fitness.fitnessCru.databinding.SelectCoachListBinding
import com.fitness.fitnessCru.response.CoachTypeResponse

class SelectCoachAdapter(val context: Context?, val planId: Int, val num: String) :
    RecyclerView.Adapter<SelectCoachAdapter.ViewHolder>() {
    private var list: List<CoachTypeResponse.Data> = ArrayList()

    inner class ViewHolder(binding: SelectCoachListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding = binding
        fun bind(data: CoachTypeResponse.Data, position: Int) {
            binding.tvName.text = data.name
            Glide.with(context!!).load(data.image_url).placeholder(R.drawable.place_holder)
                .into(binding.fitnessImg)
            itemView.setOnClickListener {
                val intent =
                    Intent(context!!.applicationContext, CaochListingActivity::class.java)
                intent.putExtra("planId", planId)
                intent.putExtra("cat_id", data.id)
                intent.putExtra("num", num)
                context.startActivity(intent)

                /* val intent =
                     Intent(context!!.applicationContext, SetupAllActivity::class.java)
                 intent.putExtra(Constants.DESTINATION, Constants.COACH_PLAN_DETAIL)
                 context.startActivity(intent)*/
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectCoachAdapter.ViewHolder {
        return ViewHolder(
            SelectCoachListBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SelectCoachAdapter.ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    fun setList(selectCoach: List<CoachTypeResponse.Data>) {
        this.list = selectCoach
        notifyDataSetChanged()
    }

}
