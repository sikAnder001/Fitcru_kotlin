package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.TransformationBinding
import com.fitness.fitnessCru.response.DashBoardResponse

class TransformationAdapter(private val context: Context?) :
    RecyclerView.Adapter<TransformationAdapter.ViewHolder>() {

    private var list = ArrayList<DashBoardResponse.Data.Transformation>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransformationAdapter.ViewHolder {
        return ViewHolder(
            TransformationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TransformationAdapter.ViewHolder, position: Int) =
        holder.bind(list[position])

    fun setList(list: ArrayList<DashBoardResponse.Data.Transformation>) {

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

    inner class ViewHolder(val binding: TransformationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DashBoardResponse.Data.Transformation) {
            binding.apply {
                /* details.text = "${data.name}, ${data.age} Years"
                 tvMessage.text = "${data.weight} KG"*/

                details.text = "${data.name}"
                tvMessage.text = "Lost ${data.weight} KG in ${data.month}"
                Glide.with(context!!).load(data.before_image_url)
                    .placeholder(R.drawable.place_holder).into(image1)
                Glide.with(context!!).load(data.after_image_url)
                    .placeholder(R.drawable.place_holder).into(image2)
            }
        }
    }

}
