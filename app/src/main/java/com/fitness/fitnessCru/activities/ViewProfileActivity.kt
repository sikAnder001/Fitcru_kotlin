package com.fitness.fitnessCru.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivityViewProfileBinding
import com.fitness.fitnessCru.quickbox.utils.SharedPrefsHelper
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.*
import com.fitness.fitnessCru.viewmodel.DeactivateAccountViewModel
import com.fitness.fitnessCru.viewmodel.UserDetailsViewModel
import com.fitness.fitnessCru.vm_factory.DeactivateAccountVMFactory
import com.fitness.fitnessCru.vm_factory.UserDetailsVMFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.orhanobut.hawk.Hawk

class ViewProfileActivity : AppCompatActivity() {
    private lateinit var viewProfileBinding: ActivityViewProfileBinding

    private lateinit var loading: CustomProgressLoading

    private lateinit var repository: Repository

    private lateinit var viewModel: DeactivateAccountViewModel

    private lateinit var factory: DeactivateAccountVMFactory


    private val binding get() = viewProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_profile)

        loading = CustomProgressLoading(applicationContext)

        repository = Repository()

        factory = DeactivateAccountVMFactory(repository)

        viewModel = ViewModelProvider(this, factory)[DeactivateAccountViewModel::class.java]

        deactivateAccount()

        deleteAccount()

        logout()
        //  Hawk.put(ISLOGIN,"isLogin")
        //  Hawk.get(ISLOGIN)


        if (Hawk.contains(ISLOGIN)) {
            binding.deactivateAccountContainer.visibility = View.GONE

            binding.deleteAccountContainer.visibility = View.GONE

        } else {
            binding.deactivateAccountContainer.visibility = View.VISIBLE

            binding.deleteAccountContainer.visibility = View.VISIBLE


        }



        getDetails()

        val key = intent.getStringExtra("key")
        if (key != null) {
            binding.backBtn.setOnClickListener {
                val dashboardIntent = Intent(this, DashboardActivity::class.java)
                startActivity(dashboardIntent)
                finish()
            }
        } else {
            binding.backBtn.setOnClickListener {
                onBackPressed()
                finish()
            }
        }

        try {
            binding.userName.text = Session.getUserDetails().name
            Util.loadImage(
                applicationContext,
                binding.profileImg,
                Session.getUserDetails().image_url
            )
            //binding.weight.text = Session.getUserDetails().questionnaire.weight
        } catch (e: Exception) {
            binding.userName.text = "Your Profile"
            Util.loadImage(
                applicationContext,
                binding.profileImg, ""
            )
        }
        binding.llAppointment.setOnClickListener {
            val intent = Intent(applicationContext, SetupAllActivity::class.java)
            intent.putExtra(Constants.DESTINATION, Constants.APPOINTMENT)
            startActivity(intent)
            finish()
        }

        binding.llMyInsight.setOnClickListener {
            val intent = Intent(applicationContext, SetupAllActivity::class.java)
            intent.putExtra(Constants.DESTINATION, Constants.INSIGHTS)
            intent.putExtra("tab_id", "0")
            startActivity(intent)
            finish()
        }

        binding.editProfile.setOnClickListener {

            val intent = Intent(applicationContext, EditProfileActivity::class.java)
            intent.putExtra("view_p", "1")
            /*  startActivity(
                  Intent(
                      applicationContext, EditProfileActivity::class.java

                  )
              ) finish()*/
            startActivity(intent)
            finish()

        }

        binding.llMyOrders.setOnClickListener {
            val intent = Intent(applicationContext, SetupAllActivity::class.java)
            intent.putExtra(Constants.DESTINATION, Constants.MY_ORDERS)
            startActivity(intent)
            finish()
        }

        binding.termsAndConditionsContainer.setOnClickListener {
            val termsIntent = Intent(applicationContext, TermsAndConditionsActivity::class.java)
            startActivity(termsIntent)
        }

        binding.sixContainer.setOnClickListener {
            val termsIntent = Intent(applicationContext, LinkDevicesActivity::class.java)
            startActivity(termsIntent)
        }

        binding.privacyContainer.setOnClickListener {
            val privacyIntent = Intent(applicationContext, PrivacyPolicyActivity::class.java)
            startActivity(privacyIntent)
        }

        binding.helpSupportContainer.setOnClickListener {
            Util.sendMessage(applicationContext, "Hello")
        }
    }

    private fun getDetails() {

        val repository by lazy { Repository() }
        val factory by lazy { UserDetailsVMFactory(repository) }
        val viewModel by lazy {
            ViewModelProvider(
                this,
                factory
            ).get(UserDetailsViewModel::class.java)
        }
        loading.showProgress()
        viewModel.getUserDetails()
        viewModel.response.observe(this) {
            loading.hideProgress()
            if (it.isSuccessful && it.code() == 200) {
                binding?.apply {
                    val data = it.body()!!.data
                    userName.text = data.name
                    weight.text = data?.questionnaire?.weight ?: ""
                    Util.loadImage(
                        applicationContext,
                        binding.profileImg,
                        data.image_url
                    )
                    bmi.text =
                        if (data.bmi_calculation == null) "" else data.bmi_calculation?.bmi.toString()
                }
            } else {
                Toast.makeText(applicationContext, "Error: Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun deactivateAccount() {
        binding.apply {

            deactivateAccountContainer.setOnClickListener {
                val dialogBuilder =
                    AlertDialog.Builder(this@ViewProfileActivity, R.style.AlertDialogStyle)

                dialogBuilder.setMessage("Do you want to deactivate your account?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                        viewModel.deactivateAccount()
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })

                val alert = dialogBuilder.create()
                alert.setTitle("Deactivate Account")
                alert.show()
            }
        }
        viewModel.deactivateAccount.observe(this) {
            loading.hideProgress()
            if (it.isSuccessful && it.code() == 200) {
                Session.destroySession()
                Hawk.deleteAll()
                intent = Intent(this@ViewProfileActivity, SplashActivity::class.java)
                startActivity(intent)
                finishAffinity()
            } else {
                Toast.makeText(applicationContext, "Error: Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun deleteAccount() {
        binding.apply {

            deleteAccountContainer.setOnClickListener {
                val dialogBuilder =
                    AlertDialog.Builder(this@ViewProfileActivity, R.style.AlertDialogStyle)

                dialogBuilder.setMessage("Do you want to delete your account?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                        viewModel.deleteAccount()
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })

                val alert = dialogBuilder.create()
                alert.setTitle("Delete Account")
                alert.show()
            }
        }
        viewModel.delete.observe(this) {
            loading.hideProgress()
            if (it.isSuccessful && it.code() == 200) {
                Session.destroySession()
                Hawk.deleteAll()
                intent = Intent(this@ViewProfileActivity, SplashActivity::class.java)
                startActivity(intent)
                finishAffinity()
            } else {
                Toast.makeText(applicationContext, "Error: Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun logout() {
        binding.apply {

            logoutContainer.setOnClickListener {
                val dialogBuilder =
                    AlertDialog.Builder(this@ViewProfileActivity, R.style.AlertDialogStyle)

                dialogBuilder.setMessage("Do you want to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                        viewModel.logout()
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                        dialog.cancel()
                    })

                val alert = dialogBuilder.create()
                alert.setTitle("Logout")
                alert.show()
            }
        }
        viewModel.logout.observe(this) {
            if (it.isSuccessful && it.code() == 200) {
                //  showToast("NewDeleted")

                Fitness.getConfigClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
                    .disableFit()

                SharedPreferenceUtil.getInstance(this).isLogin = false
                Session.destroySession()
                Hawk.deleteAll()
                SharedPrefsHelper.clearAllData()


                //


                intent = Intent(this@ViewProfileActivity, OpponentsActivity::class.java)
                intent.putExtra("logout", 1)
                //logoutShared()
                startActivity(intent)

            } else {

                Fitness.getConfigClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
                    .disableFit()

                val fitnessOptions = FitnessOptions.builder()
                    .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                    .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                    .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                    .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                    .build()
                val signInOptions =
                    GoogleSignInOptions.Builder().addExtension(fitnessOptions).build()
                val client = GoogleSignIn.getClient(applicationContext, signInOptions)
                client.revokeAccess()

                SharedPreferenceUtil.getInstance(this).isLogin = false
                Session.destroySession()
                Hawk.deleteAll()
                SharedPrefsHelper.clearAllData()


                //


                intent = Intent(this@ViewProfileActivity, OpponentsActivity::class.java)
                intent.putExtra("logout", 1)
                //logoutShared()
                startActivity(intent)


            }
        }
    }


    /* private fun unsubscribeFromPushes(callback: () -> Unit) {
         if (QBPushManager.getInstance().isSubscribedToPushes) {
             QBPushManager.getInstance().addListener(SubscribeListener(TAG, callback))
             SubscribeService.unSubscribeFromPushes(this@ViewProfileActivity)
         } else {
             callback()
         }
     }*/
}