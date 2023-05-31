package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.model.TodaySessionModel

class TodaySessionAdapter(
    var todaySessionModel: ArrayList<TodaySessionModel>, var context: Context?
) :
    RecyclerView.Adapter<TodaySessionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todaySessionViews: TextView = itemView.findViewById(R.id.today_session_views)
        val thumbnailImageTodaySession: ImageView =
            itemView.findViewById(R.id.thumbnail_image_today_session)
        val totalSessionTodaySession: TextView =
            itemView.findViewById(R.id.total_sessions_today_session)
        val sessionDetailTodaySession: TextView =
            itemView.findViewById(R.id.session_detail_today_session)
        val durationTodaySession: TextView = itemView.findViewById(R.id.duration_today_session)
        val calTodaySession: TextView = itemView.findViewById(R.id.cal_today_session)
        val categoryTodaySession: TextView = itemView.findViewById(R.id.category_today_session)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodaySessionAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.today_session_rv_item, parent, false)
        return TodaySessionAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodaySessionAdapter.ViewHolder, position: Int) {
        Glide.with(context!!)
            .load(todaySessionModel[position].thumbnailImageTodaySession)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.thumbnailImageTodaySession)
        holder.todaySessionViews.text = todaySessionModel[position].todaySessionViews.toString()
        holder.totalSessionTodaySession.text =
            todaySessionModel[position].totalSessionTodaySession.toString()
        holder.sessionDetailTodaySession.text =
            todaySessionModel[position].sessionDetailTodaySession.toString()
        holder.durationTodaySession.text =
            todaySessionModel[position].durationTodaySession.toString()
        holder.calTodaySession.text = todaySessionModel[position].calTodaySession.toString()
        holder.categoryTodaySession.text =
            todaySessionModel[position].categoryTodaySession.toString()
        holder.itemView.setOnClickListener {
            com.fitness.fitnessCru.utility.Util.toast(context!!, "Coming soon")
        }
    }

    override fun getItemCount(): Int {
        return todaySessionModel.size
    }
}