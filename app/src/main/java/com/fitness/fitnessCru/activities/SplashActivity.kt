package com.fitness.fitnessCru.activities


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivitySplashBinding
import com.fitness.fitnessCru.quickbox.utils.SharedPrefsHelper
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.*
import com.fitness.fitnessCru.viewmodel.UserDetailsViewModel
import com.fitness.fitnessCru.vm_factory.UserDetailsVMFactory
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_splash.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class SplashActivity : FitcruBaseActivity() {
    //splash
    private val OVERLAY_PERMISSION_CHECKED_KEY = "overlay_checked"
    private val MI_OVERLAY_PERMISSION_CHECKED_KEY = "mi_overlay_checked"
    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1764

    private var tokenCheck: String? = null


    private var backPress = 0L
    lateinit var instance: Session
    lateinit var deviceToken: String

    @SuppressLint("InvalidWakeLockTag")
    private var binding: ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        setContentView(R.layout.activity_splash);
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    deviceToken = token.toString()
                    Hawk.put(DEVICE_TOKEN, deviceToken)


                } else {
                    Log.v(
                        "LoginMainActivity",
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    deviceToken = ""
                }
                Hawk.put(FCM_TOKEN, deviceToken)

                Log.v("FCMToken", deviceToken)
            }
        tokenCheck = Session.getToken()


        instance = Session.getInstance()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        tokenCheck = Session.getToken()
        Log.v("hell", tokenCheck.toString())

        val video: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.new_splash)
        binding!!.videoview.setVideoURI(video)
        binding!!.videoview.setOnCompletionListener(object : OnCompletionListener {
            override fun onCompletion(mp: MediaPlayer) {

                jump()

            }
        })




        binding!!.videoview.start()


        val textShader: Shader = LinearGradient(
            510f, 400f, textcontinue.textSize, textcontinue.textSize, intArrayOf(
                Color.parseColor("#FF6105"),
                Color.parseColor("#FF02BD")
            ), null, Shader.TileMode.CLAMP
        )
        textcontinue.paint.shader = textShader

        binding?.textcontinue?.setOnClickListener {
            if (checkOverlayPermissions()) {
                if (Hawk.contains(ACCESS_TOKEN)) {
                    Hawk.put(DEVICE_TOKEN, deviceToken)
                    val intent: Intent = Intent(this@SplashActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    OpponentsActivity.start(this@SplashActivity)
                    finish()
                } else {
                    val intent: Intent =
                        Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }

        binding!!.loginTV.setOnClickListener {
            if (checkOverlayPermissions()) {


                if (Hawk.contains(ACCESS_TOKEN)) {

                    getUserDetail(Hawk.get(ACCESS_TOKEN))


                } else {

                    val intent: Intent =
                        Intent(this@SplashActivity, LoginMainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }

        }
        getExternalStoragePermission()
    }

    override fun initViews() {

    }

    override fun initControl() {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        jump()
        return true
    }

    private fun jump() {
        if (isFinishing) return
        binding!!.videoview.start()
    }

    companion object {
    }

    override fun onBackPressed() {

        if (backPress + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(applicationContext, "Press back again to exit app", Toast.LENGTH_SHORT)
                .show()
        }

        backPress = System.currentTimeMillis()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.e("TAG", "onActivityResult")

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (checkOverlayPermissions()) {
                runNextScreen()
            }
        }
    }

    private fun runNextScreen() {
        /* if (sharedPreference.isLogin) {
            startActivity(Intent(this,   MainActivity::class.java))
            finishAffinity()
        }else {
            val v = Intent(this, LoginMainActivity::class.java)
            startActivity(v)
            getExternalStoragePermission()
            finish()
        }*/
    }

    //permission
    private fun getExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //Permission not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                //Can ask user for permission

                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    101
                )

            } else {
                var userAskedPermissionBefore = false

                if (userAskedPermissionBefore == false) {
                    //If User was asked permission before and denied
                    var alertDialogBuilder = AlertDialog.Builder(this);

                    alertDialogBuilder.setTitle("Permission needed");
                    alertDialogBuilder.setMessage("Location permission Required");
                    alertDialogBuilder.setPositiveButton("Open Setting") { dialogInterface, which ->

                        var intent = Intent()
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        var uri = Uri.fromParts(
                            "package", this.packageName,
                            null
                        )
                        intent.data = uri
                        startActivity(intent)
                    }

                    //alertDialogBuilder.setNegativeButton("Cancel"){dialogInterface, which->
                    //    Log.d(ContentValues.TAG, "onClick: Cancelling");

                    //  }

                    //  var dialog = alertDialogBuilder.create();
                    //   dialog.show();
                } else {
                    //If user is asked permission for first time
                    ActivityCompat.requestPermissions(
                        this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        101
                    );


                }
            }

        }

    }


    private fun checkOverlayPermissions(): Boolean {
        Log.e("TAG", "Checking Permissions")
        val overlayChecked = SharedPrefsHelper.get(OVERLAY_PERMISSION_CHECKED_KEY, false)
        val miOverlayChecked = SharedPrefsHelper.get(MI_OVERLAY_PERMISSION_CHECKED_KEY, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this) && !overlayChecked) {
                Log.e("TAG", "Android Overlay Permission NOT Granted")
                buildOverlayPermissionAlertDialog()
                return false
            } else if (isMiUi() && !miOverlayChecked) {
                Log.e("TAG", "Xiaomi Device. Need additional Overlay Permissions")
                buildMIUIOverlayPermissionAlertDialog()
                return false
            }
        }
        Log.e("TAG", "All Overlay Permission Granted")
        SharedPrefsHelper.save(OVERLAY_PERMISSION_CHECKED_KEY, true)
        SharedPrefsHelper.save(MI_OVERLAY_PERMISSION_CHECKED_KEY, true)
        return true
    }

    fun isMiUi(): Boolean {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name")) ||
                !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.code"))
    }

    fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return line
    }

    private fun buildOverlayPermissionAlertDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setTitle("Overlay Permission Required")
        builder.setIcon(R.drawable.ic_error_outline_gray_24dp)
        builder.setMessage("To receive calls in background - \nPlease Allow overlay permission in Android Settings")
        builder.setCancelable(false)

        builder.setNeutralButton("No") { dialog, which ->
            showToast("You might miss calls while your application in background")
            SharedPrefsHelper.save(OVERLAY_PERMISSION_CHECKED_KEY, true)
        }

        builder.setPositiveButton("Settings") { dialog, which ->
            showAndroidOverlayPermissionsSettings()
        }

        val alertDialog = builder.create()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.create()
            alertDialog.show()
        }
    }

    private fun showAndroidOverlayPermissionsSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this@SplashActivity)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:" + applicationContext.packageName)
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
        } else {
            Log.d("TAG", "Application Already has Overlay Permission")
        }
    }

    fun buildMIUIOverlayPermissionAlertDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Additional Overlay Permission Required")
        builder.setIcon(R.drawable.ic_error_outline_orange_24dp)
        builder.setMessage("Please make sure that all additional permissions granted")
        builder.setCancelable(false)

        builder.setNeutralButton("I'm sure") { dialog, which ->
            SharedPrefsHelper.save(MI_OVERLAY_PERMISSION_CHECKED_KEY, true)
            runNextScreen()
        }

        builder.setPositiveButton("Mi Settings") { dialog, which ->
            showMiUiPermissionsSettings()
        }

        val alertDialog = builder.create()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.create()
            alertDialog.show()
        }
    }

    private fun showMiUiPermissionsSettings() {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.setClassName(
            "com.miui.securitycenter",
            "com.miui.permcenter.permissions.PermissionsEditorActivity"
        )
        intent.putExtra("extra_pkgname", packageName)
        startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
    }

    private fun getUserDetail(accessToken: String) {
        val repository by lazy { Repository() }
        val factory by lazy { UserDetailsVMFactory(repository) }
        val viewModel by lazy {
            ViewModelProvider(
                this,
                factory
            )[UserDetailsViewModel::class.java]
        }
        // loading.showProgress()
        viewModel.getUserDetails()
        viewModel.response.observe(this@SplashActivity) {

            //  loading.hideProgress()
            if (it.isSuccessful && it.code() == 200) {
                val data = it.body()!!.data
                /* if (data.name != null || data.email != null)
                     Session.destroySession()
                 Session.setUserDetails(data)
                 Session.setToken(accessToken)*/


                val intent: Intent =
                    Intent(this@SplashActivity, DashboardActivity::class.java)
                OpponentsActivity.start(this@SplashActivity)
                Hawk.put(DEVICE_TOKEN, deviceToken)


                // showToast("DashBoard with saved token")
                startActivity(intent)
                finish()


            } else {
                // showToast("NewTokenCreated")
                val intent: Intent =
                    Intent(this@SplashActivity, LoginMainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


}