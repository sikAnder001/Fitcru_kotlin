package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.activities.SubscriptionPlanActivity
import com.fitness.fitnessCru.databinding.WorkoutPopularCollectionListBinding
import com.fitness.fitnessCru.response.WorkoutProgramResponse
import com.fitness.fitnessCru.utility.Constants

class WorkOutGoalChildAdapter(private val context: Context?, val isPaid: Int) :
    RecyclerView.Adapter<WorkOutGoalChildAdapter.ViewHolder>() {

    private var list = ArrayList<WorkoutProgramResponse.Data.GoalsWithProgram.Program>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkOutGoalChildAdapter.ViewHolder {
        return ViewHolder(
            WorkoutPopularCollectionListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )
    }

    override fun onBindViewHolder(
        holder: WorkOutGoalChildAdapter.ViewHolder,
        position: Int
    ) = holder.bind(list[position])

    fun setList(list: ArrayList<WorkoutProgramResponse.Data.GoalsWithProgram.Program>) {

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

    inner class ViewHolder(val binding: WorkoutPopularCollectionListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: WorkoutProgramResponse.Data.GoalsWithProgram.Program) {
            binding.apply {
                tvTitle.text = data.name
                tvDescription.text = data.description
                Glide.with(context!!).load(data.image_url).placeholder(R.drawable.place_holder)
                    .into(posterImg)

                if (isPaid == 1 && data.price != null) {
                    lockImg.visibility = View.GONE
                    itemView.setOnClickListener {
                        val intent =
                            Intent(context.applicationContext, SetupAllActivity::class.java)
                        intent.putExtra(Constants.DESTINATION, Constants.WORKOUT2)
                        intent.putExtra("id", data.id)
                        context.startActivity(intent)
                    }
                } else {
                    if (data.price != null) {
                        lockImg.visibility = View.VISIBLE
                        itemView.setOnClickListener {
                            val intent = Intent(context, SubscriptionPlanActivity::class.java)
                            intent.putExtra("num", 3)
                            context!!.startActivity(intent)
                        }
                    } else {
                        lockImg.visibility = View.GONE
                        itemView.setOnClickListener {
                            val intent =
                                Intent(context.applicationContext, SetupAllActivity::class.java)
                            intent.putExtra(Constants.DESTINATION, Constants.WORKOUT2)
                            intent.putExtra("id", data.id)
                            context.startActivity(intent)
                        }
                    }

                }

            }
        }
    }

}