package com.fitness.fitnessCru.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.EditProfileActivity
import com.fitness.fitnessCru.model.SNCProgramModel

class SNCAdapter(
    var list: List<SNCProgramModel.Data.Sessionlist.Session>?,
    val context: Context,
    var program_id: Int,
    val listener: SelectVideoListener
) :
    RecyclerView.Adapter<SNCAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sessionVideoIV: ImageView = itemView.findViewById(R.id.session_video_iv)
        val sessionTitle: TextView = itemView.findViewById(R.id.session_title_tv)
        val imgCv: CardView = itemView.findViewById(R.id.imgCv)

        //        val sessionDuration: TextView = itemView.findViewById(R.id.session_duration_tv)
        /*  val sessionDurationcontainer: LinearLayoutCompat =
              itemView.findViewById(R.id.session_duration_container)*/
        val sessionDayBtn: TextView = itemView.findViewById(R.id.day_btn)
        val playBtn: ImageView = itemView.findViewById(R.id.play_btn)

        // val sessionAddToLibrary: TextView = itemView.findViewById(R.id.session_add_to_library_tv)
        // val sessionShare: TextView = itemView.findViewById(R.id.session_share_tv)
        val mainContainer: ConstraintLayout = itemView.findViewById(R.id.s_n_c_item_main_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sessions_s_n_c_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SNCAdapter.ViewHolder, position: Int) {
        holder.sessionTitle.text = list!![position].session_name
        /*    holder.sessionDuration.text = list!![position].duration*/
        holder.sessionDayBtn.text = "Day ${list!![position].day}"

        /* holder.sessionVideoIV.setOnClickListener {
             val intent = Intent(context?.applicationContext, SetupAllActivity::class.java)
             intent.putExtra(Constants.DESTINATION, Constants.GO_TO_HELL)
             context?.startActivity(intent)
         }*/
//        holder.sessionAddToLibrary.text = sncItemModel[position].sessionAddToLibrary.toString()
//        holder.sessionShare.text = sncItemModel[position].sessionShare.toString()

        holder.mainContainer.setBackgroundResource(R.drawable.background_for_main_container_s_n_c_item)

        holder.itemView.setOnClickListener {
            if (list!![position].session_type.equals("body_stat")) {
                val intent = Intent(context?.applicationContext, EditProfileActivity::class.java)
                intent.putExtra("pos", "snc")
                context?.startActivity(intent)
            } else if (list!![position].session_type.equals(
                    "photo"
                )
            ) {
                val intent = Intent(context?.applicationContext, EditProfileActivity::class.java)
                intent.putExtra("pos2", "sncHQ")
                context?.startActivity(intent)

            } else if (list!![position].session_type.equals("cardio")) {
                // run video
                if (list!![position].video_url != null) {
                    listener.onClick(list!![position].video_url.toString())
                } else {
                    Toast.makeText(context, "Video is not Available", Toast.LENGTH_SHORT).show()
                }
            } else if (list!![position].session_type.equals("workout")) {
                val bundle = Bundle()
                bundle.putInt("workout_id", list!![position].id!!)
                bundle.putInt("program_id", program_id)

                Navigation.findNavController(
                    context as Activity,
                    R.id.nutrition_setup_fragment_container
                ).navigate(R.id.sncSessionFragment, bundle)
            }
        }

        if (list!![position].session_type.equals("rest")) {
            // make the current view invisible and show the rest day View
            holder.sessionTitle.text = "Rest"
            /* holder.sessionDurationcontainer.visibility = View.GONE*/
            holder.imgCv.visibility = View.GONE
            holder.mainContainer.setBackgroundResource(R.drawable.round_back_totaltransparent)

        } else if (list!![position].session_type.equals("body_stat")) {
            holder.imgCv.visibility = View.GONE
            holder.sessionTitle.text = "Update your statistics"

        } else if (list!![position].session_type.equals("photo")
        ) {
            holder.imgCv.visibility = View.GONE
            holder.sessionTitle.text = "Upload photo"
        } else if (list!![position].session_type.equals("cardio")) {
            if (list!![position].video_url != null) {
                holder.playBtn.visibility = View.VISIBLE
            } else {
                holder.playBtn.visibility = View.GONE
            }
            holder.imgCv.visibility = View.VISIBLE
            Glide.with(context!!)
                .load(list!![position].img)
                .placeholder(R.drawable.place_holder)
                .into(holder.sessionVideoIV)
            holder.sessionTitle.text = list!![position].session_name
        } else {
            holder.imgCv.visibility = View.VISIBLE
            holder.playBtn.visibility = View.VISIBLE
            Glide.with(context!!)
                .load(list!![position].banner)
                .placeholder(R.drawable.place_holder)
                .into(holder.sessionVideoIV)
        }
    }

    override fun getItemCount(): Int {
        return try {
            list!!.size
        } catch (e: Exception) {
            0
        }
    }

    interface SelectVideoListener {
        fun onClick(video: String)
    }
}