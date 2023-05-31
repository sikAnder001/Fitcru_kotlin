package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.DietTypeListBinding
import com.fitness.fitnessCru.interfaces.QuestionaryInterface
import com.fitness.fitnessCru.response.DietTypeResponse

class DietTypeAdapter(private val context: Context) :
    RecyclerView.Adapter<DietTypeAdapter.ViewHolder>() {

    private var list: List<DietTypeResponse.Data> = ArrayList()
    lateinit var questionaryInterface: QuestionaryInterface
    private var itemPos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DietTypeListBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    fun setList(lists: List<DietTypeResponse.Data>) {
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

    fun setOnClick(questionaryInterface: QuestionaryInterface) {
        this.questionaryInterface = questionaryInterface
    }

    inner class ViewHolder(binding: DietTypeListBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding = binding
        fun bind(data: DietTypeResponse.Data, position: Int) {
            Glide.with(context!!).load(data.image_url).placeholder(R.drawable.place_holder)
                .into(binding.ivDiet)

            binding.parent.setBackgroundResource(if (itemPos == position) R.drawable.round_back_border else R.drawable.round_back_transparent)
            binding.tvName.text = data.name
            binding.tvDescription.text = data.description
            itemView.setOnClickListener {
                questionaryInterface.onClick(data.id)
                itemPos = position
                notifyDataSetChanged()
            }
        }

    }
}