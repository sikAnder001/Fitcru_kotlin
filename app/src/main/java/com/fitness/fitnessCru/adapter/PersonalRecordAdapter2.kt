package com.fitness.fitnessCru.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.PersonalRecordRvItemBinding
import com.fitness.fitnessCru.interfaces.FeedbackInterface
import com.fitness.fitnessCru.model.ExerciseByWorkoutModel

class PersonalRecordAdapter2(
    var context: Context?,
    private val feedbackInterface: FeedbackInterface
) : RecyclerView.Adapter<PersonalRecordAdapter2.ViewHolder>() {

    var list = ArrayList<ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise>()

    inner class ViewHolder(personalRecordBinding: PersonalRecordRvItemBinding) :
        RecyclerView.ViewHolder(personalRecordBinding.root) {
        var personalRecordBinding = personalRecordBinding

        fun bind(
            exerciseData: ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise,
            position: Int
        ) {
            personalRecordBinding.apply {

                var rep = 0

                var weight = 0

                repsPlusIcon.setOnClickListener {
                    (totalReps as TextView).text = (++rep).toString()

                }

                repsMinusIcon.setOnClickListener {
                    if (rep != 0) {
                        (totalReps as TextView).text = (--rep).toString()
                    }
                }

                weightMinusIcon.setOnClickListener {
                    if (weight != 0) {
                        totalWeight.text = (--weight).toString()
                    }
                }

                weightPlusIcon.setOnClickListener {
                    totalWeight.text = (++weight).toString()
                }

                tvTitle.text = exerciseData.title

                if (exerciseData.title.equals("Rest")) {
                    itemView.visibility = View.GONE
                } else {
                    itemView.visibility = View.VISIBLE
                }

                tvDescription.text = exerciseData.description

                var fullReps = ""
                var fullTime = ""

                if (exerciseData.target_type.equals("reps")) {
                    repsTv.text = "Reps"

                } else if (exerciseData.target_type.equals("time")) {
                    repsTv.text = "Time(sec)"

                } else repsTv.text = ""

                if (exerciseData.weights.equals("yes")) {
                    weightItemContainer.visibility = View.VISIBLE
                } else {
                    weightItemContainer.visibility = View.GONE
                }

                totalReps.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (repsTv.text.equals("Reps")) {
                            fullReps = "${totalReps.text}reps"
                            fullTime = ""
                        } else {
                            fullTime = "${totalReps.text} sec"
                            fullReps = ""
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {
                        itemView.performClick()
                    }
                })

                var fullWeight = ""

                totalWeight.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        fullWeight = "${totalWeight.text}kg"
                    }

                    override fun afterTextChanged(p0: Editable?) {
                        itemView.performClick()
                    }
                })

                itemView.setOnClickListener {
                    feedbackInterface.onFeedbackClick(
                        position,
                        exerciseData.exercise_id,
                        exerciseData.title.toString(),
                        exerciseData.description.toString(),
                        fullWeight,
                        fullTime,
                        fullReps
                    )
                }
            }
        }
    }

    fun stylist(list: ArrayList<ExerciseByWorkoutModel.Data.ExerciseData.Details.Exercise>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersonalRecordAdapter2.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val personalRecordBinding = PersonalRecordRvItemBinding.inflate(inflater, parent, false)
        return ViewHolder(personalRecordBinding)
    }

    override fun onBindViewHolder(holder: PersonalRecordAdapter2.ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return try {
            list.size
        } catch (e: Exception) {
            0
        }
    }
}
