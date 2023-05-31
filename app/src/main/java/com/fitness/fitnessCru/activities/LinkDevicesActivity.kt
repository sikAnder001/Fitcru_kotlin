package com.fitness.fitnessCru.activities


import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.databinding.ActivityLinkDevicesBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.UserDetailsResponse
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.NotificationsViewModel
import com.fitness.fitnessCru.vm_factory.NotificationsVMFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


class LinkDevicesActivity : AppCompatActivity() {
    //linked

    private var binding: ActivityLinkDevicesBinding? = null

    private var fitnessOptions: FitnessOptions? = null
    protected val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 11

//    private lateinit var loading: CustomProgressLoading


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLinkDevicesBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

//        loading = CustomProgressLoading(applicationContext)

        binding!!.gobackbtn.setOnClickListener {
            onBackPressed()
            this.finish()
        }

        binding!!.syncTime.text = "Last synced at ${Session.getSyncTime()}"

//        bluetoothPermission()

        syncWatch()

        if (checkIfFitInstalled()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                binding!!.tvConnect.text = "Connect"

            } else {
                binding!!.tvConnect.text = "Connected"
            }
        } else {
            binding!!.tvConnect.text = "Install"
        }

        binding!!.tvConnect.setOnClickListener {
            if (binding!!.tvConnect.text.toString() == "Install") {
                try {
                    val appStoreIntent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            "market://details?id=com.google.android.apps.fitness"
                        )
                    )
                    appStoreIntent.setPackage("com.android.vending")
                    startActivity(appStoreIntent)
                } catch (exception: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.fitness")
                        )
                    )
                }
            } else {
                fitnessOptions = FitnessOptions.builder()
                    .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                    .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                    .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                    .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                    .build()
                val accountGoogleFit =
                    GoogleSignIn.getAccountForExtension(this, fitnessOptions!!)

                if (!GoogleSignIn.hasPermissions(accountGoogleFit, fitnessOptions!!)) {
                    GoogleSignIn.requestPermissions(
                        this, // your activity
                        GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, // e.g. 1
                        accountGoogleFit,
                        fitnessOptions!!
                    )
                } else {
                    Toast.makeText(applicationContext, "Already Connected", Toast.LENGTH_SHORT)
                        .show()
                    accessGoogleFit()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun syncWatch() {
        binding!!.refresh.setOnClickListener {
            val clk_rotate = AnimationUtils.loadAnimation(
                this,
                com.fitness.fitnessCru.R.anim.rotate_clockwise
            )

            // assigning that animation to
            // the image and start animation
            binding!!.refresh.startAnimation(clk_rotate)
            fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .build()
            val accountGoogleFit =
                GoogleSignIn.getAccountForExtension(this, fitnessOptions!!)

            if (!GoogleSignIn.hasPermissions(accountGoogleFit, fitnessOptions!!)) {
                GoogleSignIn.requestPermissions(
                    this, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, // e.g. 1
                    accountGoogleFit,
                    fitnessOptions!!
                )
            } else {
                accessGoogleFit()
                val timer: CountDownTimer = object : CountDownTimer(2000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {

                    }

                    override fun onFinish() {
                        Toast.makeText(applicationContext, "Refreshed!", Toast.LENGTH_SHORT).show()
                        updateRowData()
                    }
                }.start()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateRowData() {
        try {

            Log.v(
                "Updated STEPS",
                Session.getSteps().toString() + "..Calorie" + Session.getCal().toString()
            )

            val map: MutableMap<String, RequestBody> = mutableMapOf()
            map.put(
                "calburn",
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    if (Session.getCal().toString() == "{}") "0" else Session.getCal().toString()
                )
            )
            map.put("bodytemp", RequestBody.create("text/plain".toMediaTypeOrNull(), "112"))
            map.put("bloodpressure", RequestBody.create("text/plain".toMediaTypeOrNull(), "112"))
            map.put("heartbeat", RequestBody.create("text/plain".toMediaTypeOrNull(), "112"))
            map.put(
                "steps",
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    if (Session.getSteps().toString() == "{}") "0" else Session.getSteps()
                        .toString()
                )
            )


            val requestFile1 = RequestBody.create(MultipartBody.FORM, "12288")
            val requestFile2 = RequestBody.create(MultipartBody.FORM, "12288")
            val requestFile3 = RequestBody.create(MultipartBody.FORM, "12288")
            val requestFile4 = RequestBody.create(MultipartBody.FORM, "12288")
            val requestFile5 = RequestBody.create(MultipartBody.FORM, "12288")

            val repository by lazy { Repository() }
            val factory by lazy { NotificationsVMFactory(repository) }
            val viewModel by lazy {
                ViewModelProvider(
                    this,
                    factory
                )[NotificationsViewModel::class.java]
            }
            viewModel.watchSync(map)
            viewModel.watchResponse.observe(this) {
                if (it.isSuccessful && it.code() == 200) {
                    val data = it.body()!!
                    val dtf = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
                    val now = LocalDateTime.now()
                    Session.setSyncTime(dtf.format(now).toString())
                    binding!!.syncTime.text = "Last synced at ${Session.getSyncTime()}"
//                    Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
                } else {
                    if (it.code() == 404) {
                        val error = Util.error(it.errorBody(), UserDetailsResponse::class.java)
//                    homeBindng.toolbar.notificationCounter.text = "0"
                    } else Toast.makeText(
                        applicationContext,
                        "Error: Something went wrong",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        } catch (call: Exception) {
            Log.d("TAGSHOW", "error stopServices FetchSynServices")

//            Toast.makeText(requireContext(), "Error! Try again later", Toast.LENGTH_SHORT)
//                .show()
        }

        /*val uploadFile =
            apiServices.uploadProfile(map)
        uploadFile.enqueue(
            object : Callback<ResponseModel?> {
                override fun onResponse(
                    call: Call<ResponseModel?>,
                    response: Response<ResponseModel?>
                ) {
                    Log.e("TAGSH", "onSuccess: refreshAllGroups: call:" + call.request())
                    Log.e("TAGSH", "onSuccess: refreshAllGroups: call:" + response.message())
                    Log.d("TAGSHOW", response.code().toString())
                    if (response.code() == 401) {
                        Log.d("TAGSHOW", "error if 401")
                    } else {
                        Log.d("TAGSHOW", "error error if 401")
                    }
                }

                override fun onFailure(call: Call<ResponseModel?>, t: Throwable) {
                    Log.e("TAG", "onFailure: refreshAllGroups: call:" + call.request())
                    Toast.makeText(requireContext(), "qwqqqq", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    } catch (call: Exception) {
        Log.d("TAGSHOW", "error stopServices FetchSynServices")
        Toast.makeText(requireContext(), "Error! Try again later", Toast.LENGTH_SHORT)
            .show()

    }*/

    }

    /*  private var requestBluetooth =
          registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
              if (result.resultCode == RESULT_OK) {
                  //allow
              } else {
                  //deny
              }
          }

      private val requestMultiplePermissions =
          registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
              permissions.entries.forEach {
                  Log.d("test006", "${it.key} = ${it.value}")
              }
          }


      private fun bluetoothPermission() {
          *//* if (ContextCompat.checkSelfPermission(
                this@LinkDevicesActivity,
                BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_DENIED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(
                    this@LinkDevicesActivity,
                    arrayOf(BLUETOOTH_CONNECT),
                    2
                )
                return
            }
        }*//*

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(
                arrayOf(
                    BLUETOOTH_SCAN,
                    BLUETOOTH_CONNECT
                )
            )
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

            requestBluetooth.launch(enableBtIntent)
        }

    }*/

    fun checkIfFitInstalled(): Boolean {
        return try {
            applicationContext.packageManager.getPackageInfo(
                "com.google.android.apps.fitness",
                PackageManager.GET_ACTIVITIES
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun accessGoogleFit() {
        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val startTime = endTime.minusDays(1)

        val cal = Calendar.getInstance()
        val now = Date()
        cal.time = now
        val endTime2 = cal.timeInMillis
        cal.time = atStartOfDay(now)
        val atStart = cal.timeInMillis
        val startTime2 = cal.timeInMillis

        val readRequest =
            DataReadRequest.Builder()
                .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime2, endTime2, TimeUnit.MILLISECONDS)
                .build()
        Log.v("timingNow:", "....${LocalDateTime.now()}")

        Fitness.getHistoryClient(
            applicationContext,
            GoogleSignIn.getAccountForExtension(this, fitnessOptions!!)
        )
            .readData(readRequest)
            .addOnSuccessListener { response ->
                // The aggregate query puts datasets into buckets, so flatten into a
                // single list of datasets
                val totalSteps = response.buckets
                    .flatMap { it.dataSets }
                    .flatMap { it.dataPoints }
                    .sumBy { it.getValue(Field.FIELD_STEPS).asInt() }
                Session.setSteps(totalSteps.toString())
                Log.i("TAG", "Total steps: $totalSteps")
            }
            .addOnFailureListener { e ->
                Log.w("accessGoogleFit1", "There was an error reading data from Google Fit", e)
            }


        // Use the History API to read the calorie data

        Log.v("CheckNow Time", atStart.toString())
        // Use the History API to read the calorie data
        val readRequests = DataReadRequest.Builder()
            .aggregate(DataType.AGGREGATE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime2, endTime2, TimeUnit.MILLISECONDS)
            .build()

        Fitness.getHistoryClient(
            applicationContext,
            GoogleSignIn.getLastSignedInAccount(applicationContext)!!
        )
            .readData(readRequests)
            .addOnSuccessListener { dataReadResponse -> // Process the data
                if (dataReadResponse.buckets.size > 0) {
                    Log.d("TAG", "NEWNumber of buckets: " + dataReadResponse.buckets.size)
                    for (bucket in dataReadResponse.buckets) {
                        val dataSets =
                            bucket.dataSets
                        for (dataSet in dataSets) {
                            for (dataPoint in dataSet.dataPoints) {
                                val startTime2 =
                                    dataPoint.getStartTime(TimeUnit.MILLISECONDS)
                                val endTime2 = dataPoint.getEndTime(TimeUnit.MILLISECONDS)
                                val calories =
                                    dataPoint.getValue(Field.FIELD_CALORIES)
                                        .asFloat()
                                Session.setCal(calories.toInt().toString())
                                Log.d(
                                    "TAG",
                                    "NEWCalories: $calories Start: $startTime2 End: $endTime2"
                                )
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    "TAG",
                    "There was a problem reading the calories data.",
                    e
                )
            }

    }

    fun atStartOfDay(date: Date?): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

}