package com.fitness.fitnessCru.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.FragmentRecipeDetailBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.MealTypeResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.RecipeDetailViewModel
import com.fitness.fitnessCru.vm_factory.RecipeDetailVMFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.chip.Chip

class RecipeDetailFragment : Fragment() {

    private var recipeDetailBinding: FragmentRecipeDetailBinding? = null
    private lateinit var viewModel: RecipeDetailViewModel
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var loading: CustomProgressLoading

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recipeDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false)
        loading = CustomProgressLoading(requireContext())
        recipeDetailBinding?.goBackBtn?.setOnClickListener { activity?.onBackPressed() }
        simpleExoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        recipeHowToMake()
        return recipeDetailBinding?.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun recipeHowToMake() {
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { RecipeDetailVMFactory(repository) }
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeDetailViewModel::class.java)
        viewModel.getRecipeDetails(arguments?.getInt("id", 0) ?: 0)
        loading.showProgress()
        viewModel.response.observe(viewLifecycleOwner) {
            loading.hideProgress()
            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0 && it.body()!!.data != null) {
                try {
                    recipeDetailBinding?.apply {
                        var data = it.body()!!.data
                        tvFoodName.text = data.food.name
                        tvDuration.text = "${data.preparation_time}mins"
                        tvDescription.text = data.description
                        servingText.text = "${data.serving} Serving"

                        Glide.with(requireContext()).load(data.food.image_url)
                            .placeholder(R.drawable.place_holder)
                            .into(iv)
                        /*tvHowToMake.text =data.how_to_make*/
                        tvHowToMake.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(data.how_to_make, Html.FROM_HTML_MODE_LEGACY)
                        } else {
                            Html.fromHtml(data.how_to_make)
                        }
//                         createTagChip(
//                             requireContext(),
//                             it.toString()
//                         )

                        data.tag?.forEach { it ->
                            tagChip.addView(
                                createTagChip(
                                    requireContext(),
                                    it.toString()
                                )
                            )
                        }
                        /*   exoPlayerWork(data.video_url)*/
                    }
                } catch (e: Exception) {
                }

            } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                Util.toast(
                    requireContext(), it.body()!!.message
                )
                activity?.onBackPressed()
            } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                requireContext(),
                Util.error(it.errorBody(), MealTypeResponse::class.java).message
            )
        }
    }

    private fun createTagChip(context: Context, chipName: String): Chip {
        return Chip(context).apply {
            text = "#$chipName"
            setChipBackgroundColorResource(R.color.overlay_dark_50)
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setTextAppearance(R.style.ChipTextAppearance)
        }
    }

    /* private fun exoPlayerWork(video: String) {
         val playerView: PlayerView = recipeDetailBinding?.exoVideo!!
         playerView.player = simpleExoPlayer
         val mediaItem = MediaItem.fromUri(video)
         simpleExoPlayer.addMediaItems(listOf(mediaItem))

         simpleExoPlayer.prepare()
         simpleExoPlayer.play()
         simpleExoPlayer.playWhenReady = false

         view?.findViewById<View>(R.id.exo_play)
             ?.setOnClickListener(View.OnClickListener { simpleExoPlayer.play() })
         view?.findViewById<View>(R.id.exo_pause)
             ?.setOnClickListener(View.OnClickListener { simpleExoPlayer.pause() })
         simpleExoPlayer.addListener(object : Player.Listener {
             override fun onPlaybackStateChanged(state: Int) {
                 if (state == ExoPlayer.STATE_ENDED) {
                 }
             }
         })
     }*/

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.pause()
        recipeDetailBinding = null
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer!!.pause()
    }
}