package com.fitness.fitnessCru.activities

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.view.KeyEvent
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.quickbox.utils.showErrorSnackbar

abstract class BaseActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null

    internal fun showProgressDialog(@StringRes messageId: Int) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
            progressDialog?.isIndeterminate = true
            progressDialog?.setCancelable(false)
            progressDialog?.setCanceledOnTouchOutside(false)
            progressDialog?.setOnKeyListener(KeyEventListener())
        }
        progressDialog?.setMessage(getString(messageId))
        progressDialog?.show()
    }

    override fun onPause() {
        super.onPause()
        hideProgressDialog()
    }

    internal fun hideProgressDialog() {
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }

    protected fun showErrorSnackbar(
        @StringRes resId: Int,
        e: Exception?,
        clickListener: View.OnClickListener
    ) {
        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        showErrorSnackbar(rootView, resId, e, R.string.dlg_retry, clickListener)
    }

    protected fun checkPermissions(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (checkPermission(permission)) {
                return true
            }
        }
        return false
    }

    protected fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_DENIED
    }

    inner class KeyEventListener : DialogInterface.OnKeyListener {
        override fun onKey(dialog: DialogInterface?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
            return keyCode == KeyEvent.KEYCODE_BACK
        }
    }
}