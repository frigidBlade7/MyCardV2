package com.spaceandjonin.mycrd.fragments.scan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageActivity
import com.canhub.cropper.CropImageOptions
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.ActivityCustomCropImageBinding

class CustomCropImageActivity: CropImageActivity(), CustomCropContract.View {

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

        Log.d("TAG", "onCreate: $cropImageUri")


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
        //binding.cropImageView.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.add_card_manually))
        //binding.cropImageView.setImageUriAsync(Uri.parse("content://com.android.providers.media.documents/document/image%3A434233"))
    }

    override fun setContentView(view: View?) {
        super.setContentView(binding.root)
    }

    override fun rotate(counter: Int) {
        binding.cropImageView.rotateImage(counter)
    }
/*

    override fun updateRotationCounter(counter: String) {
        binding.rotateText.text = getString(R.string.rotation_value, counter)
    }
*/

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
        Log.i("extend", "User this override to change behaviour when cancel")
        super.setResultCancel()
    }

/*    override fun updateMenuItemIconColor(menu: Menu, itemId: Int, color: Int) {
        Log.i(
                "extend",
                "If not using your layout, this can be one option to change colours. Check README and wiki for more"
        )
        super.updateMenuItemIconColor(menu, itemId, color)
    }*/

}