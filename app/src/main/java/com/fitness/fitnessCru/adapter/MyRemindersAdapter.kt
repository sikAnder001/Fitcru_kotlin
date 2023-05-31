package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.MyRemindersRvItemBinding
import com.fitness.fitnessCru.interfaces.DotClickInterface
import com.fitness.fitnessCru.response.GetAllReminderResponse

class MyRemindersAdapter(
    val context: Context?
) : RecyclerView.Adapter<MyRemindersAdapter.ViewHolder>() {

    private var list = ArrayList<GetAllReminderResponse.Data>()

    lateinit var clickInterface: DotClickInterface

    inner class ViewHolder(myRemindersItemBinding: MyRemindersRvItemBinding) :
        RecyclerView.ViewHolder(myRemindersItemBinding.root) {

        var myRemindersItemBinding = myRemindersItemBinding

        fun bind(data: GetAllReminderResponse.Data, position: Int) {

            myRemindersItemBinding.apply {

                title.text = data.reminder_title

                details.text = data.reminder_description

                timeTv.text = data.reminder_time

                dotsBtn.setOnClickListener {
                    clickInterface.onDotClick(
                        dotsBtn,
                        data,
                        adapterPosition
                    )
                }
            }
        }

    }

    fun setList(list: ArrayList<GetAllReminderResponse.Data>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyRemindersAdapter.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val myRemindersItemBinding = MyRemindersRvItemBinding.inflate(inflater, parent, false)

        return ViewHolder(myRemindersItemBinding)

    }

    override fun onBindViewHolder(holder: MyRemindersAdapter.ViewHolder, position: Int) =
        holder.bind(list[position], position)

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    fun setOnClickInterface(clickInterface: DotClickInterface) {
        this.clickInterface = clickInterface
    }
}