package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.FoodAdapter
import com.fitness.fitnessCru.databinding.FragmentIndianBinding
import com.fitness.fitnessCru.model.IndianFoodPojo

class IndianFragment : Fragment() {

    private lateinit var binding: FragmentIndianBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentIndianBinding.inflate(inflater, container, false)

        getFood()
        return binding.root
    }

    private fun getFood() {

        var foodAdapter = FoodAdapter(activity!!, context)

        binding.foodDetailRv.adapter = foodAdapter

        foodAdapter.setList(foodList()!!)
    }

    private fun foodList(): ArrayList<IndianFoodPojo>? {
        val list: ArrayList<IndianFoodPojo> = ArrayList()
        list.add(
            IndianFoodPojo(
                R.drawable.food_img,
                "Burger Aloo Tikki",
                "Rs.250",
                "448 Cal",
                "High Protein",
                "0g",
                "120g",
                1
            )
        )
        list.add(
            IndianFoodPojo(
                R.drawable.food_img,
                "Burger Aloo Tikki",
                "Rs.250",
                "448 Cal",
                "High Protein",
                "0g",
                "120g",
                2
            )
        )
        return list
    }

}