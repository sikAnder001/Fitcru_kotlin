package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.body.ChangePasswordBody
import com.fitness.fitnessCru.databinding.FragmentChangePasswordBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.ChangePasswordResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.ChangePasswordViewModel
import com.fitness.fitnessCru.vm_factory.ChangePasswordVMFactory

class ChangePasswordFragment : Fragment() {

    private lateinit var changePasswordBinding: FragmentChangePasswordBinding
    private lateinit var loading: CustomProgressLoading

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changePasswordBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
        changePassword()
        loading = CustomProgressLoading(requireContext())

        return changePasswordBinding.root
    }

    private fun changePassword() {
        val changePasswordRepository = Repository()
        val changePasswordViewModelFactory =
            ChangePasswordVMFactory(changePasswordRepository)
        val chnagePasswordViewModel = ViewModelProvider(
            this,
            changePasswordViewModelFactory
        ).get(ChangePasswordViewModel::class.java)
        changePasswordBinding.updatePassBtn.setOnClickListener(View.OnClickListener {
            val oldPass = changePasswordBinding.oldPassword.text.toString().trim()
            val newPass = changePasswordBinding.newPassword.text.toString().trim()

            if (checkPassword())
                if (oldPass == newPass) {
                    changePasswordBinding.newPassword.requestFocus()
                    changePasswordBinding.newPassword.error =
                        resources.getString(R.string.new_password_must_be_not_same)
                } else {

                    changePasswordBinding.apply {
                        chnagePasswordViewModel.changePassword(
                            ChangePasswordBody(
                                oldPassword.text.toString(),
                                newPassword.text.toString(),
                                confNewPassword.text.toString(),
                                ""

                            )
                        )
                        loading.showProgress()
                    }
                }
        })
        chnagePasswordViewModel.response.observe(viewLifecycleOwner) {
            loading.hideProgress()
            try {
                if (it.isSuccessful && it.body()?.error_code ?: 1 == 0) {
                    val token = Session.getToken()
                    Util.toast(requireContext(), it.body()?.message.toString())
                } else {
                    if (it.code() == 400 || it.code() == 401) {
                        val error =
                            Util.error(it.errorBody(), ChangePasswordResponse::class.java)
                        Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun checkPassword(): Boolean {
        val oldPass = changePasswordBinding.oldPassword.text.toString().trim()
        if (oldPass.isEmpty()) {
            changePasswordBinding.oldPassword.requestFocus()
            changePasswordBinding.oldPassword.error =
                resources.getString(R.string.please_enter_password)
            return false
        }
        val newPass = changePasswordBinding.newPassword.text.toString().trim()
        if (newPass.isEmpty()) {
            changePasswordBinding.newPassword.requestFocus()
            changePasswordBinding.newPassword.error =
                resources.getString(R.string.please_enter_new_npassword)
            return false
        }
        if (newPass.length < 8) {
            changePasswordBinding.newPassword.requestFocus()
            changePasswordBinding.newPassword.error =
                resources.getString(R.string.password_must_be_8_digits)
            return false
        }
        val confirmPass = changePasswordBinding.confNewPassword.text.toString().trim()
        if (confirmPass.isEmpty()) {
            changePasswordBinding.confNewPassword.requestFocus()
            changePasswordBinding.confNewPassword.error =
                resources.getString(R.string.please_enter_confirm_npassword)
            return false
        } else if (newPass != confirmPass) {
            changePasswordBinding.confNewPassword.requestFocus()
            changePasswordBinding.confNewPassword.error =
                resources.getString(R.string.password_doesnot_match)
            return false
        }
        return true
    }

}