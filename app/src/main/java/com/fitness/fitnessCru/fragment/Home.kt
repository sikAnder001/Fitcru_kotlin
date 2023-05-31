package com.fitness.fitnessCru.fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.*
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.*
import com.fitness.fitnessCru.adapter.*
import com.fitness.fitnessCru.databinding.FragmentHomeBinding
import com.fitness.fitnessCru.interfaces.ConsumedMealInterface
import com.fitness.fitnessCru.model.DataChip
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.DashBoardResponse
import com.fitness.fitnessCru.response.UserDetailsResponse
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.DashBoardViewModel
import com.fitness.fitnessCru.viewmodel.NotificationsViewModel
import com.fitness.fitnessCru.viewmodel.UserDetailsViewModel
import com.fitness.fitnessCru.vm_factory.DashBoardVMFactory
import com.fitness.fitnessCru.vm_factory.NotificationsVMFactory
import com.fitness.fitnessCru.vm_factory.UserDetailsVMFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt
import kotlin.system.exitProcess


class Home : Fragment() {
    private val TAG = OpponentsActivity::class.java.simpleName
//    private lateinit var opponents: ArrayList<QBUser>

    private var progressDialog: ProgressDialog? = null

    private lateinit var homeBindng: FragmentHomeBinding
    private val binding get() = homeBindng!!
    private lateinit var repository: Repository
    private lateinit var viewModel: DashBoardViewModel

    private lateinit var factory: DashBoardVMFactory
    private lateinit var chipAdapter: ChipAdapter

    //    private lateinit var loading: CustomProgressLoading
    private lateinit var dateTime: LocalDateTime
    private var present: Int = 0
    private var date = ""
    /* private var totSteps = ""
     private var totCal = ""*/

    var doubleBackToExitPressedOnce = false

    //google fit
    private var fitnessOptions: FitnessOptions? = null
    protected val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 11

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBindng = FragmentHomeBinding.inflate(inflater, container, false)

//        loading = CustomProgressLoading(requireContext())

//        Toast.makeText(requireContext(), "HomeQBID"+SharedPrefsHelper.getQbUser().id.toString(), Toast.LENGTH_SHORT).show()
        if (checkForInternet(requireContext())) {
            binding.coachDashboardContainer.visibility = View.GONE

            repository = Repository()

            factory = DashBoardVMFactory(repository)

            viewModel = ViewModelProvider(this, factory).get(DashBoardViewModel::class.java)

            val repository by lazy { Repository() }
            val factory by lazy { UserDetailsVMFactory(repository) }
            val viewModel by lazy {
                ViewModelProvider(
                    this,
                    factory
                )[UserDetailsViewModel::class.java]
            }
            viewModel.getUserDetails()
            viewModel.response.observe(viewLifecycleOwner) {
                if (it.isSuccessful && it.code() == 200) {
                    val data = it.body()!!.data

                    Session.setUserDetails(data)
                    setRecyclerViewInit(setChipData())
                    chipAdapter.notifyDataSetChanged()
                    Log.v(
                        "Alldata",
                        Session.getUserDetails().toString() + "breaking news" + data.toString()
                    )
                } else {
                    Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG)
                        .show()
                }
            }
            setRecyclerViewInit(setChipData())

            getNotificationCount()


            backPress()

            homeBindng.myProfileContainer.setOnClickListener {

                val intent = Intent(activity, EditProfileActivity::class.java)

                startActivity(intent)
            }
            homeBindng.toolbar.notificationBtn.setOnClickListener {
                val intent = Intent(context?.applicationContext, SetupAllActivity::class.java)
                intent.putExtra(Constants.DESTINATION, Constants.NOTIFICATION_LIST)
                startActivity(intent)
            }

            calendar()
            var userDetail = Session.getUserDetails()

