package com.fitness.fitnessCru.utility

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.network.API
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.regex.Pattern


class Util {
    fun validEmail(email: String?): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    companion object {
        fun isValid(password: String?): Boolean {
            val PASSWORD_PATTERN =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$"
            val pattern = Pattern.compile(PASSWORD_PATTERN)
            val matcher = pattern.matcher(password)
            return matcher.matches()
        }

        fun toast(context: Context, string: String) =
            Toast.makeText(context, string, Toast.LENGTH_LONG).show()

        fun <T> error(error: ResponseBody?, type: Class<T>?): T {
            return Gson().fromJson(error!!.charStream(), type)
        }

        @SuppressLint("LongLogTag")
        fun <T> print(body: Response<T>): Response<T> {
            //Log.e(TAG, "${body.code()} <<<<<< ${Gson().toJson(body.body() ?: body.errorBody())}")
            return body
        }

        fun loadImage(
            context: Context,
            imageView: ImageView,
            imageURL: String?,
            imageName: String?
        ) {
            Glide.with(context).load(API.IMAGE_BASE_URL + imageURL + imageName)
                .placeholder(context.resources.getDrawable(R.drawable.place_holder, null))
                .into(imageView)
        }

        fun loadImage(
            context: Context,
            imageView: ImageView,
            imageURL: String?,
        ) {
            Glide.with(context).load(imageURL)
                .placeholder(context.resources.getDrawable(R.drawable.place_holder, null))
                .into(imageView)
        }

        fun sendMessage(context: Context, message: String) {
            try {
                val uri =
                    Uri.parse("whatsapp://send?phone=${context.getString(R.string.whatsapp_mobile_no)}")
                val i = Intent(Intent.ACTION_VIEW, uri)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(i)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Toast.makeText(context, "WhatsApp not installed.", Toast.LENGTH_SHORT).show()
            }
        }

        fun getVideoId(videoUrl: String): String {
            var videoId = ""
            val regex =
                "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)"
            val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(videoUrl)
            if (matcher.find()) {
                videoId = matcher.group(1)
            }
            return videoId!!
        }

        fun checkUrl(url: String): Boolean {
            val pattern = ".*(youtube|youtu.be).*"
            val compiledPattern = Pattern.compile(pattern)
            var matcher = compiledPattern.matcher(url)
            return matcher.find()
        }

        fun recyclerSlideIssueFix(recyclerview: RecyclerView) {
            val listener = object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val action = e.action
                    return if (recyclerview.canScrollHorizontally(RecyclerView.FOCUS_FORWARD)) {
                        when (action) {
                            MotionEvent.ACTION_MOVE -> rv.parent
                                .requestDisallowInterceptTouchEvent(true)
                        }
                        false
                    } else if (recyclerview.canScrollHorizontally(RecyclerView.FOCUS_BACKWARD)) {
                        when (action) {
                            MotionEvent.ACTION_MOVE -> rv.parent
                                .requestDisallowInterceptTouchEvent(true)
                        }
                        false
                    } else {
                        when (action) {
                            MotionEvent.ACTION_MOVE -> rv.parent
                                .requestDisallowInterceptTouchEvent(false)
                        }
                        recyclerview.removeOnItemTouchListener(this)
                        true
                    }
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            }
            recyclerview.addOnItemTouchListener(listener)
        }
    }
}

object Utils {
    fun validEmail(email: String?): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun isValidPass(password: String?): Boolean {
        val PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$"
        val pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
}
