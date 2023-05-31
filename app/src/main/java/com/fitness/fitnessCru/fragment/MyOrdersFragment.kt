package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.MyOrdersAdapter
import com.fitness.fitnessCru.databinding.FragmentMyOrdersBinding
import com.fitness.fitnessCru.model.MyOrdersModel

class MyOrdersFragment : Fragment() {

    private lateinit var orderBinding: FragmentMyOrdersBinding

    private lateinit var myOrdersAdapter: MyOrdersAdapter

    private lateinit var myOrdersModel: ArrayList<MyOrdersModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        orderBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_orders, container, false)

        setMyOrdersRV()

        orderBinding.backBtn.setOnClickListener { activity?.onBackPressed() }

        return orderBinding.root
    }

    private fun setMyOrdersRV() {

        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        orderBinding.foodSearchRv.layoutManager = linearLayout

        orderBinding.foodSearchRv.setHasFixedSize(true)

        myOrdersModel = getMyOrdersItem()

        myOrdersAdapter = MyOrdersAdapter(myOrdersModel, context)

        orderBinding.foodSearchRv.adapter = myOrdersAdapter

    }

    private fun getMyOrdersItem(): ArrayList<MyOrdersModel> {

        val orders: ArrayList<MyOrdersModel> = ArrayList()

        orders.apply {

            add(
                MyOrdersModel(
                    R.drawable.place_holder,
                    "Butter Chicken Burger",
                    "340",
                    "448 Cal",
                    "High Protein",
                    "08 feb 2022 at 12:16PM"
                )
            )

            add(
                MyOrdersModel(
                    R.drawable.place_holder,
                    "Butter Chicken Burger",
                    "340",
                    "448 Cal",
                    "High Protein",
                    "08 feb 2022 at 12:16PM"
                )
            )

            add(
                MyOrdersModel(
                    R.drawable.place_holder,
                    "Butter Chicken Burger",
                    "340",
                    "448 Cal",
                    "High Protein",
                    "08 feb 2022 at 12:16PM"
                )
            )

            add(
                MyOrdersModel(
                    R.drawable.place_holder,
                    "Butter Chicken Burger",
                    "340",
                    "448 Cal",
                    "High Protein",
                    "08 feb 2022 at 12:16PM"
                )
            )

        }
        return orders
    }
}