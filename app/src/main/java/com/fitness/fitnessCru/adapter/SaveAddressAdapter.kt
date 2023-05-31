package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.AddressRvItemBinding
import com.fitness.fitnessCru.response.GetAddressResponse
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.viewmodel.SaveAddressViewModel

class SaveAddressAdapter(
    private val context: Context?,
    private val viewModel: SaveAddressViewModel,
    val listener: SetOnClickInterface
) :
    RecyclerView.Adapter<SaveAddressAdapter.ViewHolder>() {

    private var list: ArrayList<GetAddressResponse.Data> = ArrayList()

    private var itemPos = -1

    inner class ViewHolder(addressBinding: AddressRvItemBinding) :
        RecyclerView.ViewHolder(addressBinding.root) {

        var addressBinding = addressBinding

        fun bind(
            data: GetAddressResponse.Data,
            position: Int
        ) {

            addressBinding.apply {

                if (Session.getUserDetails().name != null)
                    userName.text = Session.getUserDetails().name
                else userName.text = ""

                addressTv.text =
                    "${data.address},${data.landmark}, ${data.locality}, ${data.pincode}, ${data.city_name.name}, ${data.state_name.name}, ${data.country_name.name}, ${data.phone_number ?: ""}"

                selectedAddressIv.setBackgroundResource(

                    if (itemPos == position)
                        R.drawable.selected_address else R.drawable.non_selected_address

                )

                deleteBtn.setOnClickListener {
                    AlertDialog.Builder(context!!, R.style.AlertDialogStyle)
                        .setTitle("Address")
                        .setMessage("Are you sure you want to delete address?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            deleteAddress(position, data.id!!)
                            dialog.dismiss()

                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()

                        }
                        .create()
                        .show()


                    /* deleteAddress(position)*/
                }

                editBtn.setOnClickListener {
                    listener.onEditClicked(
                        editBtn,
                        data,
                        adapterPosition
                    )
                }

            }

            itemView.setOnClickListener {

                itemPos = position

                notifyDataSetChanged()

            }

        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SaveAddressAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val addressBinding = AddressRvItemBinding.inflate(inflater, parent, false)

        return ViewHolder(addressBinding)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun setAddressList(lists: ArrayList<GetAddressResponse.Data>) {
        list = lists
        notifyDataSetChanged()
    }

    fun deleteAddress(position: Int, id: Int) {

        viewModel.deleteAddress.value = null

        viewModel.deleteAddress(id)

        list.removeAt(position)

        notifyItemRemoved(position)

        notifyItemRangeChanged(position, list.size)

        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    interface SetOnClickInterface {
        fun onEditClicked(view: View, data: GetAddressResponse.Data, position: Int)

    }


}