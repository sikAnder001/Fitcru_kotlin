package com.fitness.fitnessCru.fragment

import TimePickerHelper
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.MyRemindersAdapter
import com.fitness.fitnessCru.body.ReminderBody
import com.fitness.fitnessCru.databinding.FragmentMyRemindersBinding
import com.fitness.fitnessCru.databinding.SetReminderBinding
import com.fitness.fitnessCru.helper.AlarmReceiver
import com.fitness.fitnessCru.interfaces.DotClickInterface
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.DeleteReminderResponse
import com.fitness.fitnessCru.response.GetAllReminderResponse
import com.fitness.fitnessCru.response.ReminderCommonResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.ReminderViewModel
import com.fitness.fitnessCru.vm_factory.ReminderVMFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MyRemindersFragment : Fragment() {

    private lateinit var remindersBinding: FragmentMyRemindersBinding

    private lateinit var setReminderBinding: SetReminderBinding

    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var timePicker: TimePickerHelper

    private lateinit var myRemindersAdapter: MyRemindersAdapter

    private lateinit var loading: CustomProgressLoading

    private var flag = false
    private var flags = 0
    private var ids = 0

    private lateinit var repository: Repository
    private lateinit var viewModel: ReminderViewModel
    private lateinit var factory: ReminderVMFactory

    var minutes: Int = 0
    var hours: Int = 0

    var startDate: Date? = null;

    var now: Calendar = Calendar.getInstance()

    var date: Date? = null;

    var notificationId = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        remindersBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_reminders, container, false)

        timePicker = TimePickerHelper(requireContext(), false, true)

        loading = CustomProgressLoading(requireContext())

        repository = Repository()

        factory = ReminderVMFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(ReminderViewModel::class.java)


        getALMyReminder()

        setMyRemindersRV()

        callBottomSheet()

        addReminder()

        return remindersBinding.root
    }

    private fun callDelete(id: Int, position: Int) {
        viewModel.deleteReminder(id)
        viewModel.deleteReminder.observe(viewLifecycleOwner) {
            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0) {
                viewModel.getAllReminder()
            } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                Util.toast(
                    requireContext(), it.body()!!.message
                )
                activity?.onBackPressed()
            } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                requireContext(),
                Util.error(it.errorBody(), DeleteReminderResponse::class.java).message
            )
        }
    }

    private fun setMyRemindersRV() {

        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        remindersBinding.myRemindersRv.layoutManager = linearLayout

        myRemindersAdapter = MyRemindersAdapter(context)

        remindersBinding.myRemindersRv.adapter = myRemindersAdapter

        myRemindersAdapter.setOnClickInterface(object : DotClickInterface {
            override fun onDotClick(view: View, data: GetAllReminderResponse.Data, position: Int) {
                notificationId = position
                PopupMenu(context, view).apply {
                    setOnMenuItemClickListener { item ->
                        when (item?.itemId) {
                            R.id.editText -> {
                                flag = false
                                ids = data.id
                                showBottomSheet(data)
                                true
                            }
                            R.id.deleteText -> {
                                callDelete(data.id, position)
                                true
                            }
                            else -> false
                        }
                    }
                    inflate(R.menu.show_menu)
                    show()
                }
            }
        })
    }

    private fun showBottomSheet(data: GetAllReminderResponse.Data) {
        setReminderBinding.apply {
            addTitleEt.setText(data.reminder_title)
            remindMeEt.setText(data.reminder_description)
            addTimeTv.text = data.reminder_time
            remindMeBeforeTv.text = data.reminder_before_time
            waterTypeSpinner.setSelection(
                if (data.repeat == "0") 0 else 1
            )
            bottomSheetDialog.show()
        }
        bottomSheetDialog.show()
    }

    private fun getALMyReminder() {
        viewModel.getAllReminder()
        viewModel.allReminder.observe(viewLifecycleOwner) {

            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0) {
                myRemindersAdapter.setList(it.body()!!.data as ArrayList<GetAllReminderResponse.Data>)
            } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                Util.toast(
                    requireContext(), it.body()!!.message
                )
                activity?.onBackPressed()
            } else if (!it.isSuccessful && it.errorBody() != null)
                Util.toast(
                    requireContext(),
                    Util.error(it.errorBody(), GetAllReminderResponse::class.java).message
                )
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun callBottomSheet() {
        setReminderBinding = SetReminderBinding.inflate(layoutInflater)

        bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetDialog.setContentView(setReminderBinding.root)

        remindersBinding.addRemindersBtn.setOnClickListener {
            flag = true
            showBottomSheet(
                GetAllReminderResponse.Data("", 0, "", "", "", "", "", "", 0)
            )
        }

        setReminderBinding.crossBTN.setOnClickListener { bottomSheetDialog.hide() }

        setReminderBinding.addTimeTv.setOnClickListener {
            flags = 1
            val timePicker = TimePickerDialog(
                requireContext(),
                timePickerDialogListener,
                12,
                10,
                false
            )
            timePicker.show()
            timePicker.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.two)
            timePicker.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.two)
        }
        setReminderBinding.remindMeBeforeTv.setOnClickListener {
            flags = 2
            val timePicker = TimePickerDialog(
                requireContext(),
                timePickerDialogListener,
                0,
                0,
                false
            )
            timePicker.show()
            timePicker.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.two)
            timePicker.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.two)
        }

    }


    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val time = "$hourOfDay:$minute";
            val fmt = SimpleDateFormat("HH:mm");
