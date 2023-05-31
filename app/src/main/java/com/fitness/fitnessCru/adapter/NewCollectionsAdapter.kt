package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.databinding.NewCollectionsListBinding
import com.fitness.fitnessCru.response.StudioResponse
import com.fitness.fitnessCru.utility.Constants

class NewCollectionsAdapter(val context: Context?) :
    RecyclerView.Adapter<NewCollectionsAdapter.ViewHolder>() {

    private var list = listOf<StudioResponse.NewCollection>()

    inner class ViewHolder(val binding: NewCollectionsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StudioResponse.NewCollection) {
            binding.apply {
                titleTV.text = data.title
                Glide.with(context!!).load(data.image_url).placeholder(R.drawable.place_holder)
                    .into(imgIMG)

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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewCollectionsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewCollectionsListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    fun setList(list: List<StudioResponse.NewCollection>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: NewCollectionsAdapter.ViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

}
