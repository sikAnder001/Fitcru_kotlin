package com.fitness.fitnessCru.adapter

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.databinding.CartAddressListBinding

class AddressListAdapter(val activity: Activity) :
    RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CartAddressListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(CartAddressListBinding.inflate(activity.layoutInflater, parent, false))
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

}
