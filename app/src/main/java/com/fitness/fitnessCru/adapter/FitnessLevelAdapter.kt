package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.FitnessLevelListBinding
import com.fitness.fitnessCru.interfaces.QuestionaryInterface
import com.fitness.fitnessCru.response.FitnessLevelResponse

class FitnessLevelAdapter(private val context: Context) :
    RecyclerView.Adapter<FitnessLevelAdapter.ViewHolder>() {
    lateinit var questionaryInterface: QuestionaryInterface
    private var list: List<FitnessLevelResponse.Data> = ArrayList()
    private var itemPos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FitnessLevelListBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    fun setOnClick(questionaryInterface: QuestionaryInterface) {
        this.questionaryInterface = questionaryInterface
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    fun setList(lists: List<FitnessLevelResponse.Data>) {
        list = lists
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    inner class ViewHolder(binding: FitnessLevelListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var binding = binding
        fun bind(data: FitnessLevelResponse.Data, position: Int) {
            Glide.with(context!!).load(data.image_url).placeholder(R.drawable.place_holder)
                .into(binding.ivFitness)

            binding.parent.setBackgroundResource(if (itemPos == position) R.drawable.round_back_border else R.drawable.round_back_transparent)
            binding.tvName.text = data.name
            binding.tvDescription.text = data.detail
            itemView.setOnClickListener {
                itemPos = position
                notifyDataSetChanged()
                questionaryInterface.onClick(data.id)
            }
        }

    }
}