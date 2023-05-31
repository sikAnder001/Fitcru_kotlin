package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.adapter.ExerciseListAdapter2
import com.fitness.fitnessCru.databinding.ActivityWhiteBoardTwoBinding
import com.fitness.fitnessCru.model.ExerciseByWorkoutModel
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.WorkoutExersiseViewModel
import com.fitness.fitnessCru.vm_factory.WorkoutExersiseVMFactory

class
WhiteBoardTwoActivity : AppCompatActivity() {

    private var binding: ActivityWhiteBoardTwoBinding? = null

    lateinit var exerciseListAdapter2: ExerciseListAdapter2
    lateinit var allData:
            ArrayList<ExerciseByWorkoutModel.Data.ExerciseData>

    private lateinit var repository: Repository
    private lateinit var viewModel: WorkoutExersiseViewModel
    private lateinit var factory: WorkoutExersiseVMFactory

    var newData = ArrayList<ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise>()

    private var workout_id = 0
    private var program_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhiteBoardTwoBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        repository = Repository()

        factory = WorkoutExersiseVMFactory(repository)

        viewModel = ViewModelProvider(
            this@WhiteBoardTwoActivity,
            factory
        ).get(WorkoutExersiseViewModel::class.java)

        binding!!.gobackbtn.setOnClickListener {
            onBackPressed()
        }

        recycleWarmUp()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        exerciseListAdapter2.simpleExoPlayer!!.stop()
    }

    private fun recycleWarmUp() {
        exerciseListAdapter2 = ExerciseListAdapter2(
            this@WhiteBoardTwoActivity,
            object : ExerciseListAdapter2.SelectVideoData {
                override fun onClick(
                    data: ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise,
                    pos: Int,
                    exType: String?,
                ) {
                    callIntent(data, exType)
                }
            })

        binding!!.rvWorkoutList.adapter = exerciseListAdapter2

        workout_id = intent.extras!!.getInt("work_id")
        program_id = intent.extras!!.getInt("program_id")

        viewModel.getExercise(workout_id)

        viewModel.response.observe(this@WhiteBoardTwoActivity) {
            try {

                if (it.isSuccessful && it.code() == 200 && it.body() != null) {

                    var data = it.body()!!.data

                    allData = ArrayList()
                    newData = ArrayList()

                    for (i in data.exercises_data) {
                        var match1: String? = null
                        var match2: String? = i.ex_type
                        var tmpex =
                            arrayListOf<ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise>()

                        var set: Int
                        try {
                            set = i.details?.sets?.toInt()!!
                        } catch (e: Exception) {
                            set = 1
                        }
                        for (toy in 0 until set) {
                            var arrex = i.details?.exercise
                            if (arrex != null) {
                                for (exerciseItem in arrex) {
                                    if (match2 != match1) {
                                        match1 = match2
                                        tmpex.add(exerciseItem)
                                        newData.add(
                                            ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise(
                                                exerciseItem.exercise_id,
                                                exerciseItem.exercizetype_id,
                                                exerciseItem.banner,
                                                exerciseItem.video,
                                                exerciseItem.title,
                                                exerciseItem.target,
                                                exerciseItem.target_text,
                                                exerciseItem.rest,
                                                exerciseItem.sets,
                                                exerciseItem.main_rest,
                                                exerciseItem.target_type,
                                                exerciseItem.description,
                                                exerciseItem.weights,
                                                exerciseItem.fullWeight,
                                                exerciseItem.fullTime,
                                                exerciseItem.fullReps,
                                                i.ex_type,
                                                i.details.description,
                                                i.ex_type,
                                                i.details.sets_type,
                                                i.details.sets

                                            )
                                        )
                                    } else {
                                        tmpex.add(exerciseItem)
                                        newData.add(
                                            ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise(
                                                exerciseItem.exercise_id,
                                                exerciseItem.exercizetype_id,
                                                exerciseItem.banner,
                                                exerciseItem.video,
                                                exerciseItem.title,
                                                exerciseItem.target,
                                                exerciseItem.target_text,
                                                exerciseItem.rest,
                                                exerciseItem.sets,
                                                exerciseItem.main_rest,
                                                exerciseItem.target_type,
                                                exerciseItem.description,
                                                exerciseItem.weights,
                                                exerciseItem.fullWeight,
                                                exerciseItem.fullTime,
                                                exerciseItem.fullReps,
                                                null,
                                                null,
                                                i.ex_type,
                                                null,
                                                null
                                            )
                                        )
                                    }
                                }
                            } else {
//                                continue
                                newData.add(
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
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
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

                    exerciseListAdapter2.stylist(newData)

                    Log.v("newALL", newData.toString())
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

    private fun callIntent(
        data: ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise,
        exType: String?,
    ) {
        /*var lastindexdata = allData.last().details.exercise!!.last().exercise_id.toString()
        var exType=exType*/
        var lastExerciseId = newData.last().exercise_id
        var lastExType = newData.last().ex_type
        var exType = exType


        if (lastExerciseId == data.exercise_id.toString() && lastExType == exType) {
            val intent = Intent(applicationContext, SetupAllActivity::class.java)
            intent.putExtra(Constants.DESTINATION, Constants.FEEDBACK)
            intent.putExtra("work_id", workout_id)
            intent.putExtra("program_id", program_id)
            startActivity(intent)
        }
    }

}