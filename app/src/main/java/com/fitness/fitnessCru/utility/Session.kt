package com.fitness.fitnessCru.utility


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.databinding.library.baseAdapters.BuildConfig
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.quickbox.db.DbHelper
import com.fitness.fitnessCru.response.Data
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import com.quickblox.auth.session.QBSettings
import timber.log.Timber

//User default credentials
const val DEFAULT_USER_PASSWORD = "quickblox"

/**
 * Credentials I have used

static let applicationID:UInt = 99297
static let authKey = "kH7EdmLsByMjqYX"
static let authSecret = "czG5A7QWftLmADV"
static let accountKey = "Q6vE-2Rt3hpCXPpHrL4f"
Shivam please use these credentials for quickblox so that ios and android both will be on same sync
 */

//App credentials
/*private const val APPLICATION_ID = "98837"
private const val AUTH_KEY = "C8ZZV6OHBpYMePg"
private const val AUTH_SECRET = "vxvkUzHS3HFfNV3"
private const val ACCOUNT_KEY = "xdJ3K9AUXznLpoT4sCPh"*/

private const val APPLICATION_ID = "99297"
private const val AUTH_KEY = "kH7EdmLsByMjqYX"
private const val AUTH_SECRET = "czG5A7QWftLmADV"
private const val ACCOUNT_KEY = "Q6vE-2Rt3hpCXPpHrL4f"

class Session : Application() {
    private lateinit var dbHelper: DbHelper

    /**
     * INITIALIZATION FOR SESSION
     */


    companion object {
        private var context: Context? = null
        private var editor: SharedPreferences.Editor? = null
        private var auth: Session? = null
        private var preferences: SharedPreferences? = null


        /**
         * DESTROY SESSION OR LOGOUT
         */
        fun destroySession() {
            editor!!.clear()
            editor!!.commit()
            editor!!.apply()
        }

        fun setUserDetails(value: Any): Any {
            return saveObject(Constants.USER_DETAIL, value)
        }

        fun getUserDetails(): Data {
            return Gson().fromJson(getString(Constants.USER_DETAIL), Data::class.java)
        }

        fun setToken(token: String): String {
            return saveString(Constants.TOKEN, "Bearer $token")
        }

        fun getToken() = getString(Constants.TOKEN)

        fun setSteps(step: String): String {
            return saveString(Constants.STEPS, step.ifEmpty { "0" })
        }

        fun getSteps() = getString(Constants.STEPS)

        fun setCal(calorie: String): String {
            return saveString(Constants.CALORIE, calorie.ifEmpty { "0" })
        }

        fun getCal() = getString(Constants.CALORIE)

        fun getSyncTime() = getString(Constants.SYNC_TIME)

        fun setSyncTime(time: String): String {
            return saveString(Constants.SYNC_TIME, time)
        }

        private fun saveString(key: String, value: String): String {
            editor!!.putString(key, value)
            editor!!.commit()
            editor!!.apply()
            return value
        }

        private fun saveObject(key: String, value: Any): Any {
            editor!!.putString(key, Gson().toJson(value))
            editor!!.commit()
            editor!!.apply()
            return value
        }

        private fun saveBoolean(key: String, value: Boolean): Boolean {
            editor!!.putBoolean(key, value)
            editor!!.commit()
            editor!!.apply()
            return value
        }

        /**
         * GET VALUE IN SESSION
         */
        private fun getBoolean(key: String): Boolean {
            return preferences!!.getBoolean(key, false)
        }

        private fun getString(key: String): String? {
            return preferences!!.getString(key, "{}")
        }

        private lateinit var instance: Session

        @Synchronized
        fun getInstance(): Session = instance


        fun setEmailForCreate(email: String): String {
            return saveString(Constants.QBEMAIL, email)
        }

        fun getEmailForCreate() = getString(Constants.QBEMAIL)


        fun setMobileForCreate(mobile: String): String {
            return saveString(Constants.QBMOBILE, mobile)
        }

        fun getMobileForCreate() = getString(Constants.QBMOBILE)


    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        dbHelper = DbHelper(this)
        Hawk.init(this).build()
        initFabric()
        checkCredentials()
        initCredentials()
        context = baseContext
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        if (auth == null) {
            auth = Session()
            preferences = context!!.getSharedPreferences("FIT_CRU", MODE_PRIVATE)
            editor = preferences!!.edit()
        }


    }

    private fun initFabric() {
        if (!com.quickblox.BuildConfig.DEBUG) {
            //Fabric.with(this, Crashlytics())
        }
    }

    private fun checkCredentials() {
        if (APPLICATION_ID.isEmpty() || AUTH_KEY.isEmpty() || AUTH_SECRET.isEmpty() || ACCOUNT_KEY.isEmpty()) {
            throw AssertionError(getString(R.string.error_qb_credentials_empty))
        }
    }

    private fun initCredentials() {
        QBSettings.getInstance().init(
            applicationContext,
            APPLICATION_ID,
            AUTH_KEY,
            AUTH_SECRET
        )
        QBSettings.getInstance().accountKey = ACCOUNT_KEY


    }

    @Synchronized
    fun getDbHelper(): DbHelper {
        return dbHelper
    }

}