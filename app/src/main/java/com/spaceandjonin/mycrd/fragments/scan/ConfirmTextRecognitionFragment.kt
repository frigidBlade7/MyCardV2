
package com.spaceandjonin.mycrd.fragments.scan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImage
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentConfirmTextRecognitionBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.utils.TextGraphic
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ConfirmTextRec"

@AndroidEntryPoint
class ConfirmTextRecognitionFragment : Fragment() {

    lateinit var binding: FragmentConfirmTextRecognitionBinding

    val viewModel : OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConfirmTextRecognitionBinding.inflate(layoutInflater, container, false)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.elementListLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                binding.graphicOverlay.clear()
                for (element in it) {
                    binding.graphicOverlay.add(TextGraphic(binding.graphicOverlay, element))

                }
            }
        })
        val uri = Uri.parse(ConfirmTextRecognitionFragmentArgs.fromBundle(requireArguments()).uriString)

        setupImageViewAndProcessUri(uri)

/*
        Glide.with(this).asBitmap().load(uri).error(
            R.drawable
                .user_default
        ).transform(
            CenterCrop(),
            RoundedCorners(8)
        ).thumbnail(0.1f).into(binding.preview)*/


        viewModel.destination.observe(viewLifecycleOwner, EventObserver {
            if (it.actionId == 0)
                findNavController().popBackStack()
            else
                findNavController().navigate(it)
        })

        binding.confirmButton.setOnClickListener {
            viewModel.elementListLiveData.value?.let {
                var lines = mutableListOf<String>()
                for (element in it)
                    lines.add(element.text)
                findNavController().navigate(ConfirmTextRecognitionFragmentDirections.actionConfirmTextRecognitionFragmentToReviewScannedDetailsFragment(lines.toTypedArray()))
            }
        }


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.crop.setOnClickListener {
            CropImage.activity(uri)
                .start(requireContext(), this,CustomCropImageActivity::class.java)
        }


        return binding.root
    }

    private fun setupImageViewAndProcessUri(uri: Uri?) {
        binding.imageView.setImageURI(uri)
        viewModel.processPhysicalCard(binding.imageView.drawable.toBitmap())
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                Log.d(TAG, "onActivityResult: ${result?.getBitmap(requireContext())}")

                val uri = result?.uri
                setupImageViewAndProcessUri(uri)

                //findNavController().popBackStack()
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context, result?.error?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}