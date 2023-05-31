package com.ennovations.fitcrutrainer.fragments

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.SystemClock
import android.os.Vibrator
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.CallActivity
import com.fitness.fitnessCru.fragment.IncomeCallFragmentCallbackListener
import com.fitness.fitnessCru.quickbox.db.QbUsersDbManager
import com.fitness.fitnessCru.quickbox.utils.RingtonePlayer
import com.fitness.fitnessCru.quickbox.utils.WebRtcSessionManager
import com.fitness.fitnessCru.quickbox.utils.getColorCircleDrawable
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.AvailableTimeSlotResponse
import com.fitness.fitnessCru.utility.CNAME
import com.fitness.fitnessCru.utility.IMAGE
import com.fitness.fitnessCru.utility.NAME
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.AppointmentViewModel
import com.fitness.fitnessCru.vm_factory.AppointmentVMFactory
import com.orhanobut.hawk.Hawk
import com.quickblox.chat.QBChatService
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.core.helper.StringifyArrayList
import com.quickblox.core.request.GenericQueryRule
import com.quickblox.core.request.QBPagedRequestBuilder
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import com.quickblox.videochat.webrtc.QBRTCSession
import com.quickblox.videochat.webrtc.QBRTCTypes
import java.io.Serializable
import java.util.concurrent.TimeUnit

private const val PER_PAGE_SIZE_100 = 100
private const val ORDER_RULE = "order"
private const val ORDER_DESC_UPDATED = "desc string updated_at"

class IncomeCallFragment : Fragment(), Serializable, View.OnClickListener {
    private val TAG = IncomeCallFragment::class.java.simpleName
    private val CLICK_DELAY = TimeUnit.SECONDS.toMillis(2)

    //Views
    private lateinit var callTypeTextView: TextView
    private lateinit var rejectButton: ImageButton
    private lateinit var takeButton: ImageButton
    private lateinit var alsoOnCallText: TextView
    private lateinit var progressUserName: ProgressBar

    private lateinit var callerNameTextView: TextView
    private var otherUsersTextView: TextView? = null
    private var opponentIds: List<Int>? = null
    private var vibrator: Vibrator? = null
    private var conferenceType: QBRTCTypes.QBConferenceType? = null
    private var lastClickTime = 0L
    private lateinit var ringtonePlayer: RingtonePlayer
    private lateinit var incomeCallFragmentCallbackListener: IncomeCallFragmentCallbackListener

    private var currentSession: QBRTCSession? = null

    private lateinit var repository: Repository
    private lateinit var factory: AppointmentVMFactory
    private lateinit var viewModel: AppointmentViewModel

