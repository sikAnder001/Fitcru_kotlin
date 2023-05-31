package com.fitness.fitnessCru.fragment

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.CallActivity
import com.fitness.fitnessCru.quickbox.utils.SharedPrefsHelper
import com.fitness.fitnessCru.quickbox.utils.WebRtcSessionManager
import com.fitness.fitnessCru.quickbox.utils.getColorCircleDrawable
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.CNAME
import com.fitness.fitnessCru.utility.IMAGE
import com.fitness.fitnessCru.utility.NAME
import com.fitness.fitnessCru.viewmodel.AppointmentViewModel
import com.orhanobut.hawk.Hawk
import com.quickblox.core.helper.StringifyArrayList
import com.quickblox.users.model.QBUser
import com.quickblox.videochat.webrtc.AppRTCAudioManager
import com.quickblox.videochat.webrtc.QBRTCSession

const val SPEAKER_ENABLED = "is_speaker_enabled"

class AudioConversationFragment : BaseConversationFragment(), CallActivity.OnChangeAudioDevice {
    private val TAG = AudioConversationFragment::class.simpleName

    private lateinit var audioSwitchToggleButton: ToggleButton
    private lateinit var alsoOnCallText: TextView
    private lateinit var firstOpponentNameTextView: TextView
    private lateinit var otherOpponentsTextView: TextView
    private var currentSession: QBRTCSession? = null

    private lateinit var repository: Repository
    private lateinit var viewModel: AppointmentViewModel


    override fun onStart() {
        super.onStart()
        conversationFragmentCallback?.addOnChangeAudioDeviceListener(this)
    }

    override fun onResume() {
        super.onResume()
        conversationFragmentCallback?.addCallTimeUpdateListener(CallTimeUpdateListenerImpl(TAG))
        conversationFragmentCallback?.addUpdateOpponentsListener(UpdateOpponentsListenerImpl(TAG))
    }

    override fun onPause() {
        super.onPause()
        conversationFragmentCallback?.removeCallTimeUpdateListener(CallTimeUpdateListenerImpl(TAG))
        conversationFragmentCallback?.removeUpdateOpponentsListener(UpdateOpponentsListenerImpl(TAG))
    }

