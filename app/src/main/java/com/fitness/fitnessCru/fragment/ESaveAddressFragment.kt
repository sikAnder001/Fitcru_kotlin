package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.SaveAddressAdapter
import com.fitness.fitnessCru.body.AddressBody
import com.fitness.fitnessCru.databinding.AddNewAddressBinding
import com.fitness.fitnessCru.databinding.FragmentESaveAddressBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.AddressResponse
import com.fitness.fitnessCru.response.GetAddressResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.SpinnerUtil
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.SaveAddressViewModel
import com.fitness.fitnessCru.vm_factory.AddressVMFactory
import com.google.android.material.bottomsheet.BottomSheetDialog


class ESaveAddressFragment : Fragment() {
    private var TAG = ESaveAddressFragment::class.java.simpleName
    private lateinit var saveAddressBinding: FragmentESaveAddressBinding

    private lateinit var addAddressBinding: AddNewAddressBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var loading: CustomProgressLoading
    private lateinit var addressAdapter: SaveAddressAdapter
    private lateinit var viewModel: SaveAddressViewModel

    var mcountryId = 0
    var mstateId = 0
    var mcityId = 0
    var addressId = 0
    var countryU = 0
    var stateU = 0
    var cityU = 0

    private var country: Int = 0
    private var state: Int = 0
    private var city: Int = 0
    var mcountryName = ""
    var mStateName = ""
    var mCityName = ""
    var countryList = ArrayList<String>()
    var stateList = ArrayList<String>()
    var cityList = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        saveAddressBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_e_save_address, container, false)

        loading = CustomProgressLoading(requireContext())


        val repository by lazy { Repository() }
        val factory = AddressVMFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(SaveAddressViewModel::class.java)
        getAddress()
        setAddressInRV()
        openBottomSheet(null)
        observer()




        return saveAddressBinding.root
    }

    private fun getAddress() {

        viewModel.getCountry()

        viewModel.getAddressByUser()

        viewModel.getAddress.observe(viewLifecycleOwner) {

            try {

                /*val token = Session.getToken()

                if (Session.getUserDetails().name != null) {
                    val test = Session.getUserDetails().name
                }*/

                /*Log.e(TAG, "$token")*/

                if (it.isSuccessful && it.body()?.error_code == 0) {

                    addressAdapter.setAddressList(it.body()!!.data as ArrayList<GetAddressResponse.Data>)

                } else {
/*
                    Toast.makeText(requireContext(), "Shivaay Something went wrong", Toast.LENGTH_LONG)
                        .show()
*/
                }

            } catch (e: Exception) {

                Util.toast(requireContext(), "Error : ${e.message}")
            }
        }

    }

    fun observer() {

        viewModel.country.observe(viewLifecycleOwner) {


            addAddressBinding.countrySpinner.apply {
                if (it.isSuccessful && it.body() != null) {
                    val body = it.body()!!.data
                    countryList.clear()
                    countryList.add("Select Country")
                    for (e in it.body()!!.data) {
                        countryList.add(e.name)
                    }
                    Log.v("Shivay", "openBottomSheet: ${countryList.toString()}")

                    SpinnerUtil.setSpinner(
                        addAddressBinding.countrySpinner,
                        countryList,
                        requireContext()
                    )

                    if (countryU > 0) {
                        run breaking@{
                            body.forEachIndexed { index, element ->
                                if (element.id == countryU) {
                                    country = 0
                                    country = element.id
                                    addAddressBinding.countrySpinner.setSelection(index + 1)
                                    return@breaking
                                }
                            }
                        }
                    }
                    addAddressBinding.countrySpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }

                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                pos: Int,
                                p3: Long
                            ) {

                                //  (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)

                                if (pos > 0) {
                                    mcountryId = it.body()!!.data[pos - 1].id
                                    mcountryName = it.body()!!.data[pos - 1].name
                                    Log.v("IndiaCountry", "openBottomSheet: ${mcountryName}")

                                    hitStateApi(mcountryId)
                                } else {
                                    mcountryId = 0
                                    mcountryName = ""
//                                    (p1 as TextView).setTextColor(Color.GRAY)
                                }

                            }
                        }
                }
            }
        }



        viewModel.country.observe(viewLifecycleOwner) {

            // ErrorUtil.handlerGeneralError(this, tv_view1, it)
            //  showToast(it?.message!!)
            // Util.toast(requireContext(), "Something went wrong")

        }

        viewModel.state.observe(viewLifecycleOwner) {


            addAddressBinding.stateSpinner.apply {
                if (it.isSuccessful && it.body() != null) {
                    val body = it.body()!!.data
                    stateList.clear()
                    stateList.add("Select State")
                    for (e in it.body()!!.data) {
                        stateList.add(e.name)
                    }
                    Log.v("Shivay1", "openBottomSheet: ${stateList.toString()}")


                    SpinnerUtil.setSpinner(
                        addAddressBinding.stateSpinner,
                        stateList,
                        requireContext()
                    )

                    if (stateU > 0) {
                        run breaking@{
                            body.forEachIndexed { index, element ->
                                if (element.id == stateU) {
                                    state = 0
                                    state = element.id
                                    addAddressBinding.stateSpinner.setSelection(index + 1)
                                    return@breaking
                                }
                            }
                        }
                    }
                    addAddressBinding.stateSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }

                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                pos: Int,
                                p3: Long
                            ) {

                                if (pos > 0) {
                                    mstateId = it.body()!!.data[pos - 1].id
                                    mStateName = it.body()!!.data[pos - 1].name
                                    hitCityApi(mstateId)

                                } else {
                                    mstateId = 0
                                    mStateName = ""
//                                    (p1 as TextView).setTextColor(Color.GRAY)
                                }
                            }
                        }

                }

            }
            viewModel.state.observe(viewLifecycleOwner) {

                // ErrorUtil.handlerGeneralError(this, tv_view1, it)
                //  showToast(it?.message!!)
                // Util.toast(requireContext(), "Something went wrong")

            }




            viewModel.city.observe(viewLifecycleOwner) {


                addAddressBinding.citySpinner.apply {
                    if (it.isSuccessful && it.body() != null) {
                        val body = it.body()!!.data
                        cityList.clear()
                        cityList.add("Select City")
                        for (e in it.body()!!.data) {
                            cityList.add(e.name)
                        }
                        Log.v("Shivay2", "openBottomSheet: ${cityList.toString()}")


                        SpinnerUtil.setSpinner(
                            addAddressBinding.citySpinner,
                            cityList,
                            requireContext()
                        )
                        if (cityU > 0) {
                            run breaking@{
                                body.forEachIndexed { index, element ->
                                    if (element.id == cityU) {
                                        city = 0
                                        city = element.id
                                        addAddressBinding.citySpinner.setSelection(index + 1)
                                        return@breaking
                                    }
                                }
                            }
                        }
                        addAddressBinding.citySpinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onNothingSelected(p0: AdapterView<*>?) {

                                }

                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    pos: Int,
                                    p3: Long
                                ) {

                                    if (pos > 0) {
                                        mcityId = it.body()!!.data[pos - 1].id
                                        mCityName = it.body()!!.data[pos - 1].name
                                    } else {
                                        mcityId = 0
                                        mCityName = ""
//                                        (p1 as TextView).setTextColor(Color.GRAY)
                                    }

                                }
                            }

                    }

                }
                viewModel.city.observe(viewLifecycleOwner) {

                    // ErrorUtil.handlerGeneralError(this, tv_view1, it)
                    //  showToast(it?.message!!)
                    // Util.toast(requireContext(), "Something went wrong")

                }

            }
        }

    }


    private fun setAddressInRV() {

        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        saveAddressBinding.addressRv.layoutManager = linearLayout

        saveAddressBinding.addressRv.setHasFixedSize(true)

        addressAdapter = SaveAddressAdapter(context, viewModel, object :
            SaveAddressAdapter.SetOnClickInterface {
            override fun onEditClicked(view: View, data: GetAddressResponse.Data, position: Int) {
                addAddressBinding.apply {
                    addAddressBtn.text = "Update Address"
                    addAddressTv.text = "Update Address"
                }
                openBottomSheet(data)
                bottomSheetDialog.show()
            }
        })

        saveAddressBinding.addressRv.adapter = addressAdapter

    }

    private fun openBottomSheet(data: GetAddressResponse.Data?) {
        Log.e(TAG, "openBottomSheet: $data")
        addAddressBinding = AddNewAddressBinding.inflate(layoutInflater)


        bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetDialog.setContentView(addAddressBinding.root)

        hitGetCountryApi()


        hitStateApi(mcountryId)
        hitCityApi(mstateId)

        saveAddressBinding.addNewAddressBtn.setOnClickListener {

            addressId = 0
            country = 0
            state = 0
            city = 0
            countryU = 0
            stateU = 0
            cityU = 0

            addAddressBinding.apply {

                addressEt.text.clear()
                locationEt.text.clear()
                landmarkEt.text.clear()
                pinCodeEt.text.clear()
                addAddressBtn.text = "Add Address"
                addAddressTv.text = "Add Address"
            }
            openBottomSheet(null)
            bottomSheetDialog.show()

        }

        if (data != null) {


            addressId = data.id!!
            countryU = data.country_id
            stateU = data.state_id
            cityU = data.city_id
            addAddressBinding.apply {
                addressEt.setText(data.address)
                locationEt.setText(data.locality)
                landmarkEt.setText(data.landmark)
                pinCodeEt.setText(data.pincode)
                addAddressBtn.text = "Update Address"
                addAddressTv.text = "Update Address"
            }
        }

        addAddressBinding.crossBTN.setOnClickListener {
            bottomSheetDialog.hide()

        }

        addAddressBinding.addAddressBtn.setOnClickListener {
            if
                    (isValid())
                address()
        }

    }