            if (userDetail.name == null) {
                if (userDetail.email != null) {
                    var n = userDetail.email[0].toString().uppercase()
                    homeBindng.toolbar.tvMenuBar.text = n
                } else {
                    homeBindng.toolbar.tvMenuBar.visibility = View.GONE
                    homeBindng.toolbar.placeholder.visibility = View.VISIBLE
                }
            } else {
                homeBindng.toolbar.placeholder.visibility = View.GONE
                homeBindng.toolbar.tvMenuBar.visibility = View.VISIBLE
                var n = userDetail.name[0].toString().uppercase()
                homeBindng.toolbar.tvMenuBar.text = n
            }

            Log.v("bbDEtail", Session.getUserDetails().toString())

            homeBindng.toolbar.placeholder.setOnClickListener {

                startActivity(
                    Intent(
                        requireContext()!!,
                        ViewProfileActivity::class.java
                    )
                )
            }
            homeBindng.toolbar.tvMennu.setOnClickListener {


                startActivity(
                    Intent(
                        requireContext()!!,
                        ViewProfileActivity::class.java


                    )
                )/*Intent.getStringExtra("showView")=="1".toString()*/


                /*val intent = Intent(context, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("showView","1")
                context.startActivity(intent)*/

                /* }
                   else
                 {
                     startActivity(
                         Intent(
                             requireContext()!!,
                             ViewProfileActivity::class.java
                         )
                     )
                 }*/

            }
        } else {
            dateTime = LocalDateTime.now()
            backPress()
            setRecyclerViewInit(setChipData())
            Toast.makeText(context, "check your internet connection", Toast.LENGTH_SHORT).show()
        }
        val timer: CountDownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                updateRowData()
            }
        }.start()

        calendar()
        return binding.root

