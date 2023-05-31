package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.response.WorkoutProgramResponse

class WorkoutHappyCustomerAdapter(val context: Context) :
    RecyclerView.Adapter<WorkoutHappyCustomerAdapter.SliderViewHolder>() {
    var happyCustomerModel = ArrayList<WorkoutProgramResponse.Data.HappyCustomer>()

    inner class SliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ratings = view.findViewById<RatingBar>(R.id.rating_bar)
        private val user_img = view.findViewById<ImageView>(R.id.imageview)
        private val user_feedback = view.findViewById<TextView>(R.id.user_feedback)
        private val user_name = view.findViewById<TextView>(R.id.user_name)
        private val user_weight_loss = view.findViewById<TextView>(R.id.user_weight_loss)

        fun bind(happyCustomerModel: WorkoutProgramResponse.Data.HappyCustomer) {
            ratings.rating = try {
                happyCustomerModel.rating_count.toFloat()
            } catch (e: Exception) {
                0f
            }
            Glide.with(context).load(happyCustomerModel.image_url)
                .placeholder(R.drawable.place_holder)
                .into(user_img)
            user_feedback.text = happyCustomerModel.client_short_description
            user_name.text = happyCustomerModel.client_name
            user_weight_loss.text = happyCustomerModel.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.workout_happy_customer_vp_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(happyCustomerModel[position])
    }

    fun setList(list: ArrayList<WorkoutProgramResponse.Data.HappyCustomer>) {

        happyCustomerModel = list

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return try {
            happyCustomerModel.size
        } catch (e: Exception) {
            0
        }
    }
}