    private var coachname = String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            incomeCallFragmentCallbackListener = activity as IncomeCallFragmentCallbackListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity?.toString() + " must implement OnCallEventsController")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        retainInstance = true

        Log.d(TAG, "onCreate() from IncomeCallFragment")
        super.onCreate(savedInstanceState)
        // longToast("IncomeCallFragment")
        /*    val userInfo: HashMap<String, String> = (currentSession?.userInfo as HashMap<String, String>?)!!
            val userin= userInfo.get("name")
            val userim= userInfo.get("image")
            Hawk.put(NAME,userin)
            Hawk.put(IMAGE,userim)*/


        repository = Repository()

        factory = AppointmentVMFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AppointmentViewModel::class.java]
        viewModel.getCoachList()

    }

    override fun onResume() {
        super.onResume()
        // shortToast("onResumeIncoming")
        val userInfo: HashMap<String, String> =
            (currentSession?.userInfo as HashMap<String, String>?)!!
        val userin = userInfo.get("name")
        val userim = userInfo.get("image")
        Hawk.put(NAME, userin)
        Hawk.put(IMAGE, userim)
        (requireActivity() as CallActivity).addUpdateOpponentsListener(
            UpdateOpponentsListenerImpl(
                TAG
            )
        )
    }

    override fun onPause() {
        super.onPause()
        // shortToast("onPauseIncoming")
        val userInfo: HashMap<String, String> =
            (currentSession?.userInfo as HashMap<String, String>?)!!
        val userin = userInfo.get("name")
        val userim = userInfo.get("image")
        Hawk.put(NAME, userin)
        Hawk.put(IMAGE, userim)
        (requireActivity() as CallActivity).removeUpdateOpponentsListener(
            UpdateOpponentsListenerImpl(TAG)
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_income_call, container, false)
        currentSession = WebRtcSessionManager.getCurrentSession()


        val userInfo: HashMap<String, String> =
            (currentSession?.userInfo as HashMap<String, String>?)!!
        val userin = userInfo.get("name")
        val userim = userInfo.get("image")
        Hawk.put(NAME, userin)
        Hawk.put(IMAGE, userim)


        /*   val userInfo2: HashMap<String, String> = (currentSession?.userInfo as HashMap<String, String>?)!!
           val userim= userInfo2.get("image")
           Hawk.put(IMAGE,userim)*/

        //userInfo.get("image")
        Log.v("hii", userInfo.get("name").toString())
        Log.v("hi", userInfo.get("image").toString())

        initFields()
        hideToolBar()

        conferenceType?.let {

            initUI(view)
            setDisplayedTypeCall(it)
            initButtonsListener()

        }


        val context = activity as Context
        ringtonePlayer = RingtonePlayer(context)
        return view

    }


    private fun initFields() {


        currentSession?.let {

            opponentIds = it.opponents
            conferenceType = it.conferenceType
            Log.d(TAG, conferenceType.toString() + "From onCreateView()")

        }

    }

    private fun hideToolBar() {
        val toolbar: Toolbar? = activity?.findViewById(R.id.toolbar_call)
        toolbar?.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        startCallNotification()
    }

    private fun initButtonsListener() {
        rejectButton.setOnClickListener(this)
        takeButton.setOnClickListener(this)
    }

    private fun initUI(view: View) {

        val userInfo: HashMap<String, String> =
            (currentSession?.userInfo as HashMap<String, String>?)!!
        val userin = userInfo.get("name")
        val userim = userInfo.get("image")
        Hawk.put(NAME, userin)
        Hawk.put(IMAGE, userim)


        //userInfo.get("image")
        Log.v("hii", userInfo.get("name").toString())
        Log.v("hi", userInfo.get("image").toString())
        callTypeTextView = view.findViewById(R.id.call_type)
        val callerAvatarImageView: ImageView = view.findViewById(R.id.image_caller_avatar)
        callerNameTextView = view.findViewById(R.id.text_caller_name)
        otherUsersTextView = view.findViewById(R.id.text_other_users)
        progressUserName = view.findViewById(R.id.progress_bar_opponent_name)
        alsoOnCallText = view.findViewById(R.id.text_also_on_call)
        rejectButton = view.findViewById(R.id.image_button_reject_call)
        takeButton = view.findViewById(R.id.image_button_accept_call)

        //val userInfo: Map<String, String> = currentSession?.getUserInfo()!!

        currentSession?.let {
            /* if(data!=null){

             }*/
            callerAvatarImageView.setBackgroundDrawable(getBackgroundForCallerAvatar(it.callerID))
            //  callerAvatarImageView.setImageResource(userim).toString()
            /* callerAvatarImageView.setBackgroundDrawable(userimage?.let { it1 ->
                 getBackgroundForCallerAvatar(
                     it1
                 )
             })*/
            Glide.with(requireContext()).load(userim)
                .placeholder(R.drawable.place_holder)
                .into(callerAvatarImageView)

        }

        //  callerNameTextView.visibility = View.VISIBLE

        val callerUser = QbUsersDbManager.getUserById(currentSession?.callerID)

        callerNameTextView.text = coachname.toString()




        if (callerUser != null) {
            val name = callerUser.fullName ?: callerUser.login
            //val name = callerUser.fullName ?: callerUser.login
            viewModel.coachList.observe(viewLifecycleOwner) {

                if (it.isSuccessful && it.body() != null) {
                    val data = it.body()!!.data
                    for (i in data!!) {
                        if (i.coach_email == name) {

                            callerNameTextView.text = i.coach_name

                            progressUserName.visibility = View.GONE
                            callerNameTextView.visibility = View.VISIBLE

                            Hawk.put(CNAME, i.coach_name)
                            Glide.with(requireContext()).load(i.image_url)
                                .placeholder(R.drawable.place_holder)
                                .into(callerAvatarImageView)
                        }
                    }
                } else if (!it.isSuccessful && it.errorBody() != null) {
                    Util.error(it.errorBody(), AvailableTimeSlotResponse::class.java).message
                } else "Something went wrong"
            }

            //    callerNameTextView.text = name


            // callerNameTextView.text = userin
//            callerNameTextView.text = name
        } else {
            //  callerNameTextView.text = coachname.toString()
            callerNameTextView.text = currentSession?.callerID.toString()
            updateUserFromServer()
        }

        otherUsersTextView?.text = getOtherIncUsersNames()

        setVisibilityAlsoOnCallTextView()
    }

    private fun updateUserFromServer() {
        progressUserName.visibility = View.VISIBLE

        // val userInfo: Map<String, String> = currentSession?.getUserInfo()!!
        val userInfo: HashMap<String, String> =
            (currentSession?.userInfo as HashMap<String, String>?)!!
        val userin = userInfo.get("name")
        val userim = userInfo.get("image")
        Hawk.put(NAME, userin)
        Hawk.put(IMAGE, userim)

        //userInfo.get("image")
        Log.v("hii", userInfo.get("name").toString())

        currentSession?.let {
            QBUsers.getUser(it.callerID).performAsync(object : QBEntityCallback<QBUser> {
                override fun onSuccess(qbUser: QBUser?, bundle: Bundle?) {
                    if (qbUser != null) {
                        QbUsersDbManager.saveUser(qbUser)
                        val callerName =
                            if (TextUtils.isEmpty(qbUser.fullName)) qbUser.login else qbUser.fullName
                        callerNameTextView.text = userin

                    }

                    progressUserName.visibility = View.GONE
                }

                override fun onError(e: QBResponseException?) {
                    progressUserName.visibility = View.GONE
                    e?.printStackTrace()
                }
            })
        }

        val rules = ArrayList<GenericQueryRule>()
        rules.add(GenericQueryRule(ORDER_RULE, ORDER_DESC_UPDATED))
        val requestBuilder = QBPagedRequestBuilder()
        requestBuilder.rules = rules
        requestBuilder.perPage = PER_PAGE_SIZE_100

        QBUsers.getUsers(requestBuilder).performAsync(object : QBEntityCallback<ArrayList<QBUser>> {
            override fun onSuccess(users: ArrayList<QBUser>, params: Bundle?) {
                QbUsersDbManager.saveAllUsers(users, true)
                val callerUser: QBUser? = QbUsersDbManager.getUserById(currentSession?.callerID)
                if (callerUser != null) {
                    val name = callerUser.fullName ?: callerUser.login
                    callerNameTextView.text = name
                }
                progressUserName.visibility = View.GONE
            }

            override fun onError(e: QBResponseException?) {
                progressUserName.visibility = View.GONE
            }
        })
    }

    private fun setVisibilityAlsoOnCallTextView() {
        opponentIds?.let {
            if (it.size < 2) {
                alsoOnCallText.visibility = View.INVISIBLE
            }
        }
    }

    private fun getBackgroundForCallerAvatar(callerId: Int): Drawable {
        return getColorCircleDrawable(callerId)
    }


    private fun startCallNotification() {
        Log.d(TAG, "startCallNotification()")

        ringtonePlayer.play(false)

        vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        val vibrationCycle = longArrayOf(0, 1000, 1000)
        vibrator?.hasVibrator()?.let {
            vibrator?.vibrate(vibrationCycle, 1)
        }
    }

    private fun stopCallNotification() {
        Log.d(TAG, "stopCallNotification()")

        ringtonePlayer.stop()
        vibrator?.cancel()
    }

    private fun getOtherIncUsersNames(): String {
        var result = ""
        opponentIds?.let {
            val usersFromDb = QbUsersDbManager.getUsersByIds(it)
            val opponents = ArrayList<QBUser>()
            opponents.addAll(getAllUsersFromIds(usersFromDb, it))

            opponents.remove(QBChatService.getInstance().user)
            Log.d(TAG, "opponentIds = $opponentIds")
            result = makeStringFromUsersFullNames(opponents)
        }
        return result
    }

    fun makeStringFromUsersFullNames(allUsers: ArrayList<QBUser>): String {
        val usersNames = StringifyArrayList<String>()

        for (usr in allUsers) {
            if (usr.fullName != null) {
                usersNames.add(usr.fullName)
            } else if (usr.id != null) {
                usersNames.add(usr.id.toString())
            }
        }
        return usersNames.itemsAsString.replace(",", ", ")
    }

    private fun getAllUsersFromIds(
        existedUsers: ArrayList<QBUser>,
        allIds: List<Int>
    ): ArrayList<QBUser> {
        val qbUsers = ArrayList<QBUser>()

        for (userId in allIds) {
            val stubUser = createStubUserById(userId)
            if (!existedUsers.contains(stubUser)) {
                qbUsers.add(stubUser)
            }
        }

        qbUsers.addAll(existedUsers)
        return qbUsers
    }

    private fun createStubUserById(userId: Int?): QBUser {
        val stubUser = QBUser(userId)
        stubUser.fullName = userId.toString()
        return stubUser
    }

    private fun setDisplayedTypeCall(conferenceType: QBRTCTypes.QBConferenceType) {


        val isVideoCall = conferenceType == QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO

        currentSession = WebRtcSessionManager.getCurrentSession()


        val userInfo: HashMap<String, String> =
            (currentSession?.userInfo as HashMap<String, String>?)!!
        val userin = userInfo.get("name")
        val userim = userInfo.get("image")
        Hawk.put(NAME, userin)
        Hawk.put(IMAGE, userim)

        val callType = if (isVideoCall) {
            R.string.text_incoming_video_call
        } else {
            R.string.text_incoming_audio_call
        }
        callTypeTextView.setText(callType)

        val imageResource = if (isVideoCall) {
            R.drawable.ic_video_white
        } else {
            R.drawable.ic_call
        }
        takeButton.setImageResource(imageResource)
    }

    override fun onStop() {
        stopCallNotification()
        super.onStop()
        Log.d(TAG, "onStop() from IncomeCallFragment")
    }

    override fun onClick(v: View) {
        if (SystemClock.uptimeMillis() - lastClickTime < CLICK_DELAY) {
            return
        }
        lastClickTime = SystemClock.uptimeMillis()

        when (v.id) {
            R.id.image_button_reject_call -> reject()
            R.id.image_button_accept_call -> accept()
            else -> {
            }
        }
    }

    private fun accept() {
        enableButtons(false)
        stopCallNotification()

        incomeCallFragmentCallbackListener.onAcceptCurrentSession()
        Log.d(TAG, "Call is started")
    }

    private fun reject() {
        enableButtons(false)
        stopCallNotification()

        incomeCallFragmentCallbackListener.onRejectCurrentSession()
        Log.d(TAG, "Call is rejected")
    }

    private fun enableButtons(enable: Boolean) {
        takeButton.isEnabled = enable
        rejectButton.isEnabled = enable
    }

    private inner class UpdateOpponentsListenerImpl(val tag: String?) :
        CallActivity.UpdateOpponentsListener {
        override fun updatedOpponents(updatedOpponents: ArrayList<QBUser>) {
            otherUsersTextView?.text = getOtherIncUsersNames()
        }

        override fun equals(other: Any?): Boolean {
            if (other is UpdateOpponentsListenerImpl) {
                return tag == other.tag
            }
            return false
        }

        override fun hashCode(): Int {
            var hash = 1
            hash = 31 * hash + tag.hashCode()
            return hash
        }
    }
}
