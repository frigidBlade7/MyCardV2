package com.codedevtech.mycardv2.fragments.onboarding

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.*
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.utils.Utils
import com.codedevtech.mycardv2.viewmodel.AddCardViewModel
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class CompleteProfileFragment : Fragment() {

    lateinit var binding: FragmentCompleteProfileBinding
    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompleteProfileBinding.inflate(layoutInflater,container, false)
        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })


        binding.updateIcon.setOnClickListener {
            callGallery()
        }

        binding.toolbar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }

        viewmodel.snackbarInt.observe(viewLifecycleOwner,EventObserver{
            Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getPickImageResultUri(requireContext(), data)

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(requireContext(), imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE)
            } else {
                // no permissions required or already granted, can start crop image activity
                CropImage.activity(imageUri)
                    .start(requireContext(), this)
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                viewmodel.profileImageUri.value = (result?.uri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context, result?.error?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

    }
    @AfterPermissionGranted(Utils.REQUEST_PHOTO)
    private fun callGallery() {
        if (EasyPermissions.hasPermissions(requireContext(), Utils.STORAGE_PERMISSION)) {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(requireContext(), this)
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.require_gallery),
                Utils.REQUEST_PHOTO, Utils.STORAGE_PERMISSION)
        }

    }

}