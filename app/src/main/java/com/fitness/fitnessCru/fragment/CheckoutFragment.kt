package com.fitness.fitnessCru.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.AddressListAdapter
import com.fitness.fitnessCru.adapter.CartItemAdapter
import com.fitness.fitnessCru.databinding.AddNewAddressBinding
import com.fitness.fitnessCru.databinding.AddressBottomSheetBinding
import com.fitness.fitnessCru.databinding.FragmentCheckoutBinding
import com.fitness.fitnessCru.databinding.OrderPlacedAlertDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class CheckoutFragment : Fragment() {
    private lateinit var fragmentCheckoutBinding: FragmentCheckoutBinding
    private val binding get() = fragmentCheckoutBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentCheckoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_checkout, container, false)
        binding.rvCartItem.adapter = CartItemAdapter()

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetDialog2 = BottomSheetDialog(requireContext())

        val addressBinding = AddressBottomSheetBinding.inflate(requireActivity().layoutInflater)
        val addAddressBinding = AddNewAddressBinding.inflate(requireActivity().layoutInflater)

        bottomSheetDialog.setContentView(addressBinding.root)
        bottomSheetDialog2.setContentView(addAddressBinding.root)

        addressBinding.rvCartItem.adapter = AddressListAdapter(requireActivity())

        addressBinding.crossBTN.setOnClickListener { if (bottomSheetDialog.isShowing) bottomSheetDialog.hide() }
        addAddressBinding.crossBTN.setOnClickListener { if (bottomSheetDialog2.isShowing) bottomSheetDialog2.hide() }

        addressBinding.tvAddAddress.setOnClickListener {
            bottomSheetDialog.hide()
            bottomSheetDialog2.show()
        }
        addAddressBinding.addAddressBtn.setOnClickListener { bottomSheetDialog2.hide() }
        binding.tvChangeAddress.setOnClickListener { bottomSheetDialog.show() }

        binding.btnPlaceOrder.setOnClickListener {
            val view = OrderPlacedAlertDialogBinding.inflate(requireActivity().layoutInflater)
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(view.root)
            val dialog = builder.create()
            view.cutDialogBoxBtn.setOnClickListener {
                if (dialog.isShowing) dialog.hide()
            }
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        return fragmentCheckoutBinding.root
    }
}