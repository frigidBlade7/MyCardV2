package com.spaceandjonin.mycrd.fragments.scan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageActivity
import com.spaceandjonin.mycrd.databinding.ActivityCustomCropImageBinding
import timber.log.Timber

class CustomCropImageActivity : CropImageActivity(), CustomCropContract.View {

    lateinit var binding: ActivityCustomCropImageBinding
    private val presenter: CustomCropContract.Presenter = CustomCropPresenter()

    companion object {

        fun start(activity: Activity) {
            ActivityCompat.startActivity(
                activity,
                Intent(activity, CustomCropImageActivity::class.java),
                null
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCustomCropImageBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        presenter.bindView(this)

        Timber.d( "onCreate: $cropImageUri")


        binding.confirmButton.setOnClickListener {
            cropImage() // CropImageActivity.cropImage()
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() // CropImageActivity.onBackPressed()
        }
        binding.rotate.setOnClickListener {
            presenter.onRotateClick()
        }

        setCropImageView(binding.cropImageView)
        val bundle = intent.getBundleExtra(CropImage.CROP_IMAGE_EXTRA_BUNDLE)
        cropImageUri = bundle?.getParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE)
        binding.cropImageView.setImageUriAsync(cropImageUri)
    }

    override fun setContentView(view: View?) {
        super.setContentView(binding.root)
    }

    override fun rotate(counter: Int) {
        binding.cropImageView.rotateImage(counter)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            binding.cropImageView.setImageUriAsync(cropImageUri)
        }
    }

    // Override this to add more information into the intent
    override fun getResultIntent(uri: Uri?, error: java.lang.Exception?, sampleSize: Int): Intent {
        val result = super.getResultIntent(uri, error, sampleSize)
        return result.putExtra("EXTRA_KEY", "Extra data")
    }

    override fun setResult(uri: Uri?, error: Exception?, sampleSize: Int) {
        setResult(
            error?.let { CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE } ?: RESULT_OK,
            getResultIntent(uri, error, sampleSize)
        )
        finish()
    }

    override fun setResultCancel() {
        Timber.d("User this override to change behaviour when cancel")
        super.setResultCancel()
    }

}