package com.fitness.fitnessCru.adapter

import android.os.CountDownTimer
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.WarmupList2Binding
import com.fitness.fitnessCru.model.ExerciseByWorkoutModel
import com.fitness.fitnessCru.utility.Util
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import java.util.*


class ExerciseListAdapter2(
    private val activity: AppCompatActivity,
    val listner: SelectVideoData
) :
    RecyclerView.Adapter<ExerciseListAdapter2.ViewHolder>() {

    var simpleExoPlayer: SimpleExoPlayer? = SimpleExoPlayer.Builder(activity).build()

    private var youtube: YouTubePlayer? = null
    private var data = listOf<ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise>()

    var expend = false
    private val videoUri =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

    private var itemPos: Int = -1

    fun capitalize(str: String): String {
        return try {
            str.trim().split("\\s+".toRegex()).joinToString(" ") {
                it.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
            }
        } catch (e: Exception) {
            str
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(WarmupList2Binding.inflate(activity.layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position]!!, position)
    }

    override fun getItemCount(): Int {
        return try {
            data.size
        } catch (e: Exception) {
            0
        }
    }

    inner class ViewHolder(private val binding: WarmupList2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            exerciseData: ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise,
            position: Int
        ) {
            binding.apply {
                if (exerciseData.title == "Rest") {
                    titleTv.text = exerciseData.title
                    repDuration.text = exerciseData.rest

                } else {
                    val video = exerciseData.video
                    if (video == null) {
                        Glide.with(activity!!)
                            .load(exerciseData.banner)
                            .placeholder(R.drawable.place_holder)
                            .into(imageNull)

                    } else {
                        if (Util.checkUrl(video.toString())) {
                            try {
                                val videoId = Util.getVideoId(video.toString())

                                Log.v("video id $position: ", videoId)

                                binding!!.youtubePlayerView.addYouTubePlayerListener(object :
                                    AbstractYouTubePlayerListener() {
                                    override fun onReady(youTubePlayer: YouTubePlayer) {
//                                youtube = youTubePlayer
                                        youTubePlayer?.cueVideo(videoId, 0f)
                                    }

                                    override fun onStateChange(
                                        youTubePlayer: YouTubePlayer,
                                        state: PlayerConstants.PlayerState
                                    ) {
                                        super.onStateChange(youTubePlayer, state)
                                        if (state == PlayerConstants.PlayerState.ENDED) {
                                            listner.onClick(
                                                exerciseData,
                                                position,
                                                exerciseData.ex_type
                                            )
                                        }
                                    }
                                })
                            } catch (e: java.lang.Exception) {
                                Log.d("video error", "youtube $e")
                            }

                        } else {
                            try {

                                val mediaItem = MediaItem.fromUri(exerciseData.video.toString())
                                simpleExoPlayer!!.addMediaItems(listOf(mediaItem))
                                simpleExoPlayer!!.prepare()
                                simpleExoPlayer!!.setPlayWhenReady(false)

                                simpleExoPlayer!!.addListener(object : Player.EventListener {

                                    override fun onPlayerStateChanged(
                                        playWhenReady: Boolean,
                                        playbackState: Int
                                    ) {
                                        if (playbackState == Player.STATE_BUFFERING) {
                                            binding.vprogressBar.visibility = View.VISIBLE
                                        } else if (playbackState == Player.STATE_READY) {
                                            binding.vprogressBar.visibility = View.GONE
                                        }
                                        if (playbackState == Player.STATE_ENDED) {
                                            listner.onClick(
                                                exerciseData,
                                                position,
                                                exerciseData.ex_type
                                            )
                                        }
                                    }
                                })
                            } catch (e: java.lang.Exception) {
                                Log.d("video error", "exoplyer$e")
                            }
                        }
                    }
                    if (itemPos == position) {
                        if (video == null) {
                            binding.imageLayout.visibility = View.VISIBLE
                            binding.youtubeLayout.visibility = View.GONE
                            binding.expendableLayout.visibility = View.GONE
                            val timer: CountDownTimer = object : CountDownTimer(10000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {}
                                override fun onFinish() {
                                    listner.onClick(
                                        exerciseData,
                                        position,
                                        exerciseData.ex_type
                                    )
                                    //(or GONE)
                                }
                            }.start()
                        } else {
                            if (Util.checkUrl(video.toString())) {
                                binding.expendableLayout.visibility = View.GONE
                                binding.youtubeLayout.visibility = View.VISIBLE
                                binding.imageLayout.visibility = View.GONE
//                   youtube?.play()
                            } else {
                                //check for youtube
                                binding.playerView.player = simpleExoPlayer

                                binding.playerView.player?.seekTo(0)
//                    binding.playerView.player?.play()
                                val animSlideDown = AnimationUtils.loadAnimation(
                                    activity,
                                    R.anim.zexpend_video
                                )
                                //youtube
                                binding.youtubeLayout.visibility = View.GONE
                                binding.imageLayout.visibility = View.GONE

                                binding.expendableLayout.startAnimation(animSlideDown)
                                Log.v("test", "if: ${simpleExoPlayer}")
                                binding.expendableLayout.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        binding.playerView.player?.pause()
                        Log.v("test1", "else: ${simpleExoPlayer}")
                        //youtube
                        binding.youtubeLayout.visibility = View.GONE
                        binding.imageLayout.visibility = View.GONE
                        binding.expendableLayout.visibility = View.GONE
                    }


                    if (exerciseData.ex_type != null) {
//                        tvTitle.text = exerciseData.ex_type.uppercase()
                        tvTitle.text =
                            if (exerciseData.sets_type.equals("time") || exerciseData.sets_type.equals(
                                    null
                                )
                            )
                                "${exerciseData.ex_type.uppercase(Locale.getDefault())} " + if (exerciseData.sets2 != null) capitalize(
                                    exerciseData.sets2.toString()
                                ) else ""
                            else if (exerciseData.sets_type.equals("reps"))
                                "${exerciseData.ex_type.uppercase(Locale.getDefault())} X " + capitalize(
                                    exerciseData.sets2.toString()
                                ) + " Sets"
                            else if (exerciseData.sets_type.equals("distance"))
                                "${exerciseData.ex_type.uppercase(Locale.getDefault())} " + if (exerciseData.sets2 != null) capitalize(
                                    exerciseData.sets2.toString()
                                ) else ""
                            else ""

                        if (exerciseData.description2 != null) {
                            if (exerciseData.description2.length > 18) {
                                tvDescription.text = Html.fromHtml(
                                    exerciseData.description2.substring(
                                        0,
                                        18
                                    ) + "..." + "<font color='#FF02BD'> <u>View More</u></font>"
                                )
                            } else {
                                tvDescription.text = exerciseData.description2
                            }
                            tvDescription.setOnClickListener {
                                if (tvDescription.text.toString().endsWith("View More")) {
                                    tvDescription.text = exerciseData.description2
                                } else {
                                    if (exerciseData.description2.length > 18) {
                                        tvDescription.text = Html.fromHtml(
                                            exerciseData.description2.substring(
                                                0,
                                                18
                                            ) + "..." + "<font color='#FF02BD'> <u>View More</u></font>"
                                        )
                                    } else tvDescription.text = exerciseData.description2
                                }
                            }
                        }
//                        tvDescription.text = exerciseData.description2
                        tvDescription2.text = exerciseData.description2
                        tvTitle.visibility = View.VISIBLE
                        tvDescription2.visibility = View.VISIBLE
                        if (exerciseData.ex_type2.equals("normal", true)) {
                            tvDescription2.visibility = View.GONE
                        }
                    } else {
                        tvTitle.visibility = View.GONE
                        tvDescription2.visibility = View.GONE
                    }

                    titleTv.text = exerciseData.title


                    tvTargetText.text = exerciseData.target_text

                    binding.expandsesslayout.setOnClickListener {
                        itemPos = position

                        notifyDataSetChanged()
                    }

                    repDuration.text =
                        if (exerciseData.target_type.equals("time", true)) {
                            "${exerciseData.sets ?: 1} * ${exerciseData.target}"
                        } else if (exerciseData.target_type.equals("reps", true)) {
                            "${exerciseData.sets ?: 1} ${exerciseData.target_type}"
                        } else if (exerciseData.target_type.equals("distance", true)) {
                            "${exerciseData.target}"
                        } else ""
                    Util.loadImage(activity, imageView, exerciseData.banner)

                }
            }
        }
    }

    fun stylist(list: ArrayList<ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise>) {
        this.data = list
        notifyDataSetChanged()
    }

    interface SelectVideoData {
        fun onClick(
            data: ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise,
            pos: Int,
            exType: String?
        )
    }

}