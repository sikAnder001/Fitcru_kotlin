package com.fitness.fitnessCru.fragment

import android.R.layout.simple_list_item_activated_1
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.AddressAdapter
import com.fitness.fitnessCru.body.AddressBody
import com.fitness.fitnessCru.databinding.AddNewAddressBinding
import com.fitness.fitnessCru.databinding.FragmentEAddressBinding
import com.fitness.fitnessCru.interfaces.AddressClickInterface
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.*
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.AddressViewModel
import com.fitness.fitnessCru.vm_factory.AddressVMFactory
import com.google.android.material.bottomsheet.BottomSheetDialog


class EAddressFragment : Fragment() {

    private var TAG = EAddressFragment::class.java.simpleName

    private lateinit var addressBinding: FragmentEAddressBinding

    private lateinit var addAddressBinding: AddNewAddressBinding

    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var addressAdapter: AddressAdapter

    private lateinit var loading: CustomProgressLoading

    private lateinit var viewModel: AddressViewModel

    private var addressId: Int = 0
    private var country: Int = 0
    private var countryU: Int = 0
    private var state: Int = 0
    private var stateU: Int = 0
    private var city: Int = 0
    private var cityU: Int = 0
    private var countryList = ArrayList<CountriesResponse.Data>()
    private var stateList = ArrayList<StateResponse.Data>()
    private var cityList = ArrayList<CityResponse.Data>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addressBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_e_address, container, false)

        loading = CustomProgressLoading(requireContext())

        val repository by lazy { Repository() }

        val factory = AddressVMFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(AddressViewModel::class.java)


        getAddress()

        setAddressInRV()

        openBottomSheet()

        observer()

        onEditBtnClicked()

        return addressBinding.root
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

                } else Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show()

            } catch (e: Exception) {

                Util.toast(requireContext(), "Error : ${e.message}")
            }
        }

    }

    private fun observer() {
        /* viewModel.country.observe(viewLifecycleOwner) {
             loading.hideProgress()
             addAddressBinding.countrySpinner.apply {
                 if (it.isSuccessful && it.body() != null) {
                     var body = it.body()!!
                     countryList1.clear()
                     countryList1.add("Select Country")
                     for (e in body.data) {
                         countryList1.add(e.name)
                     }
                     val adapter: ArrayAdapter<*> =
                         ArrayAdapter<Any?>(
                             requireContext(),
                             simple_list_item_activated_1,
                             countryList1 as List<Any?>
                         )
                     setAdapter(adapter)
                     if (countryU != 0) {
                         run breaking@{
                             body.data.forEachIndexed { index, element ->

                                 if (element.id == countryU) {
                                     countryU = 0
                                     country = element.id
                                     setSelection(index)
                                     return@breaking
                                 }
                             }
                         }
                     } else {
                         run breaking@{
                             body.data.forEachIndexed { index, element ->
                                 if (element.name.equals("India", true) || element.phonecode == 91) {
                                     setSelection(index)
                                     return@breaking
                                 }
                             }
                         }
                     }
                     onItemSelectedListener = object :
                         AdapterView.OnItemSelectedListener {
                         override fun onItemSelected(
                             parent: AdapterView<*>?,
                             view: View,
                             position: Int,
                             id: Long
                         ) {
                             (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                             if (position > 0) {
                                 for (element in body.data) {
                                     if (parent.selectedItem.toString() == element.name) {
                                         country = element.id
                                         viewModel.getState(country)
                                     }
                                 }
                             } else {
                                 (parent?.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                             }
                             //country = (parent!!.selectedItem as CountriesResponse.Data).id

                         }

                         override fun onNothingSelected(parent: AdapterView<*>?) {}
                     }

                 } else Util.toast(requireContext(), "Something went wrong")
             }
         }*/

        /* viewModel.state.observe(viewLifecycleOwner) {
             addAddressBinding.stateSpinner.apply {
                 if (it.isSuccessful && it.body() != null) {
                     var body = it.body()!!
                     stateList1.clear()
                     stateList1.add("Select State")
                     for (e in body.data) {
                         stateList1.add(e.name)
                     }
                     val adapter: ArrayAdapter<*> =
                         ArrayAdapter<Any?>(
                             requireContext(),
                             simple_list_item_activated_1,
                             stateList1 as List<Any>
                         )
                     setAdapter(adapter)
                     if (stateU != 0) {
                         run breaking@{
                             body.data.forEachIndexed { index, element ->
                                 if (element.id == stateU) {
                                     stateU = 0
                                     state = element.id
                                     setSelection(index)
                                     return@breaking
                                 }
                             }
                         }
                     }
                     onItemSelectedListener = object :
                         AdapterView.OnItemSelectedListener {
                         override fun onItemSelected(
                             parent: AdapterView<*>?,
                             view: View,
                             position: Int,
                             id: Long
                         ) {
                             (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                             if (position > 0) {
                                 for (element in body.data) {
                                     if (parent.selectedItem.toString() == element.name) {
                                         state = element.id
                                         viewModel.getCity(state)
                                     }
                                 }
                             } else {
                                 (parent?.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                             }
                             // state = (parent!!.selectedItem as StateResponse.Data).id

                         }

                         override fun onNothingSelected(parent: AdapterView<*>?) {}
                     }

                 } else Util.toast(requireContext(), "Something went wrong")
             }
         }*/

        /*viewModel.city.observe(viewLifecycleOwner) {
            addAddressBinding.citySpinner.apply {
                if (it.isSuccessful && it.body() != null) {
                    var body = it.body()!!
                    cityList1.clear()
                    cityList1.add("Select City")
                    for (e in body.data) {
                        cityList1.add(e.name)
                    }
                    val adapter: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireContext(),
                            simple_list_item_activated_1,
                            cityList1 as List<Any>
                        )
                    setAdapter(adapter)
                    if (cityU != 0) {
                        run breaking@{
                            body.data.forEachIndexed { index, element ->
                                if (element.id == cityU) {
                                    cityU = 0
                                    city = element.id
                                    setSelection(index)
                                    return@breaking
                                }
                            }
                        }
                    }
                    onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                            if (position > 0) {
                                for (element in body.data) {
                                    if (parent.selectedItem.toString() == element.name) {
                                        city = element.id
                                    }
                                }
                            } else {
                                (parent?.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                            }
                            //city = (parent!!.selectedItem as CityResponse.Data).id
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }

                } else Util.toast(requireContext(), "Something went wrong")
            }
        }*/

        viewModel.country.observe(viewLifecycleOwner) {
            loading.hideProgress()
            addAddressBinding.countrySpinner.apply {
                if (it.isSuccessful && it.body() != null) {
                    var body = it.body()!!
                    countryList.clear()
                    countryList.addAll(body.data)
                    val adapter: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireContext(),
                            simple_list_item_activated_1,
                            body.data
                        )
                    setAdapter(adapter)
                    if (countryU != 0) {
                        run breaking@{
                            body.data.forEachIndexed { index, element ->

                                if (element.id == countryU) {
                                    countryU = 0
                                    country = element.id
                                    setSelection(index)
                                    return@breaking
                                }
                            }
                        }
                    } else {
                        run breaking@{
                            body.data.forEachIndexed { index, element ->
                                if (element.name.equals("India", true) || element.phonecode == 91) {
                                    setSelection(index)
                                    return@breaking
                                }
                            }
                        }
                    }
                    onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                            country = (parent!!.selectedItem as CountriesResponse.Data).id
                            viewModel.getState(country)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }

                } else Util.toast(requireContext(), "Something went wrong")
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            addAddressBinding.stateSpinner.apply {
                if (it.isSuccessful && it.body() != null) {
                    var body = it.body()!!
                    stateList.clear()
                    stateList.addAll(body.data)
                    val adapter: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireContext(),
                            simple_list_item_activated_1,
                            body.data
                        )
                    setAdapter(adapter)
                    if (stateU != 0) {
                        run breaking@{
                            body.data.forEachIndexed { index, element ->
                                if (element.id == stateU) {
                                    stateU = 0
                                    state = element.id
                                    setSelection(index)
                                    return@breaking
                                }
                            }
                        }
                    }
                    onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                            state = (parent!!.selectedItem as StateResponse.Data).id
                            viewModel.getCity(state)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }

                } else Util.toast(requireContext(), "Something went wrong")
            }
        }

        viewModel.city.observe(viewLifecycleOwner) {
            addAddressBinding.citySpinner.apply {
                if (it.isSuccessful && it.body() != null) {
                    var body = it.body()!!
                    cityList.clear()
                    cityList.addAll(body.data)
                    val adapter: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireContext(),
                            simple_list_item_activated_1,
                            body.data
                        )
                    setAdapter(adapter)
                    if (cityU != 0) {
                        run breaking@{
                            body.data.forEachIndexed { index, element ->
                                if (element.id == cityU) {
                                    cityU = 0
                                    city = element.id
                                    setSelection(index)
                                    return@breaking
                                }
                            }
                        }
                    }
                    onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            (parent?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                            city = (parent!!.selectedItem as CityResponse.Data).id
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }

                } else Util.toast(requireContext(), "Something went wrong")
            }
        }

    }

    private fun setAddressInRV() {

        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        addressBinding.addressRv.layoutManager = linearLayout

        addressBinding.addressRv.setHasFixedSize(true)

        addressAdapter = AddressAdapter(context, viewModel)

        addressBinding.addressRv.adapter = addressAdapter

    }

    private fun openBottomSheet() {
        addAddressBinding = AddNewAddressBinding.inflate(layoutInflater)

        bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetDialog.setContentView(addAddressBinding.root)

        addressBinding.addNewAddressBtn.setOnClickListener {

            addAddressBinding.apply {
                addAddressBtn.text = "Add Address"
                addAddressTv.text = "Add Address"
            }

            openBottomSheet(
                GetAddressResponse.Data(
                    0,
                    "0",
                    0,
                    0,
                    0,
                    GetAddressResponse.Data.CountryName(""),
                    GetAddressResponse.Data.CountryName(""),
                    GetAddressResponse.Data.CountryName(""),
                    "",
                    "",
                    "",
                    "",
                    "0",
                    GetAddressResponse.Data.UserDetails(null),
                    "",
                    ""
                )
            )
        }

        addAddressBinding.crossBTN.setOnClickListener { bottomSheetDialog.hide() }

        addAddressBinding.addAddressBtn.setOnClickListener { if (isValid()) address() }

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
            else if (country < 1)
                Pair(false, "Please Select Country")
            else if (state < 1)
                Pair(false, "Please Select State")
            else if (city < 1)
                Pair(false, "Please Select City")
            else Pair(true, "")
            if (!pair.first) Util.toast(requireContext(), pair.second)
        }
        return pair.first
    }

    private fun address() {
        addAddressBinding.apply {
            viewModel.addAddress(
                AddressBody(
                    0,
                    country,
                    state,
                    city,
                    addressEt.text.toString(),
                    locationEt.text.toString(),
                    landmarkEt.text.toString(),
                    pinCodeEt.text.toString(),
                    ""/*mobileEt.text.toString()*/
                )
            )

            loading.showProgress()
        }

        viewModel.addAddress.observe(viewLifecycleOwner) {
            val body = it.body()!!
            if (body != null) {
                if (it.isSuccessful) {
                    loading.hideProgress()
//                    defaultAddress(body.data.id, 0)
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

    private fun onEditBtnClicked() {

        addressAdapter.setOnClickInterface(object : AddressClickInterface {
            override fun onEditClicked(
                view: View,
                data: GetAddressResponse.Data,
                position: Int
            ) {
                addAddressBinding.apply {
                    addAddressBtn.text = "Update Address"
                    addAddressTv.text = "Update Address"
                }
                openBottomSheet(data)
            }
        })
    }

    fun openBottomSheet(data: GetAddressResponse.Data) {
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

            // defaultSwitch.isChecked = data.is_default == "1"
            /*when (data.is_default) {
                "1" -> defaultSwitch.isChecked = true
                "0" -> defaultSwitch.isChecked = false
                else -> defaultSwitch.isChecked = false
            }*/
        }
        viewModel.getCountry()
        bottomSheetDialog.show()
    }

}
