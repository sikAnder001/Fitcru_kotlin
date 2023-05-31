package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.PopularCollectionsListBinding
import com.fitness.fitnessCru.response.StudioResponse

class PopularCollectionsAdapter(
    val context: Context?
) : RecyclerView.Adapter<PopularCollectionsAdapter.ViewHolder>() {

    private var list = listOf<StudioResponse.PopularCollection>()

    inner class ViewHolder(val binding: PopularCollectionsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StudioResponse.PopularCollection) {
            with(binding) {
                titleTv.text = data.name
                sessionNumbersTV.text = ""
                Glide.with(context!!).load(data.image_url).placeholder(R.drawable.place_holder)
                    .into(imgIMG)
            }
            /* itemView.setOnClickListener {
                 val intent = Intent(context?.applicationContext, SetupAllActivity::class.java)
                 intent.putExtra(Constants.DESTINATION, Constants.STUDIO_DETAILS)
                 intent.putExtra("id", data.id)
                 intent.putExtra("title", data.name)
                 context?.startActivity(intent)
             }*/
        }

    }

    fun setList(list: List<StudioResponse.PopularCollection>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PopularCollectionsListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

}