//        session = WebRtcSessionManager.getCurrentSession()
//        Log.v("Dashboard", session?.userInfo?.get("name").toString())
//        val userInfo: HashMap<String, String> = (session?.userInfo as HashMap<String, String>?)!!
//        val userin = userInfo.get("name")
//        val userim = userInfo.get("image")
//        Hawk.put(NAME, userin)
//        Hawk.put(IMAGE, userim)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
            .build()
        val accountGoogleFit =
            GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions!!)

        if (!GoogleSignIn.hasPermissions(accountGoogleFit, fitnessOptions!!)) {
            GoogleSignIn.requestPermissions(
                this, // your activity
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, // e.g. 1
                accountGoogleFit,
                fitnessOptions!!
            )
        } else {
            accessGoogleFit()
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
            viewModel.watchResponse.observe(viewLifecycleOwner) {
                if (it.isSuccessful && it.code() == 200) {
                    val data = it.body()!!
                    getDashBoardWithDate(dateTime)
                    val dtf = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
                    val now = LocalDateTime.now()
                    Session.setSyncTime(dtf.format(now).toString())

//                    Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
                } else {
                    if (it.code() == 404) {
                        val error = Util.error(it.errorBody(), UserDetailsResponse::class.java)
                        homeBindng.toolbar.notificationCounter.visibility = View.GONE
//                    homeBindng.toolbar.notificationCounter.text = "0"
                    } else Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun accessGoogleFit() {
        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val startTime = endTime.minusDays(1)

        Log.v("timingNow:", "....${LocalDateTime.now()}")
//        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
//        val milliSecondsUntilTomorrow: Long =
//            Duration(now, now.plusDays(1).withTimeAtStartOfDay()).getMillis()
//        val cal: Calendar = Calendar.getInstance()
//        val now = Date()
//        cal.setTime(now)
//        val endTime: Long = cal.getTimeInMillis()
//        cal.add(Calendar.DAY_OF_YEAR, -1)
//        val startTime: Long = cal.getTimeInMillis()
//        Log.i("accessGoogleFit", "Range Start: $startTime  $endTime")

//      val readRequest2 = DataReadRequest.Builder()
//            .aggregate(DataType.AGGREGATE_CALORIES_EXPENDED)
//            .bucketByActivityType(1, TimeUnit.DAYS)
//            .setTimeRange(endTime.minusDays(1).toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
//            .build()


//        Fitness.getHistoryClient(
//            requireContext(),
//            GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions!!)
//        )
//            .readData(readRequest2)
//            .addOnSuccessListener { response ->
//                // The aggregate query puts datasets into buckets, so flatten into a
//                // single list of datasets
//                for (dataSet in response.buckets.flatMap { it.dataSets }) {
//                    dumpDataSet2(dataSet)
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "There was an error reading data from Google Fit", e)
//            }


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

        Fitness.getHistoryClient(
            requireContext(),
            GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions!!)
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
                Log.i(TAG, "Total steps: $totalSteps")
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
            requireContext(),
            GoogleSignIn.getLastSignedInAccount(requireContext())!!
        )
            .readData(readRequests)
            .addOnSuccessListener { dataReadResponse -> // Process the data
                if (dataReadResponse.buckets.size > 0) {
                    Log.d(TAG, "NEWNumber of buckets: " + dataReadResponse.buckets.size)
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
                                Session.setCal(calories.roundToInt().toString())
                                Log.d(
                                    TAG,
                                    "NEWCalories: $calories Start: $startTime2 End: $endTime2"
                                )
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    TAG,
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

//    fun dumpDataSet2(dataSet: DataSet) {
//        Log.i(TAG, "Data returned for Data type: ${dataSet.dataType.name}")
//        Log.i(TAG, "dataset2: ${dataSet}")
//        for (dp in dataSet.dataPoints) {
//            Log.i(TAG, "Data point:")
//            Log.i(TAG, "\tType: ${dp.dataType.name}")
//            Log.i(TAG, "\tStart: ${dp.getStartTimeString()}")
//            Log.i(TAG, "\tEnd: ${dp.getEndTimeString()}")
//            for (field in dp.dataType.fields) {
//                Log.i(TAG, "\tField: ${field.name.toString()} Value: ${dp.getValue(field)}")
//
//            }
//        }
//    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    private fun getNotificationCount() {
        val repository by lazy { Repository() }
        val factory by lazy { NotificationsVMFactory(repository) }
        val viewModel by lazy {
            ViewModelProvider(
                this,
                factory
            )[NotificationsViewModel::class.java]
        }
        viewModel.notificationList()
        viewModel.response.observe(viewLifecycleOwner) {
            if (it.isSuccessful && it.code() == 200) {
                val data = it.body()!!.data
                var count = 0
                for (element in data) {
                    if (element.is_read == "0") {
                        count++
                        continue
                    }
                }
                if (count == 0) {
                    homeBindng.toolbar.notificationCounter.visibility = View.GONE
                } else {
                    homeBindng.toolbar.notificationCounter.visibility = View.VISIBLE
                    homeBindng.toolbar.notificationCounter.text = count.toString()
                }
                Log.v(
                    "Alldata",
                    Session.getUserDetails().toString() + "breaking news" + data.toString()
                )
            } else {
                if (it.code() == 404) {
                    val error = Util.error(it.errorBody(), UserDetailsResponse::class.java)
                    homeBindng.toolbar.notificationCounter.visibility = View.GONE
//                    homeBindng.toolbar.notificationCounter.text = "0"
                } else Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun calendar() {
        dateTime = LocalDateTime.now()
        getDashBoardWithDate(dateTime)

        homeBindng.ivPast.setOnClickListener {
            if (present > -7) {
                present--
                dateTime = dateTime.minusDays(1)
                getDashBoardWithDate(dateTime)
            } else Util.toast(requireContext(), "You can see last 7 days data only")
        }

        homeBindng.ivFuture.setOnClickListener {
            if (present < 7) {
                present++
                dateTime = dateTime.plusDays(1)
                getDashBoardWithDate(dateTime)
            } else Util.toast(requireContext(), "You can see next 7 days data only")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDashBoardWithDate(dateTime: LocalDateTime) {
        val formatter1 = DateTimeFormatter.ofPattern("dd MMM")
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        binding.tvDate.text = formatter1.format(dateTime)
        date = formatter2.format(dateTime)
        viewModel.getDashBoard(date)
        getDashBoard(date)
        var userDetail = Session.getUserDetails()
        Log.v("yes", "yumm " + userDetail.name)

        if (userDetail.name == null) {
            if (userDetail.email != null) {
                var n = userDetail.email[0].toString().uppercase()
                homeBindng.toolbar.tvMenuBar.text = n
            } else {
                homeBindng.toolbar.tvMenuBar.visibility = View.GONE
                homeBindng.toolbar.placeholder.visibility = View.VISIBLE
            }
        } else {
            var n = userDetail.name[0].toString().uppercase()
            homeBindng.toolbar.tvMenuBar.text = n
        }

//        loading.showProgress()
    }

    private fun circularProgress(
        l1: Long,
        l2: Long,
        l3: Long,
        l4: Long,
        l5: Int,
        l6: Int,
        l7: Long,
        l8: Long
    ) {
        binding.progress1.apply {
            if (l1.toFloat() == 0f && l2.toFloat() == 0f) {
                progress = 0f
                setProgressWithAnimation(0f, 1000) // =1s
                progressMax = 0.1f
            } else {
                progress = if (l1.toFloat() > l2.toFloat()) l2.toFloat() else l1.toFloat()
                setProgressWithAnimation(l1.toFloat(), 1000) // =1s
                progressMax = l2.toFloat()
            }

            progressBarColor = Color.parseColor("#ed2b2b")

            backgroundProgressBarColor = Color.parseColor("#4a4a4a")

            progressBarWidth = 10f // in DP
            backgroundProgressBarWidth = 9f // in DP

            roundBorder = true
            //    startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        binding.progress2.apply {
            if (l3.toFloat() == 0f && l4.toFloat() == 0f) {
                progress = 0f
                setProgressWithAnimation(0f, 1000) // =1s
                progressMax = 0.1f
            } else {
                progress = if (l3.toFloat() > l4.toFloat()) l4.toFloat() else l3.toFloat()

                setProgressWithAnimation(l3.toFloat(), 1000L) // =1s

                progressMax = l4.toFloat()
            }

            progressBarColor = Color.parseColor("#00cf0a")

            backgroundProgressBarColor = Color.parseColor("#4a4a4a")

            progressBarWidth = 10f // in DP
            backgroundProgressBarWidth = 9f // in DP

            roundBorder = true
            //    startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
        binding.progress3.apply {

            if (l5.toFloat() == 0f && l6.toFloat() == 0f) {
                progress = 0f
                setProgressWithAnimation(0f, 1000) // =1s
                progressMax = 0.1f
            } else {
                progress = if (l5.toFloat() > l6.toFloat()) l6.toFloat() else l5.toFloat()

                setProgressWithAnimation(l5.toFloat(), 1000L) // =1s

                progressMax = l6.toFloat()
            }

            progressBarColor = Color.parseColor("#0ad2ff")

            backgroundProgressBarColor = Color.parseColor("#4a4a4a")

            progressBarWidth = 10f // in DP
            backgroundProgressBarWidth = 9f // in DP

            roundBorder = true
            //    startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
        binding.progress4.apply {
            if (l7.toFloat() == 0f && l8.toFloat() == 0f) {
                progress = 0f
                setProgressWithAnimation(0f, 1000) // =1s
                progressMax = 0.1f
            } else {
                progress = if (l7.toFloat() > l8.toFloat()) l8.toFloat() else l7.toFloat()

                setProgressWithAnimation(l7.toFloat(), 1000) // =1s

                progressMax = l8.toFloat()
            }

            progressBarColor = Color.parseColor("#E4A94B")

            backgroundProgressBarColor = Color.parseColor("#4a4a4a")

            progressBarWidth = 10f // in DP
            backgroundProgressBarWidth = 9f // in DP

            roundBorder = true
            //    startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
    }

    private fun setChipData(): ArrayList<DataChip> {
        val chipData = ArrayList<DataChip>()
        chipData.add(DataChip(1, R.drawable.newmysummary, "My Summary"))
        chipData.add(DataChip(2, R.drawable.newgetcoach, "Get a Coach"))
        chipData.add(DataChip(3, R.drawable.newmysummary, "FitCru Live"))
        return chipData
    }

    private fun setRecyclerViewInit(chipdata: ArrayList<DataChip>) {
        binding.chiprv.setHasFixedSize(true)
        binding.chiprv.layoutManager = GridLayoutManager(
            context, 3
        )
        val data = chipdata
        if (Session.getUserDetails().ispaid == 1) {
            data.removeAt(2)
        }

        chipAdapter = ChipAdapter(data, context, object : ChipAdapter.NavInterface {
            override fun onClick(id: Int) {
                if (id == 2) {
                    val intent = Intent(requireActivity(), SubscriptionPlanActivity::class.java)
                    intent.putExtra("num", 1)
                    startActivity(intent)
//                    findNavController().navigate(R.id.coaching3)
                }
            }
        })
        binding.chiprv.adapter = chipAdapter
    }

    private fun getDashBoard(date: String) {
        var userDetail = Session.getUserDetails()

        Log.v("LogKeyIspad", userDetail.toString())

        val todayTaskAdapter = TodayTaskAdapter(context, this.date)

        val workOutAdapter = WorkOutAdapter(context, object : WorkOutAdapter.SelectVideoListener {
            override fun onClick(video: String) {
                if (video.isNotEmpty()) {
                    val intent = Intent(
                        requireActivity(),
                        VideoPlayerActivity::class.java
                    )
                    val bundle = Bundle()
                    bundle.putString("url", video)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
        })

        val trendingSessionsAdapter = TrendingSessionAdapter(context, userDetail.ispaid)

        val whatCookingToday = WhatCookingTodayAdapter(context)

        val transformationAdapter = TransformationAdapter(context)

        val coachDashboardAdapter = CoachDashboardAdapter(context)

        val habitAdapter = PlanDetailHabitAdapter(context, object :
            PlanDetailHabitAdapter.SelectVideoListener {
            override fun onClick(video: String) {
                Log.v("vido Link", video)
                if (video.isNotEmpty()) {
                    val intent = Intent(
                        requireActivity(),
                        VideoPlayerActivity::class.java
                    )
                    val bundle = Bundle()
                    bundle.putString("url", video)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }

        })

        binding.rvTodayTask.adapter = todayTaskAdapter

        binding.rvWorkout.adapter = workOutAdapter

        binding.rvTrendingSessions.adapter = trendingSessionsAdapter

        binding.rvCookingToday.adapter = whatCookingToday

        binding.rvTransformation.adapter = transformationAdapter

        binding.coachDashboardRv.adapter = coachDashboardAdapter

        binding.habitRv.adapter = habitAdapter

        viewModel.response.observe(viewLifecycleOwner) {

//            loading.hideProgress()

            if (it.isSuccessful && it.code() == 200 && it.body() != null) {

                binding.apply {


                    var progress = it.body()!!.data.progress

                    try {
                        workoutTV.text =
                            "${
                                Math.round(progress.workout.workout_kcal_burnt.trim().toFloat())
                            }/${Math.round(progress.workout.workout_kcal_target.trim().toFloat())}"

                    } catch (e: Exception) {
                        workoutTV.text = "0/0"
                        Log.e(TAG, "getDashBoard: $e")
                    }

                    tvWorkoutUnit.text = "${progress.workout.unit.trim()} burnt"

                    dietTV.text =
                        "${Math.round(progress.diet.kcal_burnt.trim().toFloat())}/${
                            Math.round(
                                progress.diet.diet_val.trim().toFloat()
                            )
                        }"
                    tvDietUnit.text = "${progress.diet.unit} Consumed"

                    if (progress.water.water_used_val.isNotEmpty()) {
                        waterTV.text =
                            "${Math.round(progress.water.water_used_val.trim().toFloat())}/${
                                Math.round(
                                    progress.water.water_taget.trim().toFloat()
                                )
                            }"
                    } else {
                        waterTV.text = "0/0"
                    }
                    tvWaterUnit.text = "${progress.water.unit.trim()}"

                    if (progress.steps.step_count_val.isNotEmpty()) {
                        stepsTV.text =
                            "${Math.round(progress.steps.step_count_val.trim().toFloat())}/${
                                Math.round(
                                    progress.steps.step_target.trim().toFloat()
                                )
                            }"
                    } else {
                        stepsTV.text = "0/0"
                    }
                    circularProgress(
                        Math.round(progress.workout.workout_kcal_burnt.trim().toFloat()).toLong(),
                        Math.round(progress.workout.workout_kcal_target.trim().toFloat()).toLong(),
                        Math.round(progress.diet.kcal_burnt.trim().toFloat()).toLong(),
                        Math.round(progress.diet.diet_val.trim().toFloat()).toLong(),
                        Math.round(progress.water.water_used_val.trim().toFloat()),
                        Math.round(progress.water.water_taget.trim().toFloat()),
                        Math.round(progress.steps.step_count_val.trim().toFloat()).toLong(),
                        Math.round(progress.steps.step_target.trim().toFloat()).toLong(),
                    )

                }

                todayTaskAdapter.setList(it.body()!!.data.todays_task)

                todayTaskAdapter.setList2(it.body()!!.data.cooking_today)

                if (it.body()!!.data.workout != null) {
                    workOutAdapter.setList(it.body()!!.data.workout!!)
                }

                trendingSessionsAdapter.setList(it.body()!!.data.trending_seasons)

                whatCookingToday.setList(it.body()!!.data.cooking_today)

                if (it.body()!!.data.habit!!.isNotEmpty()) {
                    habitTitle.visibility = View.VISIBLE
                    habitRv.visibility = View.VISIBLE
                    habitAdapter.setList(it.body()!!.data.habit)
                } else {
                    habitTitle.visibility = View.GONE
                    habitRv.visibility = View.GONE
                }

                transformationAdapter.setList(
                    it.body()!!.data.transformation
                )

                it.body()!!.data.your_coach?.let { it1 -> coachDashboardAdapter.setList(it1) }

                if (it.body()!!.data.your_coach != null) {
//                    binding.coachDashboardContainer.visibility = View.VISIBLE
                    coachesdetail(it.body()!!.data.your_coach)
                } else {
                    binding.coachDashboardContainer.visibility = View.GONE
                }


                try {
                    llMeal.visibility =
                        if (it.body()!!.data.todays_task.size > 0) GONE else VISIBLE

                    llMeal.setOnClickListener {
                        val intent = Intent(requireContext(), SetupAllActivity::class.java)
                        intent.putExtra(Constants.DESTINATION, Constants.ADD_MEAL)
                        intent.putExtra("date", this.date)
                        startActivity(intent)
                    }

                    if (!it.body()!!.data.workout.isNullOrEmpty()) {
                        binding.llWorkout.visibility = View.GONE
                        binding.rvWorkout.visibility = View.VISIBLE
                    } else {
                        binding.apply {
                            rvWorkout.visibility = View.GONE
                            llWorkout.apply {
                                visibility = View.VISIBLE
                                setOnClickListener {
                                    findNavController().navigate(R.id.workout)
                                }
                            }
                        }
                    }

                } catch (e: Exception) {

                }

                todayTaskAdapter.setConsumed(object : ConsumedMealInterface {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun click(data: DashBoardResponse.Data.TodayTask) {
                        //   mealConsumed(data)
                    }
                })

            }
        }
    }

    private fun coachesdetail(yourCoach: ArrayList<DashBoardResponse.Data.YourCoach>?) {


    }

    inner class KeyEventListener : DialogInterface.OnKeyListener {
        override fun onKey(dialog: DialogInterface?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
            return keyCode == KeyEvent.KEYCODE_BACK
        }
    }

    private fun backPress() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (doubleBackToExitPressedOnce) {
                        activity!!.finishAffinity()
                        exitProcess(0)
                    }
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(context, "Please click BACK again to exit", Toast.LENGTH_SHORT)
                        .show()

                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        doubleBackToExitPressedOnce = false
                    }, 2000)
                }
            }
            )
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        var userDetail = Session.getUserDetails()
        Log.v("yes", "yumm " + userDetail.name)
        super.onResume()
        if (checkForInternet(requireContext())) {
            getNotificationCount()

            if (userDetail.name != null) {
                homeBindng.toolbar.placeholder.visibility = View.GONE
                homeBindng.toolbar.tvMenuBar.visibility = View.VISIBLE
                var n = userDetail.name[0].toString().uppercase()
                homeBindng.toolbar.tvMenuBar.text = n
                requireFragmentManager().beginTransaction().detach(this).attach(this).commit();
            }
            getDashBoardWithDate(dateTime)
        } else {
            Toast.makeText(context, "check your internet connection", Toast.LENGTH_SHORT).show()
        }

    }


    /*   private fun isCallServiceRunning(serviceClass: Class<*>): Boolean {
           val manager = requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
           val services = manager.getRunningServices(Integer.MAX_VALUE)
           for (service in services) {
               if (CallService::class.java.name == service.service.className) {
                   return true
               }
           }
           return false
       }

       private fun clearAppNotifications() {
           val notificationManager =
               requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
           notificationManager.cancelAll()
       }*/
/*
    private fun loadUsers() {
        isLoading = true
        showProgressDialog(R.string.dlg_loading_opponents)
        currentPage += 1
        val rules = ArrayList<GenericQueryRule>()
        rules.add(
            GenericQueryRule(
            ORDER_RULE,
            ORDER_DESC_UPDATED
        )
        )
        val requestBuilder = QBPagedRequestBuilder()
        requestBuilder.rules = rules
        requestBuilder.perPage = PER_PAGE_SIZE_100
        requestBuilder.page = currentPage

        QBUsers.getUsers(requestBuilder).performAsync(object : QBEntityCallback<ArrayList<QBUser>> {
            override fun onSuccess(qbUsers: ArrayList<QBUser>?, bundle: Bundle?) {
                qbUsers?.let {
                    Log.d(TAG, "Successfully loaded users")
                    QbUsersDbManager.saveAllUsers(qbUsers, true)

                    val totalPagesFromParams = bundle?.get(TOTAL_PAGES_BUNDLE_PARAM) as Int
                    if (currentPage >= totalPagesFromParams) {
                        hasNextPage = false
                    }

                    if (currentPage == 1) {
                        initOpponents()
                    } else {
                        //usersAdapter?.addUsers(qbUsers)
                    }
                }
                hideProgressDialog()
                isLoading = false
            }

            override fun onError(e: QBResponseException?) {
                e?.let {
                    Log.d(TAG, "Error load users" + e.message)
                    hideProgressDialog()
                    isLoading = false
                    currentPage -= 1
                    showErrorSnackbar(R.string.loading_users_error, e) {
                        loadUsers()
                    }
                }
            }
        })
    }
*/


/*

    protected fun showErrorSnackbar(@StringRes resId: Int, e: Exception?, clickListener: View.OnClickListener) {
        val rootView = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
        com.fitness.fitnessCru.quickbox.utils.showErrorSnackbar(
            rootView,
            resId,
            e,
            R.string.dlg_retry,
            clickListener
        )
    }

    private fun initOpponents() {
        opponents = QbUsersDbManager.allUsers
        opponents.remove(SharedPrefsHelper.getCurrentUser())
        */
/*  if (usersAdapter == null) {
              usersAdapter = UsersAdapter(requireActivity(), opponents)
              usersAdapter?.setSelectedItemsCountsChangedListener(object :
                  UsersAdapter.SelectedItemsCountsChangedListener {
                  override fun onCountSelectedItemsChanged(count: Int) {

                  }
              })

              usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
              usersRecyclerView.adapter = usersAdapter
              usersRecyclerView.addOnScrollListener(ScrollListener(usersRecyclerView.layoutManager as LinearLayoutManager))
          } else {
              usersAdapter?.updateUsersList(opponents)
          }*//*

    }





*/
/*
    private fun startCall(isVideoCall: Boolean,clientdata: ArrayList<QBUser>) {
        // val usersCount = usersAdapter?.selectedUsers?.size
        *//*

*/
/* if (usersCount != null && usersCount > MAX_OPPONENTS_COUNT) {
             longToast(String.format(getString(R.string.error_max_opponents_count), MAX_OPPONENTS_COUNT))
             return
         }*//*
*/
/*


        val userIds = clientdata.map { it.id }
        val conferenceType = if (isVideoCall) {
            QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
        } else {
            QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO
        }
        val rtcClient = QBRTCClient.getInstance(requireContext())
        val session = rtcClient.createNewSessionWithOpponents(userIds, conferenceType)
        WebRtcSessionManager.setCurrentSession(session)

        // make Users FullName Strings and id's list for iOS VOIP push
        val sessionId = session.sessionID
        val opponentIds = ArrayList<String>()
        val opponentNames = ArrayList<String>()
        val usersInCall = clientdata

        // the Caller in exactly first position is needed regarding to iOS 13 functionality
        usersInCall.add(0, currentUser)

        for (user in usersInCall) {
            val userId = user.id.toString()
            val name = user.fullName ?: user.login

            opponentIds.add(userId)
            opponentNames.add(name)
        }

        val idsInLine = TextUtils.join(",", opponentIds)
        val namesInLine = TextUtils.join(",", opponentNames)

        Log.d(TAG, "New Session with id: $sessionId\n Users in Call: \n$idsInLine\n$namesInLine")

        userIds?.forEach { userId ->
            Executor.addTask(object : ExecutorTask<Boolean> {
                override fun onBackground(): Boolean {
                    val timeout3Seconds = 3000L
                    return QBChatService.getInstance().pingManager.pingUser(userId, timeout3Seconds)
                }

                override fun onForeground(result: Boolean) {
                    if (result) {
                        val message =
                            "Participant with id: $userId is online. There is no need to send a VoIP notification."
                        Log.d(TAG, message)
                    } else {
                        sendPushMessage(userId, currentUser.login, sessionId, idsInLine, namesInLine, isVideoCall)
                    }
                }

                override fun onError(exception: Exception) {
                    shortToast(exception.message.toString())
                }
            })
        }

        CallActivity.start(requireContext(), false)
    }
*//*


    private fun checkIsLoggedInChat(): Boolean {
        if (!QBChatService.getInstance().isLoggedIn) {
            startLoginService()
            shortToast(R.string.login_chat_retry)
            return false
        }
        return true
    }

    protected fun checkPermissions(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (checkPermission(permission)) {
                return true
            }
        }
        return false
    }

    private fun startLoginService() {
        if (SharedPrefsHelper.hasCurrentUser()) {
            LoginService.loginToChatAndInitRTCClient(requireContext(), SharedPrefsHelper.getCurrentUser())
        }
    }


    protected fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_DENIED
    }
*/


}