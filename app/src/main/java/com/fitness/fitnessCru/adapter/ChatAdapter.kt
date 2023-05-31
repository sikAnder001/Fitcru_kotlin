package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.activities.ChatActivity
import com.fitness.fitnessCru.databinding.ChatRvItemBinding
import com.fitness.fitnessCru.response.ChatListResponse
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(private val context: Context?) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private var list: ArrayList<ChatListResponse.Data> = ArrayList()

    inner class ViewHolder(val binding: ChatRvItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ChatListResponse.Data, position: Int) {
            binding.apply {
                personName.text = data.coach_name
                chatHint.text = data.last_message
                time.text = time(data.last_message_updated_time)
                date.text = time2(data.last_message_updated_time)
                Util.loadImage(context!!, personImage, data.image_url)
                root.setOnClickListener {
                    context.startActivity(Intent(context, ChatActivity::class.java).apply {
                        putExtra("coach_name", data.coach_name)
                        putExtra("coach_profile", data.image_url)
                        putExtra("coach_id", data.coach_id)
                        putExtra("user_id", Session.getUserDetails().id)
                    })
                }
            }
        }
    }

    private fun time(string: String): String {
        val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateValue: Date = input.parse(string)
        val output = SimpleDateFormat("hh:mm a");
        return output.format(dateValue)
    }

    private fun time2(string: String): String {
        val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateValue: Date = input.parse(string)
        val messageTime = Calendar.getInstance()
        messageTime.time = dateValue
        val now = Calendar.getInstance()
        val strTimeFormat = "h:mm aa"
        val strDateFormat = "dd/MM/yyyy"
        return if (now[Calendar.DATE] === messageTime.get(Calendar.DATE) &&
            now[Calendar.MONTH] === messageTime.get(Calendar.MONTH)
            &&
            now[Calendar.YEAR] === messageTime.get(Calendar.YEAR)
        ) {
            "Today" // + DateFormat.format(strTimeFormat, messageTime)
        } else if (now[Calendar.DATE] - messageTime.get(Calendar.DATE) === 1
            &&
            now[Calendar.MONTH] === messageTime.get(Calendar.MONTH)
            &&
            now[Calendar.YEAR] === messageTime.get(Calendar.YEAR)
        ) {
            "Yesterday" // + DateFormat.format(strTimeFormat, messageTime)
        } else {
            "" + DateFormat.format(strDateFormat, messageTime)
        }
        return ""
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = ChatRvItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setAddressList(lists: ArrayList<ChatListResponse.Data>) {
        list = lists
        notifyDataSetChanged()
    }
}