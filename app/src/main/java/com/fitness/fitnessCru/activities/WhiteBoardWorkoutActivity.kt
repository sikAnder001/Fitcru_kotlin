package com.fitness.fitnessCru.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivityWhiteBoardWorkoutBinding
import com.fitness.fitnessCru.databinding.WarmupListBinding
import com.fitness.fitnessCru.databinding.WorkoutListBinding
import com.fitness.fitnessCru.model.ExerciseByWorkoutModel
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.WorkoutExersiseViewModel
import com.fitness.fitnessCru.vm_factory.WorkoutExersiseVMFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


//workout
class WhiteBoardWorkoutActivity : AppCompatActivity() {
    private var time: Long = 0
    private var binding: ActivityWhiteBoardWorkoutBinding? = null
    lateinit var workoutListAdapter: WorkoutListAdapter
    var positions = 0
    var parentPos = MutableLiveData<Int>()
    var childPos = MutableLiveData<Int>()
    var countDownTimer: CountDownTimer? = null
    private lateinit var wholeExercise: ArrayList<ExerciseByWorkoutModel.Data.ExerciseData>
    var type = ""
    lateinit var allData: ArrayList<ExerciseByWorkoutModel.Data.ExerciseData>
    lateinit var exercise: ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise
    private var videoUri = ""
    private var youtube: YouTubePlayer? = null
    /*"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"*/

    //        "http://techslides.com/demos/sample-videos/small.mp4"
    var maxProgress = 0
    var currentProgress = 0

    private lateinit var repository: Repository
    private lateinit var viewModel: WorkoutExersiseViewModel
    private lateinit var factory: WorkoutExersiseVMFactory
    private var workout_id = 0
    private var program_id = 0
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var playerView: PlayerView
    var totSet = 0
    var currentSet = 1
    var videoTime: Long = 0
    var videoRest: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhiteBoardWorkoutBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        program_id = intent.extras!!.getInt("program_id")

        simpleExoPlayer = SimpleExoPlayer.Builder(applicationContext).build()
        playerView = binding!!.exoVideo
        playerView.player = simpleExoPlayer
        repository = Repository()
        factory = WorkoutExersiseVMFactory(repository)
        viewModel = ViewModelProvider(this, factory)[WorkoutExersiseViewModel::class.java]
        findViewById<View>(R.id.exo_play)?.setOnClickListener {
            simpleExoPlayer.play()
        }
        findViewById<View>(R.id.exo_pause)?.setOnClickListener {
            simpleExoPlayer.pause()
        }

        binding!!.gobackbtn.setOnClickListener {
            onBackPressed()
            finish()
        }

