package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.databinding.ActivityRazorPaymentBinding
import com.fitness.fitnessCru.model.CoachSlabResponse
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.GradeAndSpecialResponse
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.CoachCategoryViewModel
import com.fitness.fitnessCru.viewmodel.HealthQuestionViewModel
import com.fitness.fitnessCru.viewmodel.UserDetailsViewModel
import com.fitness.fitnessCru.vm_factory.CoachCategoryVMFactory
import com.fitness.fitnessCru.vm_factory.HealthQuestionVMFactory
import com.fitness.fitnessCru.vm_factory.UserDetailsVMFactory
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

class RazorPaymentActivity : AppCompatActivity(), PaymentResultWithDataListener {

    private lateinit var binding: ActivityRazorPaymentBinding

    private lateinit var repository: Repository
    private lateinit var viewModel: CoachCategoryViewModel
    private lateinit var factory: CoachCategoryVMFactory

    private lateinit var viewModel2: HealthQuestionViewModel

    private lateinit var loading: CustomProgressLoading

    private var coachId = 0
    private var catId = 0
    private var planId = 0
    private var price = 0
    private var duration = 0
    private var num = ""

    private var name = ""
    private var orderId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  setContentView(R.layout.activity_razor_payment)
        binding = ActivityRazorPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = CustomProgressLoading(this)
        repository = Repository()

        loading.showProgress()

        factory = CoachCategoryVMFactory(repository)

        var userDetail = Session.getUserDetails()

        viewModel = ViewModelProvider(this, factory).get(CoachCategoryViewModel::class.java)


        val razorpayKey = "rzp_test_Xe9xjU099OvbEo"
        // "rzp_live_36R7V6Vo63m7XL" //Generate your razorpay key from Settings-> API Keys-> copy Key Id


        val chackout = Checkout()
        chackout.setKeyID(razorpayKey)


        val password = "fDUHdCWCM4uDbB0okneJHeun"
        //"wegG5jhKDhuPCP3TXOVNJI03"


        val credentials: String = razorpayKey + ":" + password

        val basic = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        val convertedAmount = intent.extras!!.getInt("amount", 0) * 100

        name = intent.extras?.getString("productName", "").toString()
        num = intent.extras?.getString("num").toString()

        Log.v("numaste", num.toString())
        val options = JSONObject()
        options.put("name", name)
        options.put("currency", "INR")
        options.put("amount", convertedAmount)

        val prefill = JSONObject()
        if (userDetail.email != null)
            prefill.put("email", userDetail.email)

        if (userDetail.phone_number != null)
            prefill.put("contact", userDetail.phone_number)

        options.put("prefill", prefill)

        viewModel.payment(basic, "INR", convertedAmount)
//        loading.showProgress()
        viewModel.payment.observe(this@RazorPaymentActivity) {
//            loading.hideProgress()
            try {
                orderId = it.body()!!.id.toString()
                Log.v("Order_id", it.body()!!.id.toString())
                if (it.isSuccessful && it.body()!!.id != null) {
                    chackout.open(this, options)
                }
            } catch (e: Exception) {
                Util.toast(applicationContext, "Error : ${e.message}")
                finish()
            }
        }

