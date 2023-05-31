package com.fitness.fitnessCru.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.WorkoutBenefitsItemBinding
import com.fitness.fitnessCru.response.WorkoutProgramResponse

class WorkOutBenefitsAdapter(private val context: Context?) :
    RecyclerView.Adapter<WorkOutBenefitsAdapter.ViewHolder>() {

    private var list = ArrayList<WorkoutProgramResponse.Data.Benefit>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkOutBenefitsAdapter.ViewHolder {
        return ViewHolder(
            WorkoutBenefitsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(
        holder: WorkOutBenefitsAdapter.ViewHolder,
        position: Int
    ) = holder.bind(list[position])

    fun setList(list: ArrayList<WorkoutProgramResponse.Data.Benefit>) {

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

    inner class ViewHolder(val binding: WorkoutBenefitsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(data: WorkoutProgramResponse.Data.Benefit) {
            binding.apply {

                benifitsTitle.text = data.title

                benifitsDescription.text =
                    Html.fromHtml(data.description, Html.FROM_HTML_MODE_LEGACY)

                Glide.with(context!!).load(data.image_url)
                    .placeholder(R.drawable.place_holder)
                    .into(binding.benifitIcon)
            }
        }
    }

}