        recycleWarmUp()
        parentPos.observe(this) {

            if (it == wholeExercise.size) {
                if (simpleExoPlayer != null) {
                    simpleExoPlayer.playWhenReady = false
                    simpleExoPlayer.stop()
                    simpleExoPlayer.seekTo(0)
                }

                val intent = Intent(applicationContext, SetupAllActivity::class.java)
                intent.putExtra(Constants.DESTINATION, Constants.FEEDBACK)
                intent.putExtra("work_id", workout_id)
                intent.putExtra("program_id", program_id)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (simpleExoPlayer != null) {
            simpleExoPlayer.playWhenReady = false
            simpleExoPlayer.stop()
            simpleExoPlayer.seekTo(0)
        }
    }

    private fun recycleWarmUp() {
        //  showToast("finish")
        workoutListAdapter = WorkoutListAdapter(
            this@WhiteBoardWorkoutActivity,
            object : SelectVideoData {
                override fun onClick(
                    data: ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise,
                    pos: Int,
                    itemView: View
                ) {
                    exercise = data
                    progressFun()
                    totSet = 0
                    currentSet = 1
                    videoTime = 0
                    videoRest = 0


                }
            })

        binding!!.rvWorkoutList.adapter = workoutListAdapter

        workout_id = intent.extras!!.getInt("work_id")

        viewModel.getExercise(workout_id)

        viewModel.response.observe(this@WhiteBoardWorkoutActivity) {
            youtubePlay()
            if (it.isSuccessful && it.code() == 200 && it.body() != null) {
                binding.apply {
                    try {
                        if (it.isSuccessful && it.body() != null) {
                            var data = it.body()!!.data

                            allData = ArrayList()

                            for (i in data.exercises_data) {
                                var tmpex =
                                    arrayListOf<ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise>()

                                var set: Int = try {
                                    i.details.sets?.toInt()!!
                                } catch (e: Exception) {
                                    1
                                }
                                for (toy in 0 until set) {
                                    var arrex = i.details.exercise
                                    if (arrex != null) {
                                        for (exerciseItem in arrex) {
                                            tmpex.add(exerciseItem)
                                        }
                                    } else {
                                        tmpex.add(
                                            ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise(
                                                null,
                                                null,
                                                null,
                                                null,
                                                "Rest",
                                                null,
                                                null,
                                                i.details.sets,
                                                null,
                                                null,
                                                null,
                                                null
                                            )
                                        )
                                    }
                                }

                                var tmpNewExDetails =
                                    ExerciseByWorkoutModel.Data.ExerciseData.Details(
                                        i.details.sets,
                                        i.details.sets_type,
                                        i.details.description,
                                        tmpex
                                    )
                                allData.add(
                                    ExerciseByWorkoutModel.Data.ExerciseData(
                                        i.ex_type,
                                        tmpNewExDetails
                                    )
                                )
                            }
                            workoutListAdapter.setList(allData)
                            wholeExercise = allData
                            childPos.value = 0
                            parentPos.value = 0
                            currentSet = 1
                            progressFun()
                            it.body()!!.message
                        } else if (!it.isSuccessful && it.errorBody() != null) {
                            Util.error(it.errorBody(), ExerciseByWorkoutModel::class.java).message
                        } else Toast.makeText(
                            applicationContext,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Log.d("error", e.localizedMessage.toString())
                    }

                }
            }
        }
    }

    private fun progressFun() {
        maxProgress = 0
        currentProgress = 0
        workoutListAdapter.notifyDataSetChanged()
        var data: ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise =
            try {
                wholeExercise[parentPos.value!!].details.exercise!![childPos.value!!]
            } catch (e: IndexOutOfBoundsException) {
                if (simpleExoPlayer != null) {
                    simpleExoPlayer.playWhenReady = false
                    simpleExoPlayer.stop()
                    simpleExoPlayer.seekTo(0)
                }

                val intent = Intent(applicationContext, SetupAllActivity::class.java)
                intent.putExtra(Constants.DESTINATION, Constants.FEEDBACK)
                intent.putExtra("work_id", workout_id)
                intent.putExtra("program_id", program_id)
                startActivity(intent)
                finish()
                return
                wholeExercise[parentPos.value!! - 1].details.exercise!![childPos.value!!]
            }
        binding.apply {
            if (wholeExercise[parentPos.value!!].ex_type == "rest") {
                val rest = wholeExercise[parentPos.value!!].details.sets!!.split(" ")[0].toInt()
                type = "next"
                Log.e("TAG", "progressFun: outer rest entered")
                restTimer(rest * 1000, "next")
            } else if (data.title.equals("Rest")) {
                this!!.rest.visibility = View.VISIBLE
                this.allsection.visibility = View.GONE
                this.greenBtn.visibility = View.GONE
                this.progressTimer.visibility = View.GONE
                val rest = data.rest!!.split(" ")[0].toInt()
                restTimer(rest * 1000)
                type = "rest"
            } else if (data.target_type.equals("reps")) {
                Log.e("TAG", "progressFun: ${parentPos.value} : ${childPos.value}")
                this!!.rest.visibility = View.GONE
                this.greenBtn.visibility = View.VISIBLE
                this.allsection.visibility = View.VISIBLE
                this.progressTimer.visibility = View.GONE
                this.exerciseTv.text = data.title
                totalSet.text = " $currentSet "
                if (totSet < 1)
                    totSet = data.sets?.toInt() ?: 1
                maxProgress = totSet
                currentProgress = currentSet - 1
                type = "reps"
                Log.e("TAG", "progressFun: Reps entered")
                videoUri = data.video.toString()
                if (Util.checkUrl("${Uri.parse(videoUri)}")) {
                    playerView.visibility = GONE
                    youtubePlayerView.visibility = VISIBLE
                    playYoutubeVideo()
                } else {
                    playerView.visibility = VISIBLE
                    youtubePlayerView.visibility = GONE
                    callExoPlayer2(videoUri)
                }
                greenBtn.setOnClickListener {
                    if (data.rest != null && data.rest != "null") {
                        maxProgress = totSet
                        currentProgress = currentSet
                        workoutListAdapter.notifyDataSetChanged()
                        var rest: Int
                        if (data.rest!!.split(" ")[1] == "sec") {
                            rest = data.rest!!.split(" ")[0].toInt()
                            restTimer(rest * 1000)
                        } else if (data.rest!!.split(" ")[1] == "min") {
                            rest = data.rest!!.split(" ")[0].toInt()
                            restTimer(rest * 60000)
                        }
                    } else {
                        val rest = 0
                        maxProgress = 0
                        currentProgress = 0
                        workoutListAdapter.notifyDataSetChanged()
                        restTimer(rest * 1000)
                    }
                    workoutListAdapter.notifyDataSetChanged()
                }
            } else if (data.target_type.equals("time")) {
                this!!.progressTimer.visibility = View.VISIBLE
                this.rest.visibility = View.GONE
                this.greenBtn.visibility = View.GONE
                this.allsection.visibility = View.VISIBLE
                this.exerciseTv.text = data.title
                type = "time"
                if (totSet < 1)
                    totSet = data.sets?.toInt() ?: 1

                totalSet.text = " $currentSet "
                Log.e("TAG", "progressFun: Time entered")

                if (data.target != null) {
                    if (data.target!!.split(" ")[1] == "sec") {
                        var time = data.target!!.split(" ")[0].toInt()
                        videoTime = time.toLong()
                        binding!!.timertv.text = time.toString()
                    } else if (data.target!!.split(" ")[1] == "min") {
                        var time = data.target!!.split(" ")[0].toInt()
                        videoTime = (time * 60).toLong()
                        binding!!.timertv.text = time.toString()
                    }
                } else {

                }
                videoUri = data.video.toString()
                if (Util.checkUrl("${Uri.parse(videoUri)}")) {
                    playerView.visibility = GONE
                    youtubePlayerView.visibility = VISIBLE
                    playYoutubeVideo()
                } else {
                    playerView.visibility = VISIBLE
                    youtubePlayerView.visibility = GONE
                    callExoPlayer(videoUri)
                }
                if (data.rest != null) {
                    if (data.rest!!.split(" ")[1] == "sec") {
                        videoRest = data.target!!.split(" ")[0].toInt()
                    } else if (data.target!!.split(" ")[1] == "min") {
                        videoRest = data.target!!.split(" ")[0].toInt()
                    }
                } else {
                    videoRest = 0
                }
            } else {
                this!!.greenBtn.visibility = View.VISIBLE
                type = "distance"
            }
            workoutListAdapter.notifyDataSetChanged()
        }
    }

    private fun youtubePlay() {
        lifecycle.addObserver(binding!!.youtubePlayerView)
        binding!!.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtube = youTubePlayer
                //  progressFun()
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
                if (type != "reps") {
                    var countTime = second.toLong()
                    binding!!.timerProgress.max = videoTime.toInt()
                    binding!!.timerProgress.progress = (videoTime - countTime).toInt()
                    maxProgress = totSet * videoTime.toInt()
                    currentProgress = (currentSet - 1) * videoTime.toInt() + countTime.toInt()
                    workoutListAdapter.notifyDataSetChanged()
                    binding!!.timertv.text = (videoTime - countTime).toString()
                    if (videoTime == countTime) {
                        youtube?.pause()
                        Log.e("TAG", "getCurrentPlayerPosition: Inner Rest")
                        if (type != "reps")
                            restTimer(videoRest * 1000)
                    }
                }
            }
        })
    }

    private fun playYoutubeVideo() {
        val videoId = Util.getVideoId(videoUri)
        youtube?.loadVideo(videoId, 0f)
    }

    private fun restTimer(i: Int, next: String? = null) {
        youtube?.pause()
        time = i.toLong()
        countDownTimer?.cancel()
        binding.apply {
            this!!.progressTimer.visibility = View.GONE
            this.greenBtn.visibility = View.GONE
            this.rest.visibility = View.VISIBLE
            this.allsection.visibility = View.GONE
        }
        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val f: NumberFormat = DecimalFormat("00")
                val hour = millisUntilFinished / 3600000 % 24
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                binding!!.restTv.text = f.format(min) + ":" + f.format(sec)
            }

            override fun onFinish() {
                currentSet += 1
                if (currentSet <= totSet) {
                    progressFun()
                } else {
                    if (type != null && type == "next") {
                        parentPos.value = parentPos.value!! + 1
                        childPos.value = 0
                        currentSet = 1
                    } else if (wholeExercise[parentPos.value!!].details.exercise!!.size - 1 == childPos.value) {
                        parentPos.value = parentPos.value!! + 1
                        childPos.value = 0
                        currentSet = 1
                    } else {
                        childPos.value = childPos.value!! + 1
                    }
                    totSet = 0
                    progressFun()
                }
            }
        }
        countDownTimer?.start()
    }

    private fun callExoPlayer(videoUri: String) {
        val mediaItem = MediaItem.fromUri(videoUri)
        simpleExoPlayer.addMediaItems(listOf(mediaItem))
        exoPlayerWork()
    }

    private fun callExoPlayer2(videoUri: String) {
        val mediaItem = MediaItem.fromUri(videoUri)
        simpleExoPlayer.addMediaItems(listOf(mediaItem))
        simpleExoPlayer.removeListener(listener)
        Log.e("TAG", "callExoPlayer2: CallExoPlayer2")
        exoPlayerWork2()
    }

    private fun exoPlayerWork2() {
        simpleExoPlayer.prepare()
        simpleExoPlayer.seekTo(0)
        simpleExoPlayer.play()
        simpleExoPlayer.playWhenReady = true
        Log.e("TAG", "exoPlayerWork2: ExoPlayerWork2")
    }

    private fun exoPlayerWork() {
        simpleExoPlayer.prepare()
        simpleExoPlayer.seekTo(0)
        simpleExoPlayer.play()
        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.addListener(listener)
    }

    private val listener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            if (state == ExoPlayer.STATE_ENDED) {

            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            if (type != "reps") playerView.postDelayed(this::getCurrentPlayerPosition, 1000)
        }

        private fun getCurrentPlayerPosition() {
            if (type != "reps") {
                var countTime = (simpleExoPlayer.currentPosition / 1000)
                binding!!.timerProgress.max = videoTime.toInt()
                binding!!.timerProgress.progress = (videoTime - countTime).toInt()
                maxProgress = totSet * videoTime.toInt()
                currentProgress = (currentSet - 1) * videoTime.toInt() + countTime.toInt()
                Log.e("TAG", "getCurrentPlayerPosition: $maxProgress $currentProgress")
                workoutListAdapter.notifyDataSetChanged()
                binding!!.timertv.text = (videoTime - countTime).toString()
                if (videoTime == countTime) {
                    simpleExoPlayer.pause()
                    Log.e("TAG", "getCurrentPlayerPosition: Inner Rest")
                    if (type != "reps")
                        restTimer(videoRest * 1000)
                }
                if (simpleExoPlayer.isPlaying) {
                    playerView.postDelayed(this::getCurrentPlayerPosition, 1000)
                }
            }
        }
    }

    inner class WorkoutListAdapter(
        val activity: Activity,
        val listener: SelectVideoData
    ) : RecyclerView.Adapter<WorkoutListAdapter.ViewHolder>() {

        private var list = ArrayList<ExerciseByWorkoutModel.Data.ExerciseData>()

        lateinit var exerciseListAdapter: ExerciseListAdapter

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

        inner class ViewHolder(val binding: WorkoutListBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(data: ExerciseByWorkoutModel.Data.ExerciseData, position: Int) {
                with(binding) {
                    tvTitle.text =
                        if (data.details.sets_type.equals("time") || data.details.sets_type.equals(
                                null
                            )
                        )
                            "${data.ex_type.uppercase(Locale.getDefault())} " + capitalize(data.details.sets.toString())
                        else if (data.details.sets_type.equals("reps"))
                            "${data.ex_type.uppercase(Locale.getDefault())} X " + capitalize(data.details.sets.toString()) + " Sets"
                        else if (data.details.sets_type.equals("distance"))
                            "${data.ex_type.uppercase(Locale.getDefault())} " + capitalize(data.details.sets.toString())
                        else ""

                    if (data.ex_type.equals("rest", true)) {
                        llRest.visibility = View.VISIBLE
                        llNonRest.visibility = View.GONE
                        tvRest.text = "Rest ${data.details.sets}"
                        tvTitle2.text =
                            "${data.ex_type.uppercase(Locale.getDefault())} " + capitalize(data.details.sets!!)

                    } else {
                        llRest.visibility = View.GONE
                        llNonRest.visibility = View.VISIBLE
                    }

                    tvDescription.text = data.details.description

                    llRest.setOnClickListener {
                        val rest = data.details.sets!!.split(" ")[0].toInt()
                        type = "next"
                        restTimer(rest * 1000, "next")
                        parentPos.value = position
                        childPos.value = 0
                        currentSet = 1
                        totSet = 0
                        workoutListAdapter.notifyDataSetChanged()
                    }

                    exerciseListAdapter = ExerciseListAdapter(
                        position,
                        activity,
                        data
                    )
                    exerciseRv.adapter = exerciseListAdapter
                    if (data.ex_type.equals("normal", true)) {
                        tvTitle.visibility = View.GONE
                        tvDescription.visibility = View.GONE

                    }
                }
            }
        }

        fun setList(list: List<ExerciseByWorkoutModel.Data.ExerciseData>) {
            this.list = list as ArrayList<ExerciseByWorkoutModel.Data.ExerciseData>
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = WorkoutListBinding.inflate(inflater, parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(list[position], position)

        override fun getItemCount(): Int {
            return try {
                list.size
            } catch (e: Exception) {
                0
            }
        }
    }

    inner class ExerciseListAdapter(
        private val positionP: Int,
        private val activity: Activity,
        private val data: ExerciseByWorkoutModel.Data.ExerciseData

    ) : RecyclerView.Adapter<ExerciseListAdapter.ViewHolder>() {

        var itemPos = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(WarmupListBinding.inflate(activity.layoutInflater, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(data.details.exercise!![position], position)
        }

        override fun getItemCount(): Int {
            return try {
                data.details.exercise!!.size
            } catch (e: Exception) {
                0
            }
        }

        inner class ViewHolder(private val binding: WarmupListBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(
                exerciseData: ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise,
                pos: Int
            ) {
                binding.apply {
                    titleTv.text = exerciseData.title
                    tvDescription.text = if (data.ex_type.equals(
                            "normal",
                            true
                        )
                    ) data.details.description else exerciseData.description

                    tvTargetText.text = exerciseData.target_text

                    repDuration.text =
                        if (exerciseData.target_type.equals("time", true)) {
                            "${exerciseData.sets ?: 1} * ${exerciseData.target}"
                        } else if (exerciseData.target_type.equals("reps", true)) {
                            "${exerciseData.sets ?: 1} * ${exerciseData.sets ?: 1} ${exerciseData.target_type}"
                        } else if (exerciseData.target_type.equals("distance", true)) {
                            "${exerciseData.target}"
                        } else ""
                    Util.loadImage(activity, imageView, exerciseData.banner)
                    if (data.ex_type.equals("normal", true)) {
                        tvTargetText.text = ""
                    }
                    progreBarHorizontal.visibility =
                        if (parentPos.value!! == positionP && childPos.value!! == pos) View.VISIBLE else View.GONE

                    progreBarHorizontal.max = maxProgress
                    progreBarHorizontal.progress = currentProgress

                    binding.root.setOnClickListener {
                        childPos.value = pos
                        parentPos.value = positionP
                        currentSet = 1
                        totSet = 0
                        workoutListAdapter.notifyDataSetChanged()
                        progressFun()
                    }
                }
            }
        }
    }
}

interface SelectVideoData {
    fun onClick(
        data: ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise,
        pos: Int,
        itemView: View
    )
}
