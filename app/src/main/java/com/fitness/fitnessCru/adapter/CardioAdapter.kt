package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.response.StudioDetailsResponse

class CardioAdapter(
    var context: Context?,
    val listner: AvailabilityInterface
) :
    RecyclerView.Adapter<CardioAdapter.ViewHolder>() {
    var cardioListData = ArrayList<StudioDetailsResponse.StudioData.StudioWorkouts>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var thumbnailImage: ImageView = itemView.findViewById(R.id.thumbnail_image)
        val description: TextView = itemView.findViewById(R.id.description)
        val title: TextView = itemView.findViewById(R.id.title)
        val cardioDuration: TextView = itemView.findViewById(R.id.cardio_duration)
        val cardioCategory: TextView = itemView.findViewById(R.id.cardio_category)
    }

    fun setCardioList(cardioListData: List<StudioDetailsResponse.StudioData.StudioWorkouts>) {
//        this.cardioListData.addAll(cardioListData)
        this.cardioListData =
            cardioListData as ArrayList<StudioDetailsResponse.StudioData.StudioWorkouts>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardioAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardio_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardioAdapter.ViewHolder, position: Int) {
        Glide.with(context!!)
            .load(cardioListData[position].banner)
            .placeholder(R.drawable.place_holder)
            .into(holder.thumbnailImage)
        /* holder.views.text = "${cardioListData[position].views} views"*/
        holder.description.text = cardioListData[position].description
        holder.title.text = cardioListData[position].session_name
        holder.cardioCategory.text = "${cardioListData[position].types.name}"
        /*if (cardioListData[position].types == "Regular") {
            holder.cardioCategory.visibility = View.GONE
        } else {
            holder.cardioCategory.visibility = View.VISIBLE
            holder.cardioCategory.text = "${cardioListData[position].types},"
        }*/
        //        holder.cardioDuration.text = cardioListData[position].time
        holder.itemView.setOnClickListener {
            listner.onClick(cardioListData!![position].id!!, -2)
        }
    }

    override fun getItemCount(): Int {
        return cardioListData.size
    }

    interface AvailabilityInterface {
        fun onClick(workout_id: Int, program_id: Int)
    }
}