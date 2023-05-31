package com.fitness.fitnessCru.activities

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.fitness.fitnessCru.FirebaseUtils
import com.fitness.fitnessCru.databinding.ActivityViewImageBinding
import com.fitness.fitnessCru.utility.GeneralFunctions

class ViewImageActivity : AppCompatActivity() {
    private val mBinding by lazy { ActivityViewImageBinding.inflate(layoutInflater) }
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        supportActionBar?.hide()

        imageUrl = intent.getStringExtra(FirebaseUtils.IMAGE)

        if (imageUrl == null)
            return

        GeneralFunctions.loadImage(this, imageUrl!!, mBinding.iv)

        val permissionContract =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultMap ->
                resultMap.entries.forEach { entry ->
                    if (entry.value) {
                        GeneralFunctions.downloadImage(this, imageUrl!!)

                    }
                }
            }

        mBinding.iv.setOnClickListener { ActivityCompat.finishAfterTransition(this) }

        mBinding.ivDownload.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 29)
                GeneralFunctions.downloadImage(this, imageUrl!!)
            else
                permissionContract.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ActivityCompat.finishAfterTransition(this)
    }
}