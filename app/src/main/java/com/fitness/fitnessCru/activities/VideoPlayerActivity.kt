package com.fitness.fitnessCru.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fitness.fitnessCru.databinding.ActivityVideoPlayerBinding
import com.fitness.fitnessCru.utility.Util
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.activity_video_player.*

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding

    private var videoUri = ""

//    var speed = arrayOf("0.25x", "0.5x", "Normal", "1.5x", "2x")

    private var youtube: YouTubePlayer? = null

    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //    setContentView(R.layout.activity_video_player)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        simpleExoPlayer = SimpleExoPlayer.Builder(applicationContext).build()
        playerView = binding!!.exoVideo
        playerView.player = simpleExoPlayer

        videoUri = intent.extras!!.getString("url").toString()

        Log.v("Video Link", videoUri)

//        if (videoData!!.toString().isNotEmpty()) {
//            try {
//                videoUri = videoData.toString()
//
//            } catch (e: Exception) {
//                Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT).show()
//            }
//        } else videoUri =
//            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

        if (Util.checkUrl(videoUri)) {
            playerView.visibility = View.GONE
            youtube_player_view.visibility = View.VISIBLE
            playYoutubeVideo()
        } else {
            playerView.visibility = View.VISIBLE
            youtube_player_view.visibility = View.GONE
            exoPlayerWork()
        }

        binding.apply {
//            val speedTxt: TextView = playerView.findViewById(R.id.speed)

//            gobackbtn.setOnClickListener {
//                onBackPressed()
//            }

//            exo_playback_speed.setOnClickListener {

//                val builder = AlertDialog.Builder(applicationContext)
//
//                builder.setTitle("Set Speed")
//
//                builder.setItems(speed) { _, which -> // the user clicked on colors[which]
//                    if (which == 0) {
//                        speedTxt.visibility = View.VISIBLE
//                        speedTxt.text = "0.25X"
//                        val param = PlaybackParameters(0.5f)
//                        simpleExoPlayer.playbackParameters = param
//                    }
//                    if (which == 1) {
//                        speedTxt.visibility = View.VISIBLE
//                        speedTxt.text = "0.5X"
//                        val param = PlaybackParameters(0.5f)
//                        simpleExoPlayer.playbackParameters = param
//                    }
//                    if (which == 2) {
//                        speedTxt.visibility = View.GONE
//                        val param = PlaybackParameters(1f)
//                        simpleExoPlayer.playbackParameters = param
//                    }
//                    if (which == 3) {
//                        speedTxt.visibility = View.VISIBLE
//                        speedTxt.text = "1.5X"
//                        val param = PlaybackParameters(1.5f)
//                        simpleExoPlayer.playbackParameters = param
//                    }
//                    if (which == 4) {
//                        speedTxt.visibility = View.VISIBLE
//                        speedTxt.text = "2X"
//                        val param = PlaybackParameters(2f)
//                        simpleExoPlayer.playbackParameters = param
//                    }
//                }
//                builder.show()
//            }
//            fwd.setOnClickListener { simpleExoPlayer.seekTo(simpleExoPlayer.currentPosition + 10000) }
//            rew.setOnClickListener {
//                val num: Long = simpleExoPlayer.currentPosition - 10000
//                if (num < 0) {
//                    simpleExoPlayer.seekTo(0)
//                } else {
//                    simpleExoPlayer.seekTo(simpleExoPlayer.currentPosition - 10000)
//                }
//            }
//            findViewById<View>(R.id.exo_play).setOnClickListener { simpleExoPlayer.play() }
//            findViewById<View>(R.id.exo_pause).setOnClickListener { simpleExoPlayer.pause() }

        }
    }

    private fun playYoutubeVideo() {
        val videoId = Util.getVideoId(videoUri)
        lifecycle.addObserver(binding!!.youtubePlayerView)
        binding!!.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0f)
                youtube = youTubePlayer
                //progressFun()
            }
        })

    }

    private fun exoPlayerWork() {
        val mediaItem: MediaItem = MediaItem.fromUri(this.videoUri)
        simpleExoPlayer.apply {
            addMediaItems(listOf(mediaItem))

            try {
                prepare()
                seekTo(0)
                play()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        //play by user
        simpleExoPlayer.playWhenReady = false

        simpleExoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == ExoPlayer.STATE_ENDED) {
                    onBackPressed()
                }
            }
        })
        playerView.setControllerVisibilityListener { }
    }

    override fun onBackPressed() {
        try {
            simpleExoPlayer.stop()
            finish()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}
