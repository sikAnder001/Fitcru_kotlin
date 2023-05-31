package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.HowitworkListBinding
import com.fitness.fitnessCru.model.Howitworkspojo

class HowitWorks(var context: Context?, var list: ArrayList<Howitworkspojo>) :
    RecyclerView.Adapter<HowitWorks.MyViewHolder>() {
    private var bindingS: HowitworkListBinding? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        bindingS = HowitworkListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(bindingS!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val s = list[position]
        bindingS!!.imgIMG.setImageResource(s.img)
        bindingS!!.detailTV.text = s.detail
    }

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    inner class MyViewHolder(private val binding: HowitworkListBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}