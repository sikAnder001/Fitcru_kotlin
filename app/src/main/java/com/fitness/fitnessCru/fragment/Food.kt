package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.adapter.CuisineAdapter
import com.fitness.fitnessCru.adapter.FoodPageAdapter
import com.fitness.fitnessCru.adapter.TodayMenuAdapter
import com.fitness.fitnessCru.databinding.FragmentFoodBinding
import com.fitness.fitnessCru.model.CuisinePojo
import com.fitness.fitnessCru.model.TodayMenuPojo
import com.fitness.fitnessCru.utility.Constants
import com.google.android.material.tabs.TabLayoutMediator

class Food : Fragment() {

    private lateinit var foodBindng: FragmentFoodBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        foodBindng = FragmentFoodBinding.inflate(inflater, container, false)

        getFood()
        foodBindng.apply {
            viewPager.adapter = FoodPageAdapter(this@Food)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = arrayOf("Indian", "Continental", "Drinks", "Dessert")[position]
            }.attach()
        }
        foodBindng.cartBtn.setOnClickListener {
            val intent = Intent(activity, SetupAllActivity::class.java)
            intent.putExtra(Constants.DESTINATION, Constants.CHECKOUT)
            startActivity(intent)
        }

        return foodBindng.root
    }

    private fun getFood() {
        var todayMenuAdapter = TodayMenuAdapter(context)

        foodBindng.todayMenuRv.adapter = todayMenuAdapter

        todayMenuAdapter.setList(data3()!!)

        var cuisineAdapter = CuisineAdapter(context)

        foodBindng.cuisineRv.adapter = cuisineAdapter

        cuisineAdapter.setList(cuisine()!!)
    }

    private fun cuisine(): ArrayList<CuisinePojo>? {
        val list: ArrayList<CuisinePojo> = ArrayList()
        list.add(CuisinePojo(R.drawable.food_img, "Chinese", "47 Dishes", 1))
        list.add(CuisinePojo(R.drawable.food_img, "Spanish", "59 Dishes", 2))
        list.add(CuisinePojo(R.drawable.food_img, "Indian", "64 Dishes", 3))
        return list
    }

    private fun data3(): ArrayList<TodayMenuPojo>? {
        val list: ArrayList<TodayMenuPojo> = ArrayList()
        list.add(TodayMenuPojo(R.drawable.food_img, "500 Cal", "69 Dishes", 1))
        list.add(TodayMenuPojo(R.drawable.food_img, "400 Cal", "59 Dishes", 2))
        list.add(TodayMenuPojo(R.drawable.food_img, "600 Cal", "49 Dishes", 3))
        return list
    }

}