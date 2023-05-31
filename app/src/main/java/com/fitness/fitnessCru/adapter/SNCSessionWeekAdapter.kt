package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.model.SNCProgramModel


class SNCSessionWeekAdapter(
    var list: List<SNCProgramModel.Data.Sessionlist>,
    var program_id: Int,
    var context: Context?,
    private val listener: SNCAdapter.SelectVideoListener,
    private val function: () -> Unit
) :
    RecyclerView.Adapter<SNCSessionWeekAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var weekTv: TextView = itemView.findViewById(R.id.week_tv)
        var sessionWeekChildRV: RecyclerView =
            itemView.findViewById(R.id.session_week_parent_rv_item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.session_s_n_c_week_rv_parent_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.weekTv!!.text = "Week ${list[position].week}"
        if (position == list.size - 1)
            function()
        setYogaSearchChildItemRV(
            holder.sessionWeekChildRV,
            list[position].session!!
        )
    }

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }

    private fun setYogaSearchChildItemRV(
        recyclerView: RecyclerView,
        session: List<SNCProgramModel.Data.Sessionlist.Session>
    ) {
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapter =
            SNCAdapter(session, context!!, program_id, object : SNCAdapter.SelectVideoListener {
                override fun onClick(video: String) {
                    listener.onClick(video)
                }
            })
        recyclerView.adapter = adapter
    }

}