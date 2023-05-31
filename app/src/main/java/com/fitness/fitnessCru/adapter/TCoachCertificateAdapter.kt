package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.TCoachCertificateRvItemBinding
import com.fitness.fitnessCru.response.GetCoachDetailsResponse

class TCoachCertificateAdapter(val context: Context?) :
    RecyclerView.Adapter<TCoachCertificateAdapter.ViewHolder>() {

    private var list: List<GetCoachDetailsResponse.CoachCertificate> = ArrayList()


    inner class ViewHolder(tCoachCertificateBinding: TCoachCertificateRvItemBinding) :
        RecyclerView.ViewHolder(tCoachCertificateBinding.root) {

        var tCoachCertificateBinding = tCoachCertificateBinding

        fun bind(data: GetCoachDetailsResponse.CoachCertificate) {

            tCoachCertificateBinding.apply {

                if (data.status == "Approved") {
                    allNAll.visibility = View.VISIBLE
                    Glide.with(context!!).load(data.image_url)
                        .placeholder(R.drawable.place_holder)
                        .into(tCoachCertificateBinding.certificateImg)

                    certificateName.text = data.certificate_name
                } else {
                    allNAll.visibility = View.GONE
                }
            }

        }

    }

    fun setList(lists: List<GetCoachDetailsResponse.CoachCertificate>) {
        list = lists
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TCoachCertificateAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val tCoachCertificateBinding =
            TCoachCertificateRvItemBinding.inflate(inflater, parent, false)

        return ViewHolder(tCoachCertificateBinding)
    }

    override fun onBindViewHolder(holder: TCoachCertificateAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}