package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.fitness.fitnessCru.databinding.ActivityPasswordBinding
import com.fitness.fitnessCru.utility.OnClick

class PasswordActivity : AppCompatActivity() {
    private var passwordBinding: ActivityPasswordBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        passwordBinding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(passwordBinding!!.root)
        //keyboard code start
        passwordBinding!!.passwordEt.showSoftInputOnFocus = false
        passwordBinding!!.passwordEt.setRawInputType(InputType.TYPE_CLASS_TEXT)
        passwordBinding!!.passwordEt.setTextIsSelectable(true)
        val ic: InputConnection = passwordBinding!!.passwordEt.onCreateInputConnection(EditorInfo())
        passwordBinding!!.keyboard.setInputConnection(ic)
        visibleKeyboard()
        forgotPass()
        proceed()
        //keyboard code end
    }

    private fun proceed() {
        passwordBinding!!.proceedBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val userPass: String =
                    passwordBinding!!.passwordEt.text.toString().trim({ it <= ' ' })
                if (userPass.isEmpty()) {
                    Toast.makeText(
                        this@PasswordActivity,
                        "Please enter password",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val gotoIntent: Intent =
                        Intent(this@PasswordActivity, QaCalendarActivity::class.java)
                    startActivity(gotoIntent)
                }
            }
        })
    }

    private fun visibleKeyboard() {
        passwordBinding!!.apply {
            keyboard.call(object : OnClick {
                override fun click() {
                    keyboard.visibility = View.GONE
                }
            })

            passwordEt.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    if (!keyboard.isVisible)
                        keyboard.visibility = View.VISIBLE
                    return true
                }
            })
        }
    }

    private fun forgotPass() {
        passwordBinding!!.forgetPassword.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                startActivity(Intent(this@PasswordActivity, ForgetPasswordActivity::class.java))
            }
        })
    }
}