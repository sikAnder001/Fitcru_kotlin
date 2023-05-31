package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.activities.SubscriptionPlanActivity
import com.fitness.fitnessCru.databinding.ChipListBinding
import com.fitness.fitnessCru.model.DataChip
import com.fitness.fitnessCru.utility.Constants


class ChipAdapter(
    private val chipdata: ArrayList<DataChip>, val context: Context?,
    val listener: NavInterface
) :
    RecyclerView.Adapter<ChipAdapter.ViewHolder>() {

    private var itemPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ChipListBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(chipdata[position], position)

    override fun getItemCount(): Int {
        return chipdata.size
    }

    inner class ViewHolder(val binding: ChipListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chipdata: DataChip, position: Int) {
            with(binding) {

                imgIMG.setBackgroundResource(chipdata.img)
                chipTV.text = chipdata.name
                chipCategory.setBackgroundResource(
                    if (itemPos == position)
                        R.drawable.circularbg
                    else
                        R.drawable.circularbg
                )
                itemView.setOnClickListener {
                    val position = adapterPosition

                    if (chipdata.id == 1) {
                        val intent = Intent(context, SetupAllActivity::class.java)
                        intent.putExtra(Constants.DESTINATION, Constants.INSIGHTS)
                        intent.putExtra("tab_id", "1")
                        context!!.startActivity(intent)
                    }

                    if (chipdata.id == 2) {
                        listener.onClick(chipdata.id!!)
                        /*                    val intent = Intent(context, SetupAllActivity::class.java)
                                            intent.putExtra(Constants.DESTINATION, Constants.MAIN_COACHING)
                                            context!!.startActivity(intent)*/
                    }

                    if (chipdata.id == 3) {
                        val intent = Intent(context, SubscriptionPlanActivity::class.java)
                        intent.putExtra("num", 2)
                        context!!.startActivity(intent)
//                        val intent = Intent(context, SetupAllActivity::class.java)
//                        intent.putExtra(Constants.DESTINATION, Constants.LAB_TEST)
//                        context!!.startActivity(intent)
                    }

                    itemPos = position
                    notifyDataSetChanged()

                }
            }
        }
    }

    interface NavInterface {
        fun onClick(id: Int)
    }
}