/*
    private fun openBottomSheet() {


        //   if (dialog == null) {

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = View.inflate(context, R.layout.add_new_address, null)
        dialog?.setContentView(view)
        // dialog?.tv_service_provider?.let { focusHitText(it) }
        // dialog?.tv_description?.let { focusHitTextDes(it) }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        //  }
        dialog?.show()

        dialog?.close_service_provider!!.setOnClickListener {

            dialog?.dismiss()
        }



        //focusHitText(tv_service_provider)
    }
*/

    private fun hitGetCountryApi() {
        viewModel.getCountry(
            /* "Token" + " " + sharedPreference.accessToken*/
        )

    }

    private fun hitStateApi(mcountryId: Int) {

        viewModel.getState(mcountryId)
        /*  if (NetworkUtils.isInternetAvailable(this)) {


        } else {
            showToast("Please check your internet connection")
        }*/
    }

    private fun hitCityApi(mstateId: Int) {
        viewModel.getCity(mstateId)
        /* if (NetworkUtils.isInternetAvailable(this)) {


         } else {
             showToast("Please check your internet connection")
         }*/
    }


    private fun address() {
        addAddressBinding.apply {
            viewModel.addAddress(
                AddressBody(
                    addressId,
                    mcountryId,
                    mstateId,
                    mcityId,
                    addressEt.text.toString(),
                    locationEt.text.toString(),
                    landmarkEt.text.toString(),
                    pinCodeEt.text.toString(),
                    null/*mobileEt.text.toString()*/
                )
            )

            addressEt.text.clear()
            locationEt.text.clear()
            landmarkEt.text.clear()
            pinCodeEt.text.clear()
            loading.showProgress()
        }

        viewModel.addAddress.observe(viewLifecycleOwner) {
            val body = it.body()!!
            if (body != null) {
                if (it.isSuccessful) {
                    loading.hideProgress()
                    // defaultAddress(body.data.id, 0)
                    Util.toast(requireContext(), body.message)
                    requireFragmentManager().beginTransaction().detach(this).attach(this).commit()
                    getAddress()

                    addressAdapter.notifyDataSetChanged()
                    bottomSheetDialog.hide()

                } else {
                    loading.hideProgress()
                    Util.toast(
                        requireContext(),
                        Util.error(it.errorBody(), AddressResponse::class.java).message
                    )
                    loading.hideProgress()
                }
            }
        }


        viewModel.deleteAddress.observe(viewLifecycleOwner) {
            if (it == null) {
                loading.showProgress()
            }
            try {
                val body = it.body()!!
                if (it.isSuccessful) {
                    loading.hideProgress()
                    /*  Util.toast(requireContext(), body.message)*/
                } else {
                    loading.hideProgress()
                    Util.toast(
                        requireContext(),
                        Util.error(it.errorBody(), AddressResponse::class.java).message
                    )
                }
            } catch (e: Exception) {
            }
        }
    }


    private fun isValid(): Boolean {
        val pair: Pair<Boolean, String>
        addAddressBinding.apply {
            pair = if (addressEt.text.toString().trim().isEmpty())
                Pair(false, "Please Fill Address")
            else if (locationEt.text.toString().trim().isEmpty())
                Pair(false, "Please Fill Locality")
            else if (landmarkEt.text.toString().trim().isEmpty())
                Pair(false, "Please Fill Landmark")
            else if (pinCodeEt.text.toString().length != 6)
                Pair(false, "Please Fill Pincode")
//            else if (mobileEt.text.toString().length != 10)
//                Pair(false, "Please Fill Mobile")
            else if (mcountryId < 1)
                Pair(false, "Please Select Country")
            else if (mstateId < 1)
                Pair(false, "Please Select State")
            else if (mcityId < 1)
                Pair(false, "Please Select City")
            else Pair(true, "")
            if (!pair.first) Util.toast(requireContext(), pair.second)
        }
        return pair.first
    }
}

/*
    fun openBottomUpdatedSheet(data: GetAddressResponse.Data) {
        Log.e(TAG, "openBottomSheet: $data")
        if (data.id != null) {
            addressId = data.id
        }
        countryU = data.country_id
        stateU = data.state_id
        cityU = data.city_id
        addAddressBinding.apply {
            addressEt.setText(data.address)
            locationEt.setText(data.locality)
            landmarkEt.setText(data.landmark)
            pinCodeEt.setText(data.pincode)


             defaultSwitch.isChecked = data.is_default == "1"
            *//*when (data.is_default) {
                "1" -> defaultSwitch.isChecked = true
                "0" -> defaultSwitch.isChecked = false
                else -> defaultSwitch.isChecked = false
            }*//*
        }
        viewModel.getCountry()
        bottomSheetDialog.show()
    }*/




