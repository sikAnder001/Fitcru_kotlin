package com.fitness.fitnessCru.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.body.UpdateUserDetailsBody
import com.fitness.fitnessCru.databinding.FragmentBasicBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.UserDetailsResponse
import com.fitness.fitnessCru.utility.*
import com.fitness.fitnessCru.viewmodel.UpdateUserDetailsViewModel
import com.fitness.fitnessCru.viewmodel.UserDetailsViewModel
import com.fitness.fitnessCru.vm_factory.UpdateUserDetailsVMFactory
import com.fitness.fitnessCru.vm_factory.UserDetailsVMFactory
import com.github.dhaval2404.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.ParseException
import java.util.*

class BasicFragment : Fragment() {

    private var _binding: FragmentBasicBinding? = null
    private var TAG = BasicFragment::class.java.simpleName
    private var uri: Uri? = null
    private var filePath: String? = null

    private lateinit var loading: CustomProgressLoading

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDetails()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBasicBinding.inflate(inflater, container, false)
        loading = CustomProgressLoading(requireContext())
        updateDetails()

        _binding!!.imagepickIV.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .start()
        }

        _binding!!.tvDate.setOnClickListener { pickDob() }

        return _binding!!.root
    }

    private fun pickDob() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            requireActivity(),
            { view, year, monthOfYear, dayOfMonth -> // Display Selected date in textbox
                try {
                    val months = monthOfYear + 1
                    var msg =
                        "$year-${if (months < 10) "0$months" else months}-${if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth}"
                    if (msg != null) {
                        msg = CalendarUtils.dateFormatFit(msg)
                        _binding!!.tvDate.text = msg

                    } else {
                        _binding!!.tvDate.text = ""

                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }, year, month, day
        )

        dpd.apply {
            datePicker.maxDate =
                System.currentTimeMillis() - (12 * 31_556_952_000)
            show()
            getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
            getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        }
    }

    private fun getDetails() {

        val repository by lazy { Repository() }
        val factory by lazy { UserDetailsVMFactory(repository) }
        val viewModel by lazy {
            ViewModelProvider(
                this,
                factory
            )[UserDetailsViewModel::class.java]
        }
        loading.showProgress()
        viewModel.getUserDetails()
        viewModel.response.observe(viewLifecycleOwner) {
            loading.hideProgress()
            if (it.isSuccessful && it.code() == 200) {
                _binding?.apply {
                    val data = it.body()!!.data
                    unameET.setText(data?.name)
                    mobileET.setText(data?.phone_number)
                    emailET.setText(data?.email)
                    Session.setUserDetails(data)
                    if (data.dob == null) {
                        tvDate.text = ""
                    } else {
                        tvDate.text = CalendarUtils.dateFormatFit(data?.dob.toString())
                    }
                    Picasso.get().load(data.image_url)
                        .placeholder(
                            requireContext()!!.resources.getDrawable(
                                R.drawable.upload_img,
                                null
                            )
                        )
                        .into(setIMG)
                    genderPick.setSelection(
                        requireContext()!!.resources.getStringArray(R.array.gender).indexOf(
                            if (data.gender == "0") "Male" else if (data.gender == "1") "Female" else "Other"
                        )
                    )
                }
            } else {
                Toast.makeText(context, "Error: Something went wrong", Toast.LENGTH_LONG).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            uri = data?.data
            _binding!!.setIMG.setImageURI(uri)
            filePath = data?.getStringExtra("extra.file_path")
        } catch (e: Exception) {
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateDetails() {
        val updateUserDetailsRepository = Repository()
        val updateUserDetailsVMFactory =
            UpdateUserDetailsVMFactory(updateUserDetailsRepository)
        val updateUserDetailsViewModel = ViewModelProvider(
            this,
            updateUserDetailsVMFactory
        )[UpdateUserDetailsViewModel::class.java]
        _binding!!.saveBtn.setOnClickListener(View.OnClickListener {
            if (validationCheck()) {
                loading.showProgress()
                _binding?.apply {
                    val gender = genderPick.selectedItem.toString()
                    var body: MultipartBody.Part? = null
                    body = if (uri != null) {
                        val file = File(filePath)
                        val requestFile: RequestBody =
                            file!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("image", file.name, requestFile)
                    } else null
                    updateUserDetailsViewModel.updateUserDetails(
                        UpdateUserDetailsBody(
                            unameET.text.toString().trim()
                                .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                            mobileET.text.toString().trim()
                                .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                            emailET.text.toString().trim()
                                .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                            (if (gender.equals("male", true)) 0 else if (gender.equals(
                                    "female",
                                    true
                                )
                            ) 1 else 2).toString()
                                .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                            CalendarUtils.dateFormatForApi(tvDate.text.toString().trim())
                                .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                            body
                        )
                    )
                }
            }
        })
        updateUserDetailsViewModel.response.observe(viewLifecycleOwner) {
            loading.hideProgress()
            try {
                if (it.isSuccessful && it.body()?.error_code ?: 1 == 0) {
                    Util.toast(requireContext(), it.body()!!.message)
                    Log.e(TAG, "updateDetails: Test")
                    getDetails()
//                    Session.setUserDetails(it.body()!!.user_data)
                } else {
                    if (it.code() == 404) {
                        val error = Util.error(it.errorBody(), UserDetailsResponse::class.java)
                        Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
                    }
                    if (it.code() == 400 || it.code() == 401) {
                        val error = Util.error(it.errorBody(), UserDetailsResponse::class.java)
                        Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validationCheck(): Boolean {
        val firstName = _binding!!.unameET.text.toString().trim()
        val mobileNumber = _binding!!.mobileET.text.toString().trim()
        val email = _binding!!.emailET.text.toString().trim()
        val dob = _binding!!.tvDate.text.toString().trim()

        if (firstName.isEmpty()) {
            _binding!!.unameET.error = resources.getString(R.string.please_enter_name)
            Toast.makeText(requireContext(), "Please enter name", Toast.LENGTH_SHORT).show()
            return false
        } else if (mobileNumber.isEmpty()) {
            _binding!!.mobileET.error = resources.getString(R.string.please_enter_mobile)
            Toast.makeText(requireContext(), "Please Enter Phone Number", Toast.LENGTH_SHORT).show()
            return false
        } else if (mobileNumber.length != 10) {
            _binding!!.mobileET.error = resources.getString(R.string.please_enter_mobile_lenght)
            Toast.makeText(requireContext(), "Number length should be 10 digit", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (email.isEmpty()) {
            _binding!!.emailET.error = resources.getString(R.string.please_enter_email)
            Toast.makeText(requireContext(), "Please Enter Email", Toast.LENGTH_SHORT).show()
            return false
        } else if (!Utils.validEmail(email)) {
            _binding!!.emailET.error =
                resources.getString(R.string.please_enter_correct_email)
            Toast.makeText(requireContext(), "Please Enter correct email", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (dob.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter your DOB", Toast.LENGTH_SHORT).show()
            return false
        } else if (_binding!!.genderPick.selectedItem == "Select Gender") {
            Toast.makeText(requireContext(), "Please select your gender", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}