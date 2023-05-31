package com.fitness.fitnessCru.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.SelectCoachesListBinding
import com.fitness.fitnessCru.interfaces.SelectCoachInterface
import com.fitness.fitnessCru.response.GetCoachListResponse
import com.google.android.material.chip.Chip


class SelectCoachesAdapter(private val context: Context?, val flag: Boolean) :
    RecyclerView.Adapter<SelectCoachesAdapter.ViewHolder>() {

    private var list = ArrayList<GetCoachListResponse.Data>()

    lateinit var clickInterface: SelectCoachInterface

    inner class ViewHolder(val binding: SelectCoachesListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: GetCoachListResponse.Data, clickInterface: SelectCoachInterface) {
            try {
                binding.apply {
                    coachNameTv.text = data.coach_name
                    coachAddressTv.text = data?.coach_location?.location_name
                    chipGroup.removeAllViews()
                    /* tvAvailableCount.text = data.no_of_slots*/
                    if (context != null) {
                        Glide.with(context).load(data.image_url)
                            .placeholder(
                                context.resources.getDrawable(
                                    R.drawable.place_holder,
                                    null
                                )
                            )
                            .into(coachImg)
                    }
                    data.coach_specialization.forEach { its -> chipGroup.addView(createTagChip(its.name)) }
                    val bundle = Bundle()
                    bundle.putSerializable("dataS", data)
                    bundle.putInt("coachId", data.id)
                    bundle.putString("coach_name", data.coach_name)
                    if (flag) {
                        cardView.setOnClickListener {
                            clickInterface.onBookClicked(
                                binding.bookNow,
                                adapterPosition,
                                1,
                                bundle
                            )
                        }

                        bookNow.setOnClickListener {
                            clickInterface.onBookClicked(
                                binding.bookNow,
                                adapterPosition,
                                0,
                                bundle
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("TAG", "bind: $e")
            }
        }
    }

    private fun createTagChip(chipName: String): Chip {
        return Chip(context).apply {
            text = "$chipName"
            setChipBackgroundColorResource(R.color.overlay_dark_50)
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setTextAppearance(R.style.ChipTextAppearance)
        }
    }

    fun setList(list: ArrayList<GetCoachListResponse.Data>) {
        this.list.clear()
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SelectCoachesListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position], clickInterface)

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    fun setOnClickInterface(clickInterface: SelectCoachInterface) {
        this.clickInterface = clickInterface
    }

}
