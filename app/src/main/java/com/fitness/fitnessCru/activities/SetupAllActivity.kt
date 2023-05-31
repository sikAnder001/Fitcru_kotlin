package com.fitness.fitnessCru.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivityNutritionSetupBinding
import com.fitness.fitnessCru.utility.Constants

class SetupAllActivity : AppCompatActivity() {

    private lateinit var nutritionSetupBinding: ActivityNutritionSetupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nutritionSetupBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_nutrition_setup)
        val navController: NavController =
            Navigation.findNavController(this, R.id.nutrition_setup_fragment_container)
        when (intent?.getStringExtra(Constants.DESTINATION)) {

            Constants.WORKOUT -> navController.navigate(R.id.yogaFragment)
            Constants.WORKOUT2 -> {
                var bundle = Bundle()
                var id = intent.getIntExtra("id", 0)
                var workout = intent.getIntExtra("workout_id", 0)
                var program = intent.getIntExtra("program_id", 0)
                bundle.apply {
                    putInt("id", id)
                    putInt("workout_id", workout)
                    putInt("program_id", program)
                }
                navController.navigate(R.id.action_addMealFragment_to_sncProgramFragment, bundle)
            }

            Constants.RECIPE -> {
                var bundle = Bundle()
                bundle.putInt("id", intent.getIntExtra("id", 0))
                navController.navigate(R.id.action_addMealFragment_to_recipe, bundle)
            }

            Constants.STUDIO_DETAILS -> {
                var bundle = Bundle()
                bundle.putInt("id", intent.getIntExtra("id", 0))
                bundle.putString("title", intent.getStringExtra("title"))
                navController.navigate(R.id.action_addMealFragment_to_studio_details, bundle)
            }

            Constants.WORKOUT_BY_FITNESS -> {
                var bundle = Bundle()
                bundle.putInt("id", intent.getIntExtra("id", 0))
                bundle.putString("title", intent.getStringExtra("title"))
                navController.navigate(
                    R.id.action_addMealFragment_to_workoutByFitnessFragment,
                    bundle
                )
            }

            Constants.ADD_MEAL -> {
                val bundle = Bundle()
                bundle.putInt("meal_id", intent.getIntExtra("meal_id", 0))
                bundle.putString("time", intent.getStringExtra("time"))
                bundle.putString("name", intent.getStringExtra("name"))
                bundle.putString("date", intent.getStringExtra("date"))
                bundle.putString("mealType", intent.getStringExtra("mealType"))
                if (intent.getIntExtra("meal_id", 0) > 0)
                    navController.navigate(R.id.action_addMealFragment_to_addMealFragment2, bundle)
            }

            Constants.SCHEDULE_APPOINTMENT -> {
                val bundle = Bundle()
                bundle.putSerializable("data", intent.getSerializableExtra("data"))
                navController.navigate(R.id.action_addMealFragment_to_appointmentFragment, bundle)
            }

            Constants.MAIN_COACHING -> navController.navigate(R.id.action_addMealFragment_to_coaching2)

            Constants.CHECKOUT -> navController.navigate(R.id.action_addMealFragment_to_checkoutFragment)

            Constants.APPOINTMENT -> navController.navigate(R.id.action_addMealFragment_to_upcomingFragment)

            Constants.NOTIFICATION_LIST -> navController.navigate(R.id.action_addMealFragment_to_notificationList)

            Constants.SELECT_COACH -> {
                var bundle = Bundle()
                bundle.putInt("planId", intent.getIntExtra("planId", 0))
                bundle.putString("num", intent.getStringExtra("num"))

                navController.navigate(
                    R.id.action_addMealFragment_to_selectCoachTypeFragment,
                    bundle
                )
            }

            Constants.COACH_PLAN_DETAIL -> {
                var bundle = Bundle()
                bundle.putInt("coachId", intent.getIntExtra("coachId", 0))
                bundle.putInt("planId", intent.getIntExtra("planId", 0))
                bundle.putInt("catId", intent.getIntExtra("catId", 0))
                bundle.putString("coachName", intent.getStringExtra("coachName"))
                bundle.putString("num", intent.getStringExtra("num"))

                navController.navigate(
                    R.id.action_addMealFragment_to_coachPlanDetailFragment,
                    bundle
                )
            }
            Constants.INSIGHTS -> {
                val bundle = Bundle()
                bundle.putString("tab_id", intent.getStringExtra("tab_id"))
                navController.navigate(R.id.action_addMealFragment_to_insightFragment, bundle)
            }

            Constants.MY_ORDERS -> navController.navigate(R.id.action_addMealFragment_to_myOrdersFragment)

            Constants.GO_TO_HELL -> {
                val bundle = Bundle()
                bundle.putString("num", intent.getStringExtra("num"))
                navController.navigate(
                    R.id.action_addMealFragment_to_healthQuestionnaireFragment1,
                    bundle
                )
            }
            /*Constants.GO_TO_HEALTH_PAGE ->{
                var bundle = Bundle()
                var healthy = intent.getStringExtra("health")
                navController.navigate(R.id.action_addMealFragment_to_healthQuestionnaireFragment1,bundle)
            }*/

            Constants.COACHING -> {
                val bundle = Bundle()
                bundle.putSerializable("data", intent.getSerializableExtra("data"))
                navController.navigate(
                    R.id.action_addMealFragment_to_planSelectTrainer,
                    bundle
                )
            }

            Constants.COACH_PROFILE -> {
                val bundle = Bundle()
                bundle.putInt("coachId", intent.getIntExtra("coachId", 0))
                bundle.putInt("planId", intent.getIntExtra("planId", 0))
                bundle.putInt("catId", intent.getIntExtra("catId", 0))
                bundle.putString("coachName", intent.getStringExtra("coachName"))
                navController.navigate(
                    R.id.action_addMealFragment_to_trainerProfileFragment,
                    bundle
                )
            }

            Constants.HOME_TO_WORKOUT -> {
                val bundle = Bundle()
                bundle.putInt("workout_id", intent.getIntExtra("workout_id", 0))
                bundle.putInt("program_id", intent.getIntExtra("program_id", 0))
                navController.navigate(
                    R.id.action_addMealFragment_to_sncSessionFragment,
                    bundle
                )
            }

            Constants.FEEDBACK -> {
                var bundle = Bundle()
                var workout = intent.getIntExtra("work_id", 0)
                var program_id = intent.getIntExtra("program_id", 0)
                bundle.apply {
                    putInt("work_id", workout)
                    putInt("program_id", program_id)
                }
                navController.navigate(
                    R.id.action_addMealFragment_to_sessionCompletedFragment,
                    bundle
                )
            }

            Constants.LAB_TEST -> navController.navigate(R.id.action_addMealFragment_to_labTestFragment)
        }
    }
}