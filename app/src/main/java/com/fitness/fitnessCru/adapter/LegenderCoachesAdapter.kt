package com.fitness.fitnessCru.adapter


import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.CaochesListBinding
import com.fitness.fitnessCru.quickbox.utils.getColor
import com.fitness.fitnessCru.response.CoachListingResponse
import java.util.*


class LegenderCoachesAdapter(val context: Context, val listener: SelectedCoach) :
    RecyclerView.Adapter<LegenderCoachesAdapter.ViewHolder>() {

    private var mList = listOf<CoachListingResponse.Data>()

    inner class ViewHolder(val binding: CaochesListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(data: CoachListingResponse.Data) {
            binding.apply {

                Glide.with(context!!).load(data.image_url).placeholder(R.drawable.place_holder)
                    .into(imageView)

//                imageView.setImageResource(arr.image)
                tvcoachName.text = data.coach_name

                val special = StringJoiner(",")

                if (data.coach_specialization != null) {
                    for (element in data.coach_specialization) {
                        special.add(element.name)
                    }
                }
                tvtrainingtype.text = special.toString()

                buttonTv.text = data.coach_slab_type_details?.coach_type.toString()

                if (data.coach_slab_type_details != null)
                    if (data.coach_slab_type_details?.id == 1) {
                        buttonTv.setBackgroundResource(R.drawable.round_background_gradientelite)
                        cardBorder.setCardBackgroundColor(getColor(R.color.elite_g2))
                    } else if (data.coach_slab_type_details?.id == 2) {
                        buttonTv.setBackgroundResource(R.drawable.round_background_gradientpro)
                        cardBorder.setCardBackgroundColor(getColor(R.color.pro_g2))
                    } else if (data.coach_slab_type_details?.id == 3) {
                        buttonTv.setBackgroundResource(R.drawable.round_background_gradientsuper)
                        cardBorder.setCardBackgroundColor(getColor(R.color.super_g2))
                    } else if (data.coach_slab_type_details?.id == 4) {
                        buttonTv.setBackgroundResource(R.drawable.round_background_gradientcelebrity)
                        cardBorder.setCardBackgroundColor(getColor(R.color.celebrity_g2))
                    } else {
                        buttonTv.setBackgroundResource(R.drawable.round_background_gradient)
                        cardBorder.setCardBackgroundColor(getColor(R.color.pro1))
                    }
                else {
                    buttonTv.setBackgroundResource(R.drawable.round_background_gradient)
                    cardBorder.setCardBackgroundColor(getColor(R.color.pro1))
                }
//            val colorsDif = resources.getIntArray(R.array.coachesColor)
//            val rand=Random().nextInt(R.array.coachesColor)
//            button_cv.setCardBackgroundColor(rand);
//            button_tv.setBackgroundColor(rand)
                buttonTv.setOnClickListener {
                    listener.onClick(data.id, data.coach_name, 1)

                }
                itemView.setOnClickListener {
                    listener.onClick(data.id, data.coach_name, 2)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LegenderCoachesAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CaochesListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(mList[position])

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setList(coach: List<CoachListingResponse.Data>) {
        this.mList = coach
        notifyDataSetChanged()
    }

    interface SelectedCoach {
        fun onClick(id: Int, coachName: String, i: Int)
    }
}
