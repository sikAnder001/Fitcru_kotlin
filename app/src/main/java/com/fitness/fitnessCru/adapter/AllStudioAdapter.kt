package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.databinding.AllStudioListBinding
import com.fitness.fitnessCru.response.StudioResponse
import com.fitness.fitnessCru.utility.Constants

class AllStudioAdapter(private val context: Context?) :
    RecyclerView.Adapter<AllStudioAdapter.ViewHolder>() {

    private var list = listOf<StudioResponse.Studio>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllStudioAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AllStudioListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: AllStudioAdapter.ViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    fun setList(list: List<StudioResponse.Studio>) {
        this.list = list

        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: AllStudioListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StudioResponse.Studio) {
            binding.apply {
                titleTV.text = data.title
                Glide.with(context!!).load(data.image_url).placeholder(R.drawable.place_holder)
                    .into(imgView)
            }
            itemView.setOnClickListener {
                val intent = Intent(context?.applicationContext, SetupAllActivity::class.java)
                intent.putExtra(Constants.DESTINATION, Constants.STUDIO_DETAILS)
                intent.putExtra("id", data.id)
                intent.putExtra("title", data.title)
                context?.startActivity(intent)
            }
        }

    }
}
