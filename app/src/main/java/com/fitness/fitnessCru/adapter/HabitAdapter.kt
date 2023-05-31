package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.databinding.HabitRvItemBinding
import com.fitness.fitnessCru.response.HabitsResponse

class HabitAdapter(
    /*var list: List<HabitsResponse.HabitsListData>?,*/
    val context: Context,
    val listener: SelectVideoListener

) : RecyclerView.Adapter<HabitAdapter.ViewHolder>() {

    private var list: List<HabitsResponse.HabitsListData> = ArrayList()

    inner class ViewHolder(habitBinding: HabitRvItemBinding) :
        RecyclerView.ViewHolder(habitBinding.root) {
        /*
        val habbitVideoIV: ImageView = itemView.findViewById(R.id.thumbnail_image_habit)
*/

        var habitBinding = habitBinding

        fun bind(habitModel: HabitsResponse.HabitsListData, position: Int) {

            habitBinding.apply {

                Glide.with(context!!).load(habitModel.banner)
                    .into(thumbnailImageHabit)

                sessionDetailHabit.text = habitModel.name

                habitContent.text = habitModel.content

                categoryHabit.text = habitModel.habit_category.name
            }

        }

    }

    fun setList(lists: List<HabitsResponse.HabitsListData>) {
        list = lists
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val habitBinding = HabitRvItemBinding.inflate(inflater, parent, false)
        return ViewHolder(habitBinding)
    }

    override fun onBindViewHolder(holder: HabitAdapter.ViewHolder, position: Int) {
        holder.bind(list[position], position)
        holder.itemView.setOnClickListener {

            if (list!![position].video != null) {
                listener.onClick(list!![position].video.toString())
            } else {
                Toast.makeText(context, "Video is not Available", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun getItemCount(): Int {
        return try {
            list!!.size
        } catch (e: Exception) {
            0
        }
    }

    interface SelectVideoListener {
        fun onClick(video: String)
    }
}