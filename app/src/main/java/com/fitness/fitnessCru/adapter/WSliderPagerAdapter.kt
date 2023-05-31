package com.fitness.fitnessCru.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.response.WorkoutProgramResponse

class WSliderPagerAdapter(val context: Context) : PagerAdapter() {

    var list = MutableLiveData<ArrayList<WorkoutProgramResponse.Data.ProgramsSlider>>()

    @JvmName("setList1")
    fun setList(list: ArrayList<WorkoutProgramResponse.Data.ProgramsSlider>) {

        this.list.value = list

        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return try {
            list.value!!.size
        } catch (e: Exception) {
            0
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view =
            LayoutInflater.from(container.context)
                .inflate(R.layout.w_slider_image_layout, container, false)
        val imageView = view.findViewById<ImageView>(R.id.image_view)
        list.value?.get(position)?.let { Glide.with(context).load(it.image_url).into(imageView) }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