//            var date: Date? = null;
            if (flags == 1) {
                hours = hourOfDay
                minutes = minute
                startDate = fmt.parse(time)
            }
            try {
                date = fmt.parse(time);
            } catch (e: ParseException) {
                e.printStackTrace();
            }
            val formattedTime = SimpleDateFormat("hh:mm aa").format(date)
            setReminderBinding.apply {
                formattedTime.also {
                    (if (flags == 1) addTimeTv else remindMeBeforeTv).text = it
                }
            }
        }

    private fun validation(): Boolean {
        val pair: Pair<Boolean, String>
        setReminderBinding.apply {
            pair = if (addTitleEt.text.toString().trim().isEmpty())
                Pair(false, "Please Fill Title")
            else if (remindMeEt.text.toString().trim().isEmpty())
                Pair(false, "Please Fill Remind me to(short note)")
            else if (addTimeTv.text.toString().trim().isEmpty())
                Pair(false, "Please Select Time")
            else if (remindMeBeforeTv.text.toString().trim().isEmpty())
                Pair(false, "Please Select Before Time")
            else Pair(true, "")
            if (!pair.first) Util.toast(requireContext(), pair.second)
        }
        return pair.first
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addReminder() {

        var value = ""
        setReminderBinding.apply {
            waterTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.v("Reapeat22", parent!!.selectedItem.toString())
                    value = if ((parent!!.selectedItem.toString()).equals(
                            "Repeat : No Repeat",
                            true
                        )
                    ) "0"
                    else "1"
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

            setReminderBtn.setOnClickListener {

                Log.v("repeat", value.toString())

                val startTime = Calendar.getInstance()
                startTime[Calendar.HOUR_OF_DAY] = hours
                startTime[Calendar.MINUTE] = minutes
                startTime[Calendar.SECOND] = 0
                val alarmStartTime = startTime.timeInMillis

                val id = startTime.timeInMillis.toInt()

                val intent = Intent(context, AlarmReceiver::class.java)
                intent.putExtra("notificationId", notificationId)
                intent.putExtra("title", addTitleEt.text.toString())
                intent.putExtra("subTitle", remindMeEt.text.toString())

                val alarmIntent = PendingIntent.getBroadcast(
                    context, id,
                    intent, PendingIntent.FLAG_IMMUTABLE
                )

                val alarm = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager

                Log.v(
                    "Now",
                    hours.toString() + minutes.toString()
                )

                if (value == "1") alarm.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    alarmStartTime,
                    AlarmManager.INTERVAL_DAY,
                    alarmIntent
                );
                else alarm.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmStartTime,
                    alarmIntent
                )

                if (validation()) {
                    loading.showProgress()
                    if (flag)
                        viewModel.addReminder(
                            ReminderBody(
                                returnString(addTitleEt),
                                returnString(remindMeEt),
                                returnString(addTimeTv),
                                returnString(remindMeBeforeTv),
                                value
                            )
                        )
                    else
                        viewModel.updateReminder(
                            ReminderBody(
                                ids,
                                returnString(addTitleEt),
                                returnString(remindMeEt),
                                returnString(addTimeTv),
                                returnString(remindMeBeforeTv),
                                value
                            )
                        )
                }

                /* val intent2 = Intent(AlarmClock.ACTION_SET_ALARM)
                 intent2.putExtra(AlarmClock.EXTRA_HOUR, hours)
                 intent2.putExtra(AlarmClock.EXTRA_MINUTES, minutes)

                 startActivity(intent2)*/
//                  val cDate = LocalDateTime.now()
//
//                now.set(Calendar.YEAR,cDate.year)
//                now.set(Calendar.MONTH,cDate.monthValue)
//                now.set(Calendar.DAY_OF_MONTH,cDate.dayOfMonth)
//
//                now.set(Calendar.HOUR_OF_DAY,hours);
//                now.set(Calendar.MINUTE,minutes);


//                NotifyMe.Builder(requireContext())
//                    .title(returnString(addTitleEt))
//                    .content(returnString(remindMeEt))
//                    .color(255,0,0,255)
//                    .led_color(255,255,255,255)
//                    .time(now)
//                    .addAction(intent,"Snooze",false)
//                    .key("test")
//                    .addAction(Intent(), "Dismiss", true, false)
//                    .addAction(intent,"Done")
//                    .large_icon(R.mipmap.ic_launcher_round)
//                    .rrule("FREQ=MINUTELY;INTERVAL=5;COUNT=2")
//                    .build()

            }

            viewModel.addReminder.observe(viewLifecycleOwner) {
                loading.hideProgress()
                val message = if (it.isSuccessful && it.body() != null) {
                    bottomSheetDialog.hide()
                    viewModel.getAllReminder()
                    it.body()!!.message
                } else if (!it.isSuccessful)
                    Util.error(it.errorBody(), ReminderCommonResponse::class.java).message
                else "Something Went Wrong"
                Util.toast(requireContext(), message)
            }

            viewModel.updateReminder.observe(viewLifecycleOwner) {
                loading.hideProgress()
                if (it.isSuccessful && it.body() != null) {
                    bottomSheetDialog.hide()
                    viewModel.getAllReminder()
                } else if (!it.isSuccessful)
                    Util.error(it.errorBody(), ReminderCommonResponse::class.java).message
                else "Something Went Wrong"
            }
        }
    }

    private fun returnString(view: TextView): String {
        return view.text.toString()
    }

    private fun returnString(view: EditText): String {
        return view.text.toString()
    }

//    @SuppressLint("MissingPermission")
//    private fun setReminder(cr: ContentResolver, eventID: Long) {
//        try {
//
//            val timeBefore = when (0) {
//                0 -> 15
//                else -> 0
//            }
//
//            val values = ContentValues()
//            values.put(CalendarContract.Reminders.MINUTES, timeBefore)
//            values.put(CalendarContract.Reminders.EVENT_ID, eventID)
//            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
//            cr.insert(CalendarContract.Reminders.CONTENT_URI, values)
//            val c = CalendarContract.Reminders.query(
//                cr,
//                eventID,
//                arrayOf(CalendarContract.Reminders.MINUTES)
//            )
//            if (c.moveToFirst()) {
////                println("calendar" + c.getInt(c.getColumnIndex(CalendarContract.Reminders.MINUTES)))
//            }
//            c.close()
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
}