        Log.e("TAG", "onCreate: ${convertedAmount.toString()}")

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPaymentSuccess(razorpayPaymentID: String, paymentData: PaymentData?) {
        val status: String? = null
//        val feeId = intent.extras!!.getInt("feeId", 0)

        val programId = intent.extras!!.getInt("programId", 0)

        var program = intent.extras!!.getInt("program", 0)
        if (program == 1) {
            var programAmount = intent.extras!!.getInt("amount", 0)

            programAmount /= 100

            viewModel.buyNowProgram(
                programId,
                razorpayPaymentID,
                programAmount.toString()
            )
            Toast.makeText(this, "${programAmount},${programId}", Toast.LENGTH_SHORT).show()

            loading.showProgress()

            viewModel.buyNowProgram.observe(this@RazorPaymentActivity) {

//                loading.hideProgress()

                try {
                    if (it.isSuccessful && it.body()!! != null) {
                        Toast.makeText(applicationContext, it.body()!!.message, Toast.LENGTH_SHORT)
                            .show()
                        showButton(null, planId)
                    }
                } catch (e: Exception) {
                    Util.toast(applicationContext, "Error : ${e.message}")
                    finish()
                }
            }
        } else {
            /* if (feeId != null) {

                 viewModel.payNowCoach(
                     feeId,
                     razorpayPaymentID,
                     (intent.extras!!.getInt("amount", 0) / 100),
                     intent.extras!!.getInt("planId", 0)
                 )

                 loading.showProgress()
                 viewModel.payCoach.observe(this@RazorPaymentActivity) {
                     loading.hideProgress()
                     try {
                         if (it.isSuccessful && it.body()!! != null) {
                             Toast.makeText(
                                 applicationContext,
                                 it.body()!!.message,
                                 Toast.LENGTH_SHORT
                             ).show()
                             showButton("success")
                         }
                     } catch (e: Exception) {
                         Util.toast(applicationContext, "Error : ${e.message}")
                         finish()
                     }
                 }
             }*/

            price = intent.extras?.getInt("amount", 0)!!
            planId = intent.extras?.getInt("planId", 0)!!
            coachId = intent.extras?.getInt("coachId", 0)!!
            catId = intent.extras?.getInt("catId", 0)!!
            duration = intent.extras?.getInt("duration", 0)!!

            if (planId == 3 || planId == 4) {
                viewModel.userSelectedPlan(planId, coachId, price, catId, duration, orderId)
            } else {
                viewModel.userSelectedPlan(planId, null, price, null, duration, orderId)
            }
            viewModel.userSelectedPlan.observe(this) {

                if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0) {
                    val data = it.body() as GradeAndSpecialResponse
                    getDetails()
                    Toast.makeText(
                        applicationContext,
                        it.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    showButton("success", planId)
                } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                    Util.toast(
                        applicationContext, it.body()!!.message
                    )
                } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                    applicationContext,
                    Util.error(it.errorBody(), CoachSlabResponse::class.java).message
                )
            }

        }

        /*    binding.error.visibility = View.VISIBLE
            binding.status.text = "Payment Successful"
            binding.img.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.success_icon));
            binding.errordetail.text = "Payment ID: ${razorpayPaymentID}"

            binding.error.setBackgroundColor(getColor(R.color.green_400))*/
        //API need to call here
    }

    private fun showButton(s: String?, planId: Int) {
        val repository by lazy { Repository() }
        val factory by lazy { HealthQuestionVMFactory(repository) }
        viewModel2 = ViewModelProvider(this, factory).get(HealthQuestionViewModel::class.java)
        var ss = s
        if (ss != null) {

            if (planId == 2) startActivity(
                Intent(
                    applicationContext,
                    DashboardActivity::class.java
                )
            )
            else viewModel2.getHealthQuestion()

            viewModel2.responseOne.observe(this) {

                if (it.isSuccessful && it.code() == 200 && it.body() != null) {
                    binding.apply {
                        try {
                            if (it.body()!!.data != null) {
                                if (num == "done") {
                                    val intent =
                                        Intent(applicationContext, DashboardActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    val intent =
                                        Intent(applicationContext, DashboardActivity::class.java)
                                    intent.putExtra("coaching", "done")
                                    startActivity(intent)
                                }
                            } else {
                                val intent =
                                    Intent(applicationContext, SetupAllActivity::class.java)
                                intent.putExtra(Constants.DESTINATION, Constants.GO_TO_HELL)
                                intent.putExtra("num", num)
                                startActivity(intent)
                                finishAffinity()
                            }
                        } catch (e: Exception) {
                            Log.e(Constants.TAG, "getAllquestionAnswer: $e")
                        }
                    }

                }
            }
            ss = null
        } else {
            finish()
        }
    }

    private fun getDetails() {

        val repository by lazy { Repository() }
        val factory by lazy { UserDetailsVMFactory(repository) }
        val viewModel by lazy {
            ViewModelProvider(
                this,
                factory
            )[UserDetailsViewModel::class.java]
        }
        loading.showProgress()
        viewModel.getUserDetails()
        viewModel.response.observe(this@RazorPaymentActivity) {
//            loading.hideProgress()
            if (it.isSuccessful && it.code() == 200) {
                val data = it.body()!!.data

                Session.setUserDetails(data)

            } else {
                Toast.makeText(applicationContext, "Error: Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


    override fun onPaymentError(p0: Int, message: String?, p2: PaymentData?) {
        Toast.makeText(
            applicationContext,
            "Transaction Failed" /*+ message.toString()[1]*/,
            Toast.LENGTH_LONG
        ).show()
        finish()
/*        binding.error.visibility = View.VISIBLE
        when (p0) {
            Checkout.NETWORK_ERROR -> binding.errordetail.text = "There was a network error"
            Checkout.INVALID_OPTIONS -> binding.errordetail.text = "An issue with options passed "
            Checkout.PAYMENT_CANCELED -> binding.errordetail.text = "Payment cancelled"
            Checkout.TLS_ERROR -> binding.errordetail.text =
                "your device does not support TLS v1.1 or TLS v1.2."
        }*/
    }
}
