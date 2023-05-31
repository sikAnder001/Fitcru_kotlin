package com.fitness.fitnessCru.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.DashboardActivity
import com.fitness.fitnessCru.activities.PlanDetailActivity
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.model.SubPlanParentModel
import com.fitness.fitnessCru.utility.Constants


class SubPlanParentAdapter(
    private var pareantModels: List<SubPlanParentModel>,
    var context: Context,
    val num: Int
) :
    RecyclerView.Adapter<SubPlanParentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sub_plan_rec_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v("num2", num.toString())
        holder.strike.visibility = View.GONE
        holder.intro.text = pareantModels[position].intro.toString()
        holder.price.text = pareantModels[position].price.toString()
        holder.month.text = pareantModels[position].month
        holder.btnText.text = pareantModels[position].btnText
        //        int color = Color.parseColor(pareantModels.get(position).color);
//            holder.planMainContainer.setBackgroundColor(color);
        holder.btnLl.setOnClickListener {
            if (pareantModels[position].intro == "FitCru Basic") {
                val intent = Intent(context.applicationContext, DashboardActivity::class.java)
                context.startActivity(intent)
            } else if (pareantModels[position].intro == "FitCru Live") {
                val intent = Intent(context.applicationContext, PlanDetailActivity::class.java)
                intent.putExtra("planId", 2)
                intent.putExtra("catId", 0)
                intent.putExtra("coachId", 0)
                intent.putExtra("price", "999")
                intent.putExtra("planType", "1 month")
                intent.putExtra("coachName", "FitCru Live")
                intent.putExtra("planName", "1 months FitCru Live")
                intent.putExtra("discountPrice", 0)
                intent.putExtra("num", "")
                context.startActivity(intent)
            } else {
                val intent =
                    Intent(context.applicationContext, SetupAllActivity::class.java)
                intent.putExtra(Constants.DESTINATION, Constants.SELECT_COACH)
                intent.putExtra("planId", pareantModels[position].id)
                if (num == 1) {
                    intent.putExtra("num", "done")
                } else {
                    intent.putExtra("num", "")
                }
                context.startActivity(intent)
            }

        }

        holder.planMainContainer.setBackgroundResource(pareantModels[position].color)
        val planChildAdapter: SubPlanChildAdapter
        planChildAdapter = SubPlanChildAdapter(pareantModels[position].childModel, context)
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
        val month: TextView
        val strike: TextView
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
}