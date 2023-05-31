package com.fitness.fitnessCru.fragment


import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.MealDetailActivity
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.activities.ViewProfileActivity
import com.fitness.fitnessCru.adapter.CalendarAdapter
import com.fitness.fitnessCru.adapter.MealCalendarAdapter
import com.fitness.fitnessCru.adapter.MealsAdapter
import com.fitness.fitnessCru.databinding.FragmentNutritionBinding
import com.fitness.fitnessCru.interfaces.QuestionaryInterface2
import com.fitness.fitnessCru.model.MealsPojo
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.*
import com.fitness.fitnessCru.viewmodel.DashBoardViewModel
import com.fitness.fitnessCru.viewmodel.NutritionDashboardViewModel
import com.fitness.fitnessCru.vm_factory.DashBoardVMFactory
import com.fitness.fitnessCru.vm_factory.NutritionDashboardVMFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.math.roundToInt


class Nutrition : Fragment(), CalendarAdapter.OnItemListener, MealCalendarAdapter.OnItemListener {

    lateinit var fregmentNutritionBinding: FragmentNutritionBinding
    lateinit var calendarAdapter: MealCalendarAdapter
    lateinit var adapter: MealsAdapter

    private lateinit var repository1: Repository
    private lateinit var viewModel1: DashBoardViewModel
    private lateinit var factory1: DashBoardVMFactory

    private lateinit var loading: CustomProgressLoading
    private lateinit var viewModel: NutritionDashboardViewModel

    private lateinit var selectedDate: String
    private lateinit var registerForActivityResult: ActivityResultLauncher<Array<String>>

    private var fileName = ""