    override fun configureOutgoingScreen() {
        val context: Context = activity as Context
        outgoingOpponentsRelativeLayout.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.black
            )
        )
        allOpponentsTextView.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.text_color_outgoing_opponents_names_audio_call
            )
        )
        ringingTextView.setTextColor(ContextCompat.getColor(context, R.color.text_color_call_type))
    }

    override fun configureToolbar() {
        toolbar?.visibility = View.GONE
        toolbar?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        toolbar?.setTitleTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.toolbar_title_color
            )
        )
        toolbar?.setSubtitleTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.toolbar_subtitle_color
            )
        )
    }

    override fun configureActionBar() {
        actionBar.subtitle =
            String.format(getString(R.string.subtitle_text_logged_in_as), currentUser.fullName)
    }

    override fun initViews(view: View?) {
        super.initViews(view)
        if (view == null) {
            return
        }

        timerCallText = view.findViewById(R.id.timer_call)
        val firstOpponentAvatarImageView = view.findViewById<ImageView>(R.id.image_caller_avatar)
        firstOpponentAvatarImageView?.setBackgroundDrawable(getColorCircleDrawable(opponents[0].id))
        currentSession = WebRtcSessionManager.getCurrentSession()
        val userInfo: HashMap<String, String> =
            (currentSession?.userInfo as HashMap<String, String>?)!!
        val userin = userInfo.get("name")
        val userim = userInfo.get("image")
        Hawk.put(NAME, userin)
        Hawk.put(IMAGE, userim)


        Glide.with(requireContext()).load(userim)
            .placeholder(R.drawable.place_holder)
            .into(firstOpponentAvatarImageView)

        alsoOnCallText = view.findViewById(R.id.text_also_on_call)
        setVisibilityAlsoOnCallTextView()

        firstOpponentNameTextView = view.findViewById(R.id.text_caller_name)

        /*Log.v("hii", userInfo.get("name").toString())
        Log.v("hi", userInfo.get("image").toString())*/
        val name = opponents[0].fullName ?: opponents[0].login
        firstOpponentNameTextView.text = Hawk.get(CNAME)
/*
        viewModel.coachList.observe(viewLifecycleOwner) {

            if (it.isSuccessful && it.body() != null) {
                val data= it.body()!!.data
                for (i in data!!){
                    if(i.coach_email ==name){
                        firstOpponentNameTextView.text = i.coach_name
                        Hawk.put(CNAME,i.coach_name)
                        Glide.with(requireContext()).load(i.image_url)
                            .placeholder(R.drawable.place_holder)
                            .into(firstOpponentAvatarImageView)
                    }
                }
            } else if (!it.isSuccessful && it.errorBody() != null) {
                Util.error(it.errorBody(), AvailableTimeSlotResponse::class.java).message
            } else "Something went wrong"
        }
*/

        otherOpponentsTextView = view.findViewById(R.id.text_other_users)
        otherOpponentsTextView.text = getOtherOpponentNames()

        audioSwitchToggleButton = view.findViewById(R.id.toggle_speaker)
        audioSwitchToggleButton.visibility = View.VISIBLE
        audioSwitchToggleButton.isChecked = SharedPrefsHelper.get(SPEAKER_ENABLED, true)
        actionButtonsEnabled(false)

        if (conversationFragmentCallback?.isConnectedCall() == true) {
            startedCall()
        }
    }

    private fun setVisibilityAlsoOnCallTextView() {
        if (opponents.size < 2) {
            alsoOnCallText.visibility = View.INVISIBLE
        }
    }

    private fun getOtherOpponentNames(): String {
        val otherOpponents = ArrayList<QBUser>()
        otherOpponents.addAll(opponents)
        otherOpponents.removeAt(0)
        return makeStringFromUsersFullNames(otherOpponents)
    }

    private fun makeStringFromUsersFullNames(allUsers: ArrayList<QBUser>): String {
        val usersNames = StringifyArrayList<String>()
        for (user in allUsers) {
            if (user.fullName != null) {
                usersNames.add(user.fullName)
            } else if (user.id != null) {
                usersNames.add(user.id.toString())
            }
        }
        return usersNames.itemsAsString.replace(",", ", ")
    }

    override fun onStop() {
        super.onStop()
        conversationFragmentCallback?.removeOnChangeAudioDeviceListener(this)
    }

    override fun initButtonsListener() {
        super.initButtonsListener()
        audioSwitchToggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            SharedPrefsHelper.save(SPEAKER_ENABLED, isChecked)
            conversationFragmentCallback?.onSwitchAudio()
        }
    }

    override fun actionButtonsEnabled(inability: Boolean) {
        super.actionButtonsEnabled(inability)
        audioSwitchToggleButton.isActivated = inability
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_audio_conversation
    }

    override fun audioDeviceChanged(newAudioDevice: AppRTCAudioManager.AudioDevice) {
        audioSwitchToggleButton.isChecked =
            newAudioDevice != AppRTCAudioManager.AudioDevice.SPEAKER_PHONE
    }

    private inner class UpdateOpponentsListenerImpl(val tag: String?) :
        CallActivity.UpdateOpponentsListener {

        val userInfo: HashMap<String, String> =
            (currentSession?.userInfo as HashMap<String, String>?)!!
        // val userim= userInfo.get("image")

        override fun updatedOpponents(updatedOpponents: ArrayList<QBUser>) {
            val name = opponents[0].fullName ?: opponents[0].login
            firstOpponentNameTextView.text = Hawk.get(CNAME)
            otherOpponentsTextView.text = getOtherOpponentNames()
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

    private inner class CallTimeUpdateListenerImpl(val tag: String?) :
        CallActivity.CallTimeUpdateListener {
        override fun updatedCallTime(time: String) {
            timerCallText.text = time
        }

        override fun equals(other: Any?): Boolean {
            if (other is CallTimeUpdateListenerImpl) {
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