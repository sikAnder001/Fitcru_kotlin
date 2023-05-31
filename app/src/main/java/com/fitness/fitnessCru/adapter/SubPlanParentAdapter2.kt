package com.fitness.fitnessCru.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.model.SubPlanParentModel


class SubPlanParentAdapter2(
    private var pareantModels: List<SubPlanParentModel>,
    var context: Context,
    val listener: SelectedMonthsPlans
) :
    RecyclerView.Adapter<SubPlanParentAdapter2.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sub_plan_rec_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.intro.visibility = View.GONE

        holder.month.text = ""
        holder.price.text = pareantModels[position].price
        holder.btnText.text =
            if (pareantModels[position].btnText == "₹null") "₹3000" else pareantModels[position].btnText
        //        int color = Color.parseColor(pareantModels.get(position).color);
//            holder.planMainContainer.setBackgroundColor(color);
        holder.btnLl.setOnClickListener {
            listener.onClick(
                if (pareantModels[position].btnText == "₹null") "₹3000" else pareantModels[position].btnText,
                pareantModels[position].month,
                pareantModels[position].price,
                if (pareantModels[position].actualOffer != null) (pareantModels[position].actualOffer!!.actual_price - pareantModels[position].btnText.drop(
                    1
                ).toInt()) else 0
            )
        }
        if (pareantModels[position].actualOffer != null && pareantModels[position].actualOffer!!.offer_price >= 1) {
            val nPrice = pareantModels[position].btnText.drop(1)
            if (nPrice.toInt() == pareantModels[position].actualOffer!!.actual_price || nPrice.toInt() > pareantModels[position].actualOffer!!.actual_price) {
                holder.strike.visibility = View.GONE
            } else {
                holder.strike.visibility = View.VISIBLE
            }
            holder.strike.text = " ₹${pareantModels[position].actualOffer!!.actual_price}"
            holder.strike.setPaintFlags(holder.strike.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
        } else {
            holder.strike.visibility = View.GONE
        }

        holder.planMainContainer.setBackgroundResource(pareantModels[position].color)
        val planChildAdapter: SubPlanChildAdapter2
        planChildAdapter = SubPlanChildAdapter2(pareantModels[position].childModel, context)
        holder.planChildRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.planChildRV.adapter = planChildAdapter
        planChildAdapter.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return pareantModels.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val intro: TextView
        val price: TextView
        val strike: TextView
        val month: TextView
        val planChildRV: RecyclerView
        val btnText: TextView
        val planMainContainer: ConstraintLayout
        val btnLl: LinearLayout

        init {
            intro = itemView.findViewById(R.id.inro_tv)
            price = itemView.findViewById(R.id.price_tv)
            month = itemView.findViewById(R.id.month_tv)
            strike = itemView.findViewById(R.id.strike_value)
            planChildRV = itemView.findViewById(R.id.plan_child_rec)
            btnText = itemView.findViewById(R.id.plan_btn)
            planMainContainer = itemView.findViewById(R.id.plan_main_container)
            btnLl = itemView.findViewById(R.id.btnLl)
        }
    }

    interface SelectedMonthsPlans {
        fun onClick(price: String, month: String, planName: String, discountPrice: Int)
    }
}