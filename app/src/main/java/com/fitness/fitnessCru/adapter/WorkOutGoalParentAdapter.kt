package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.GoalAndProgramParentBinding
import com.fitness.fitnessCru.response.WorkoutProgramResponse


class WorkOutGoalParentAdapter(private val context: Context?, val isPaid: Int) :
    RecyclerView.Adapter<WorkOutGoalParentAdapter.ViewHolder>() {

    private var list = ArrayList<WorkoutProgramResponse.Data.GoalsWithProgram>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkOutGoalParentAdapter.ViewHolder {
        return ViewHolder(
            GoalAndProgramParentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: WorkOutGoalParentAdapter.ViewHolder,
        position: Int
    ) = holder.bind(list[position], position)

    fun setList(list: ArrayList<WorkoutProgramResponse.Data.GoalsWithProgram>) {

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

    inner class ViewHolder(val binding: GoalAndProgramParentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: WorkoutProgramResponse.Data.GoalsWithProgram, pos: Int) {
            binding.apply {
                tvTitle.text = data.title
                var adapter = WorkOutGoalChildAdapter(context, isPaid)
                rvProgram.adapter = adapter
                if (data.programs.size > 0)
                    adapter.setList(data.programs)
                else {
                    tvPlaceHolder.visibility = View.GONE
                    all.visibility = View.GONE
                }
//                itemView.setOnClickListener{
//                    Toast.makeText(context, "program items clicked", Toast.LENGTH_SHORT).show()
//                    val activity= context as AppCompatActivity
//                    val fragment= YogaFragment()
//                    activity.supportFragmentManager
//                        .beginTransaction()
//                        .replace(com.ennovations.fitcrunewandroid.R.id.fragment_container_yoga, fragment).addToBackStack(null)
//                        .commit()
//                }

            }
        }
    }

}