    private var idd = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerForActivityResult =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultMap ->

            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        fregmentNutritionBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_nutrition, container, false)

        val c: Calendar = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        selectedDate = df.format(c.getTime())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            registerForActivityResult.launch(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
            )
        }

        var userDetail = Session.getUserDetails()

        if (userDetail.name == null) {
            if (userDetail.email != null) {
                var n = userDetail.email[0].toString().uppercase()
                fregmentNutritionBinding.gobackbtn.text = n
            } else {
                fregmentNutritionBinding.gobackbtn.visibility = View.GONE
                fregmentNutritionBinding.placeholder.visibility = View.VISIBLE
            }
        } else {
            var n = userDetail.name[0].toString().uppercase()
            fregmentNutritionBinding.gobackbtn.text = n
        }

        Log.v("bbDEtail", Session.getUserDetails().toString())

        fregmentNutritionBinding.placeholder.setOnClickListener {
            startActivity(
                Intent(
                    requireContext()!!,
                    ViewProfileActivity::class.java
                )
            )
        }

        fregmentNutritionBinding.gobackbtn.setOnClickListener {
            startActivity(
                Intent(
                    requireContext()!!,
                    ViewProfileActivity::class.java
                )
            )
        }


        repository1 = Repository()

        factory1 = DashBoardVMFactory(repository1)

        viewModel1 = ViewModelProvider(this, factory1).get(DashBoardViewModel::class.java)

        loading = CustomProgressLoading(requireContext())
        fregmentNutritionBinding.addMealTv.setOnClickListener {
            val intent = Intent(requireContext(), SetupAllActivity::class.java)
            intent.putExtra(Constants.DESTINATION, Constants.ADD_MEAL)
            intent.putExtra("date", selectedDate)
            startActivity(intent)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CalendarUtils.selectedDate = LocalDate.now()
            this.setMonthView()
        }
        recycleMeals(selectedDate)


        downloadPDF()

        return fregmentNutritionBinding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun downloadPDF() {
        fregmentNutritionBinding.downloadTV.setOnClickListener {
            viewModel.downloadPDF(selectedDate)

        }
        viewModel.downloadPDF.observe(requireActivity()) {
            loading.hideProgress()
            val body = it.body()
            val flag = writeResponseBodyToDisk(body!!)
            if (flag) {
                try {
                    val fileO = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            .toString() + "/FitCru"
                    )
                    val file = File(fileO, fileName)


                    CoroutineScope(Dispatchers.IO).launch {

                        withContext(Dispatchers.Main) {
                            if (file?.exists() == true) {
                                val path = getUriFromFile(file, requireContext())
                                try {
                                    val newIntent = Intent(Intent.ACTION_VIEW)
                                    newIntent.setDataAndType(path, "application/pdf")
                                    newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    startActivity(
                                        Intent.createChooser(
                                            newIntent,
                                            "Open PDF"
                                        )
                                    )

                                } catch (e: Exception) {
                                    Toast.makeText(
                                        requireContext(),
                                        e.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else
                                Toast.makeText(
                                    requireContext(),
                                    "File not found",
                                    Toast.LENGTH_LONG
                                ).show()
                        }
                    }


//                    val intent = Intent(Intent.ACTION_VIEW)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    intent.setDataAndType(
//                        FileProvider.getUriForFile(
//                            requireContext(),
//                            context?.applicationContext!!.packageName + ".provider",File(
//                               file,"FitCru weekly diet plan.pdf"
//                            )
//                        ), "application/pdf"
//                    )
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//                    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
//                    startActivity(Intent.createChooser(
//                        intent,
//                        "Open PDF"
//                    ))
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        context,
                        "No PDF reader found to open this file.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    private fun getUriFromFile(file: File, context: Context): Uri? =
        try {
            FileProvider.getUriForFile(context, context.packageName + ".provider", file)
        } catch (e: Exception) {
            if (e.message?.contains("ProviderInfo.loadXmlMetaData") == true) {
                throw Error("FileProvider doesn't exist or has no permissions")
            } else {
                throw e
            }
        }

    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        try {
            // todo change the file location/name according to your needs
            fileName = System.currentTimeMillis().toString() + "FitCru weekly diet plan.pdf"
            //File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            val path = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/FitCru"
            )

            if (!path.exists()) {
                path.mkdirs()
            }

            val futureStudioIconFile = File(path, fileName)
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(20480)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                Log.d("Downloads ", "working")
                outputStream = FileOutputStream(futureStudioIconFile)
                while (true) {
                    val read = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Log.d("Downloads file", "file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream.flush()
                return true
            } catch (e: IOException) {
                Log.d("Downloads ", "not working")
                return false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            return false
        }
    }

    private fun recycleMeals(selectedDate: String) {
        try {
            adapter = MealsAdapter(requireContext())
            val repository by lazy { Repository() }
            val factory by lazy { NutritionDashboardVMFactory(repository) }
            viewModel = ViewModelProvider(
                this,
                factory
            ).get(NutritionDashboardViewModel::class.java)

            fregmentNutritionBinding!!.mealsRV.adapter = adapter
            adapter.setOnClick(object : QuestionaryInterface2 {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onClick(id: Int, time: String) {
                    this@Nutrition.idd = id
                    if (id != null) {
                        var intent = Intent(context, MealDetailActivity::class.java)
                        var bundle = Bundle()
                        bundle.putInt("data", id)
                        bundle.putString("date", selectedDate)
                        bundle.putString("time", time)
                        bundle.putString("back_location", "nutrition")
                        intent.putExtras(bundle)
                        context?.startActivity(intent)
                    }

//                    mealConsumed(id)
                }
            })

            adapter.date = selectedDate
            loading.showProgress()
            viewModel.getNutritionDetails(selectedDate)
            viewModel.response.observe(requireActivity()) {
                loading.hideProgress()
                if (it.isSuccessful && it.body()!!.errorCode == 0) {
                    if (it.body()!!.data.user_selected_meals.isNullOrEmpty()) {
                        fregmentNutritionBinding.mealsRV.visibility = View.GONE
                        fregmentNutritionBinding.toastTv.visibility = View.VISIBLE
                    } else {
                        fregmentNutritionBinding.mealsRV.visibility = View.VISIBLE
                        fregmentNutritionBinding.toastTv.visibility = View.GONE
                        adapter.setList(it.body()!!.data.user_selected_meals)
                    }

                    graphProgress(it.body()!!.data)
                } else Util.toast(requireContext(), "Something went wrong")
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "recycleMeals: $e")
        }
    }

/*    @RequiresApi(Build.VERSION_CODES.O)
    private fun mealConsumed(id: Int) {
        if (id != null) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd,hh:mm a", Locale.ENGLISH)
            val formatted = current.format(formatter)
            Toast.makeText(
                context, id.toString() + "/" + formatted.split(",")[1] + "/" +
                        formatted.split(",")[0], Toast.LENGTH_SHORT
            ).show()
            viewModel1.consumedMeal(
                ConsumedMealBody(
                    id, id,
                    formatted.split(",")[1],
                    formatted.split(",")[0]
                )
            )
            viewModel1.response1.observe(viewLifecycleOwner) { response ->
                loading.hideProgress()
                val msg = if (response.isSuccessful && response.body() != null)
                    response.body()!!.message
                else if (!response.isSuccessful && response.errorBody() != null)
                    Util.error(response.errorBody(), ConsumedMealResponse::class.java).message
                else "Something went wrong"
                Util.toast(requireContext(), msg)
            }
        }
    }*/

    private fun graphProgress(data: MealsPojo.Data) {
        try {
            val caloriePro = ((data.consume_calorie.toFloat() * 100) / data.total_calorie.toFloat())
            fregmentNutritionBinding.progressIndicator.setProgress(Math.round(caloriePro))

            var angle = data.consume_calorie.toDouble()
            val df = DecimalFormat("#0.0")
            val angleFormated = df.format(angle)
            fregmentNutritionBinding!!.mainProgressTV.text =
                ("${angleFormated} KCAL")
        } catch (e: Exception) {
            fregmentNutritionBinding.progressIndicator.setProgress(1)
        }
        try {
            val carbPro =
                ((data.consume_carbs.toFloat() * 100) / data.total_carbs.toFloat()).roundToInt()
                    .toFloat()
            fregmentNutritionBinding.progressCarb.progress = carbPro

            /*  val totalCarbGram = (data.total_carbs.toInt() * 0.129598).toInt()
              val consumeCarbGram = (data.consume_carbs.toInt() * 0.129598).toInt()*/
            val leftCarb = (data.total_carbs.toFloat() - data.consume_carbs.toFloat())
            fregmentNutritionBinding.conCarbGTv.text = ("${Math.round(leftCarb)}g")
            fregmentNutritionBinding.totalCarbGTv.text =
                ("of ${Math.round(data.total_carbs.toFloat())}g left")
        } catch (e: Exception) {
            fregmentNutritionBinding.conCarbGTv.text = ("0g")
            fregmentNutritionBinding.totalCarbGTv.text = ("of 0g left")
            fregmentNutritionBinding.progressCarb.progress = 0f
        }
        try {
            val proteinPro =
                ((data.consume_protein.toFloat() * 100) / data.total_protein.toFloat()).roundToInt()
                    .toFloat()
            fregmentNutritionBinding.progressProtein.progress = proteinPro

//            val totalProGram = (data.total_protein.toInt() * 0.129598).toInt()
//            val consumeProGram = (data.consume_protein.toInt() * 0.129598).toInt()
            val leftPro = (data.total_protein.toFloat() - data.consume_protein.toFloat())
            fregmentNutritionBinding.consumeProGTv.text = ("${Math.round(leftPro)}g")
            fregmentNutritionBinding.totalProGTv.text =
                ("of ${Math.round(data.total_protein.toFloat())}g left")

        } catch (e: Exception) {
            fregmentNutritionBinding.consumeProGTv.text = ("0g")
            fregmentNutritionBinding.totalProGTv.text = ("of 0g left")

            fregmentNutritionBinding.progressProtein.progress = 0f
        }
        try {
            val fatPro =
                ((data.consume_fat.toFloat() * 100) / data.total_fat.toFloat()).roundToInt()
                    .toFloat()
            fregmentNutritionBinding.progressFat.progress = fatPro

//            val totalFatGram = (data.total_fat.toInt() * 0.129598).toInt()
//            val consumeFatGram = (data.consume_fat.toInt() * 0.129598).toInt()
            val leftFat = (data.total_fat.toFloat() - data.consume_fat.toFloat())
            fregmentNutritionBinding.consumeFatGtV.text = ("${Math.round(leftFat)}g")
            fregmentNutritionBinding.totalFatGTv.text =
                ("of ${Math.round(data.total_fat.toFloat())}g left")

        } catch (e: Exception) {
            fregmentNutritionBinding.consumeFatGtV.text = ("0g")
            fregmentNutritionBinding.totalFatGTv.text = ("of 0g left")

            fregmentNutritionBinding.progressFat.progress = 0f
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        fregmentNutritionBinding!!.monthYearTV.setText(CalendarUtils.selectedDate?.let {
            CalendarUtils.monthFromDate(
                it
            )
        })
        val daysInMonth: List<LocalDate?> =
            CalendarUtils.sevenDaysFun(CalendarUtils.selectedDate!!)

        fregmentNutritionBinding!!.yearTV.setText(CalendarUtils.selectedDate?.let {
            CalendarUtils.yearFromDate(
                it
            )
        })
        val year: List<LocalDate?> = CalendarUtils.sevenDaysFun(CalendarUtils.selectedDate!!)

        calendarAdapter = MealCalendarAdapter(
            daysInMonth as List<LocalDate>,
            year as List<LocalDate>, this
        )
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )
        fregmentNutritionBinding!!.calendarRecyclerView.setLayoutManager(layoutManager)
        fregmentNutritionBinding!!.calendarRecyclerView.setAdapter(calendarAdapter)
        fregmentNutritionBinding!!.calendarRecyclerView.smoothScrollToPosition(8)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, date: LocalDate?) {
        if (date != null) {
            CalendarUtils.selectedDate = date
            selectedDate = CalendarUtils.formattedDate(date)
            adapter.date = selectedDate
            if (calendarAdapter != null) {
                loading.showProgress()
                recycleMeals(selectedDate)
                calendarAdapter.notifyDataSetChanged()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
//        loading.showProgress()
        recycleMeals(selectedDate)
    }
}
