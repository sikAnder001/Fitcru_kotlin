package com.fitness.fitnessCru.fragment

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.FragmentChartsSummaryBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.MySummaryResponse
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.MySummaryViewModel
import com.fitness.fitnessCru.vm_factory.MySummaryVMFactory
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class ChartsSummaryFragment : Fragment() {

    private lateinit var chartBindng: FragmentChartsSummaryBinding

    lateinit var barList: ArrayList<BarEntry>
    lateinit var barDataSet: BarDataSet
    lateinit var barData: BarData
    lateinit var labeName: ArrayList<String>

    lateinit var barListStep: ArrayList<BarEntry>
    lateinit var barDataSetStep: BarDataSet
    lateinit var barDataStep: BarData
    lateinit var labeNameStep: ArrayList<String>

    lateinit var lineList: ArrayList<Entry>
    lateinit var lineDataSet: LineDataSet
    lateinit var lineData: LineData

    lateinit var lineListSleep: ArrayList<Entry>
    lateinit var lineDataSetSleep: LineDataSet
    lateinit var lineDataSleep: LineData
    lateinit var labelNameSleep: ArrayList<String>

    private lateinit var repository: Repository
    private lateinit var viewModel: MySummaryViewModel
    private lateinit var factory: MySummaryVMFactory

    private var weight = listOf<MySummaryResponse.Weight>()
    private var water = listOf<MySummaryResponse.Water>()
    private var step = listOf<MySummaryResponse.Step>()
    private var sleep = listOf<MySummaryResponse.Sleep>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chartBindng = FragmentChartsSummaryBinding.inflate(inflater, container, false)

        requireActivity().setRequestedOrientation(
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        )

        repository = Repository()

        factory = MySummaryVMFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(MySummaryViewModel::class.java)

        getSummary()
        //setLineChartSleepValues()
        //setBarChartValues()

        return chartBindng.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSummary() {
        try {
            viewModel.getSummary()
            viewModel.response.observe(viewLifecycleOwner) {
                var response = it.body()
                if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0) {

                    step = response?.step!!
                    setStepBarChartValues(step)

                    water = response.water!!
                    setLineChartValues(water)

                    /* sleep = response.sleep!!
                     setLineChartSleepValues(sleep)*/

                    weight = response.weight!!
                    setBarChartValues(weight)

                } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                    Util.toast(
                        requireContext(), it.body()!!.message
                    )
                    activity?.onBackPressed()
                } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                    requireContext(),
                    Util.error(it.errorBody(), MySummaryResponse::class.java).message
                )
            }
        } catch (e: Exception) {
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setStepBarChartValues(step: List<MySummaryResponse.Step>) {
        try {
            barListStep = ArrayList()
            labeNameStep = ArrayList()
            var colors: MutableList<Int>
            colors = ArrayList()


            if (step.isNullOrEmpty()) {
                val cal: Calendar = Calendar.getInstance()
                val month_date = SimpleDateFormat("MMM")
                val format = month_date.format(cal.getTime())
                barListStep.add(BarEntry(0f, 0f))
                labeNameStep.add(format)
                colors.add(resources.getColor(R.color.three))

            } else {
                val max = step?.maxBy { it -> it?.steps?.toInt() ?: 0 }

                var stepsFloat: Float
                for ((index, element) in step.withIndex()) {
                    if (element.steps != null) {
                        stepsFloat = element.steps.toFloat()
                    } else stepsFloat = 0f

                    val indexFloat = index.toFloat()
                    val localDate =
                        LocalDate.parse(
                            element.date,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        )
                    val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM")
                    var format: String = localDate.format(dateTimeFormatter)
                    barListStep.add(BarEntry(indexFloat, stepsFloat))
                    labeNameStep.add(format)

                    if (element.steps.equals(max?.steps)) {
                        colors.add(resources.getColor(R.color.three))
                    } else {
                        colors.add(resources.getColor(R.color.one))
                    }
                }
            }

            chartBindng.barChartSteps.getAxisLeft().setAxisMinValue(0f);
            chartBindng.barChartSteps.getAxisRight().setAxisMinValue(0f);

            val xAxis: XAxis = chartBindng.barChartSteps.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(labeNameStep)

            xAxis.position = XAxis.XAxisPosition.BOTTOM

            xAxis.apply {
                setDrawGridLines(false)
                setDrawAxisLine(true)
                granularity = 1f
                textColor = Color.WHITE
                setLabelCount(labeNameStep.size)
            }
            chartBindng.apply {
                barChartSteps.apply {
                    getDescription().setEnabled(false)
                    setTouchEnabled(false)
                    setPinchZoom(false)
                    legend.isEnabled = false
                }
            }

            barDataSetStep = BarDataSet(barListStep, "")
            barDataSetStep.setDrawValues(false)

            barDataSetStep.setColors(colors)

            barDataStep = BarData(barDataSetStep)
            barDataStep.barWidth = 0.3f

            chartBindng.barChartSteps.data = barDataStep

            barDataSetStep.valueTextColor = Color.WHITE
            barDataSetStep.valueTextSize = 15f


            val leftAxis: YAxis = chartBindng.barChartSteps.getAxisLeft()
            leftAxis.setDrawAxisLine(false)
            leftAxis.axisLineColor = resources.getColor(R.color.transparent_10)
            leftAxis.textColor = resources.getColor(R.color.white)

            //       barDataSet.setColor(resources.getColor(R.color.rating_color))

            val rightAxis: YAxis = chartBindng.barChartSteps.getAxisRight()
            rightAxis.isEnabled = false
            chartBindng.barChartSteps.setBackgroundColor(resources.getColor(R.color.transparent))
            //  chartBindng.barChart.animateXY(2000, 2000)

        } catch (e: Exception) {
            Log.v("step error", e.message.toString())
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setLineChartValues(water: List<MySummaryResponse.Water>) {
        lineList = ArrayList()
        labeName = ArrayList()

        try {

            if (water.isNullOrEmpty()) {
                Log.v("single on empty", "working1")
                val cal: Calendar = Calendar.getInstance()
                val month_date = SimpleDateFormat("MMM")
                val format = month_date.format(cal.getTime())
                lineList.add(BarEntry(0f, 0f))
                labeName.add(format)

                Log.v("single on empty", "working2")
            } else {
                var waterFloat: Float
                for ((index, element) in water.withIndex()) {
                    if (element?.water_intake != null) {
                        waterFloat = element.water_intake.toFloat()
                    } else {
                        waterFloat = 0f
                    }

                    val indexFloat = index.toFloat()
                    val localDate =
                        LocalDate.parse(
                            element!!.date,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        )
                    val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM")
                    val format: String = localDate.format(dateTimeFormatter)
                    lineList.add(BarEntry(indexFloat, waterFloat))
                    labeName.add(format)
                }
            }

//            lineList.apply {
//                add(BarEntry(0f, 20f))
//                add(BarEntry(1f, 10f))
//                add(BarEntry(2f, 30f))
//                add(BarEntry(3f, 20f))
//                add(BarEntry(4f, 50f))
//            }
//
//            labeName.apply {
//                add("1")
//                add("2")
//                add("3")
//                add("4")
//                add("5")
//            }

            chartBindng.lineChart.getAxisLeft().setAxisMinValue(0f);
            chartBindng.lineChart.getAxisRight().setAxisMinValue(0f);

            lineDataSet = LineDataSet(lineList, "count")

            lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            lineData = LineData(lineDataSet)

            chartBindng.lineChart.data = lineData

            lineDataSet.valueTextColor = Color.WHITE
            lineDataSet.valueTextSize = 15f

            val drawable =
                requireActivity()!!.resources.getDrawable(R.drawable.line_chart_background)

            lineDataSet.apply {
                setColor(resources.getColor(R.color.rating_color))

                fillDrawable = drawable

                setDrawFilled(true)

                setDrawValues(false)
            }

            chartBindng.apply {
                lineChart.apply {
                    getDescription().setEnabled(false)
                    setTouchEnabled(false)
                    setPinchZoom(false)
                    legend.isEnabled = false
                }
            }

            val xAxis: XAxis = chartBindng.lineChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(labeName)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.apply {
                setDrawGridLines(false)
                setDrawAxisLine(true)
                granularity = 1f
                textColor = Color.WHITE
                setLabelCount(labeName.size)
            }

            val leftAxis: YAxis = chartBindng.lineChart.getAxisLeft()
            leftAxis.setDrawAxisLine(false)
            leftAxis.axisLineColor = resources.getColor(R.color.transparent_10)
            leftAxis.textColor = resources.getColor(R.color.white)
            //  leftAxis.textColor=resources.getColor(R.color.white)

            val rightAxis: YAxis = chartBindng.lineChart.getAxisRight()
            rightAxis.isEnabled = false
            chartBindng.barChart.setBackgroundColor(resources.getColor(R.color.transparent))


        } catch (e: Exception) {
        }

        chartBindng.lineChart.invalidate()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun setBarChartValues(weight: List<MySummaryResponse.Weight>) {
        barList = ArrayList()
        labeName = ArrayList()

        var colors: MutableList<Int>
        colors = ArrayList()

        try {

            if (weight.isNullOrEmpty()) {
                Log.v("single on empty", "working1")
                val cal: Calendar = Calendar.getInstance()
                val month_date = SimpleDateFormat("MMM")
                val format = month_date.format(cal.getTime())
                barList.add(BarEntry(0f, 0f))
                labeName.add(format)
                Log.v("single on empty", "working2")
                colors.add(resources.getColor(R.color.one))
            } else {
                val max = weight?.maxBy { it -> it?.weight?.toDouble()!!.toInt() ?: 0 }

                var weightFloat: Float
                for ((index, element) in weight.withIndex()) {
                    if (element.weight != null) {
                        weightFloat = element.weight.toFloat()
                    } else weightFloat = 0f

                    val indexFloat = index.toFloat()
                    val localDate =
                        LocalDate.parse(
                            element.date,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        )
                    val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM")
                    val format: String = localDate.format(dateTimeFormatter)
                    barList.add(BarEntry(indexFloat, weightFloat))
                    labeName.add(format)

                    if (element.weight.equals(max?.weight)) {
                        colors.add(resources.getColor(R.color.three))
                    } else {
                        colors.add(resources.getColor(R.color.one))
                    }
                }
            }

            chartBindng.barChart.getAxisLeft().setAxisMinValue(0f);
            chartBindng.barChart.getAxisRight().setAxisMinValue(0f);

            val xAxis: XAxis = chartBindng.barChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(labeName)

            xAxis.position = XAxis.XAxisPosition.BOTTOM

            xAxis.apply {
                setDrawGridLines(false)
                setDrawAxisLine(true)
                granularity = 1f
                textColor = Color.WHITE
                setLabelCount(labeName.size)
            }
            chartBindng.apply {
                barChart.apply {
                    getDescription().setEnabled(false)
                    setTouchEnabled(false)
                    setPinchZoom(false)
                    legend.isEnabled = false
                }
            }

            barDataSet = BarDataSet(barList, "")
            barDataSet.setDrawValues(false)

            barDataSet.setColors(colors)

            barData = BarData(barDataSet)
            barData.barWidth = 0.3f

            chartBindng.barChart.data = barData

            barDataSet.valueTextColor = Color.WHITE
            barDataSet.valueTextSize = 15f

            val leftAxis: YAxis = chartBindng.barChart.getAxisLeft()
            leftAxis.setDrawAxisLine(false)
            leftAxis.axisLineColor = resources.getColor(R.color.transparent_10)
            leftAxis.textColor = resources.getColor(R.color.white)

            //       barDataSet.setColor(resources.getColor(R.color.rating_color))

            val rightAxis: YAxis = chartBindng.barChart.getAxisRight()
            rightAxis.isEnabled = false
            chartBindng.barChart.setBackgroundColor(resources.getColor(R.color.transparent))
            //  chartBindng.barChart.animateXY(2000, 2000)
        } catch (e: Exception) {
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setLineChartSleepValues(sleep: List<MySummaryResponse.Sleep>) {
        lineListSleep = ArrayList()
        labelNameSleep = ArrayList()

        try {
            var sleepFloat: Any
            for ((index, element) in sleep.withIndex()) {
                if (element?.total_sleep_time != null) {
                    sleepFloat = 0f /*element.total_sleep_time.toFloat()*/
                } else {
                    sleepFloat = 0f
                }

                val indexFloat = index.toFloat()
                val localDate =
                    LocalDate.parse(
                        element!!.date,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    )
                val dateTimeFormatter = DateTimeFormatter.ofPattern("EEE")
                val format: String = localDate.format(dateTimeFormatter)
                lineListSleep.add(BarEntry(indexFloat, sleepFloat))
                labelNameSleep.add(format)
            }

//            lineListSleep.apply {
//                add(BarEntry(0f, 9.01f))
//                add(BarEntry(1f, 9.0f))
//                add(BarEntry(2f, 9.02f))
//                add(BarEntry(3f, 9.03f))
//                add(BarEntry(4f, 9.05f))
//            }
//            labeName.apply {
//                add("1")
//                add("2")
//                add("3")
//                add("4")
//                add("5")
//            }

            lineDataSetSleep = LineDataSet(lineListSleep, "count")

            lineDataSetSleep.mode = LineDataSet.Mode.CUBIC_BEZIER
            lineDataSleep = LineData(lineDataSetSleep)

            chartBindng.lineChartSleep.data = lineDataSleep

            lineDataSetSleep.valueTextColor = Color.WHITE
            lineDataSetSleep.valueTextSize = 15f

            val drawable =
                requireActivity()!!.resources.getDrawable(R.drawable.line_chart_background_sleep)

            lineDataSetSleep.apply {
                setColor(resources.getColor(R.color.three))

                fillDrawable = drawable

                setDrawFilled(true)

                setDrawValues(false)
            }

            chartBindng.apply {
                lineChartSleep.apply {
                    getDescription().setEnabled(false)
                    setTouchEnabled(false)
                    setPinchZoom(false)
                    legend.isEnabled = false
                }
            }

            val xAxis: XAxis = chartBindng.lineChartSleep.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(labeName)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.apply {
                setDrawGridLines(false)
                setDrawAxisLine(false)
                granularity = 1f
                textColor = Color.WHITE
                setLabelCount(labelNameSleep.size)
            }

            val leftAxis: YAxis = chartBindng.lineChartSleep.getAxisLeft()
            leftAxis.isEnabled = false
            //  leftAxis.textColor=resources.getColor(R.color.white)

            val rightAxis: YAxis = chartBindng.lineChartSleep.getAxisRight()
            rightAxis.isEnabled = false
        } catch (e: Exception) {
        }

        chartBindng.lineChartSleep.invalidate()
    }
}