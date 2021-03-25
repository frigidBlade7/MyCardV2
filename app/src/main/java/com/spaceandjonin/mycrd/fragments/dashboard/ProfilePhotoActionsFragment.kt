package com.spaceandjonin.mycrd.fragments.dashboard

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageActivity
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentAddSocialsBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.AddCardViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.spaceandjonin.mycrd.databinding.FragmentPhotoActionsBinding
import com.spaceandjonin.mycrd.databinding.FragmentProfilePhotoActionsBinding
import com.spaceandjonin.mycrd.utils.Utils
import com.spaceandjonin.mycrd.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

@AndroidEntryPoint
class ProfilePhotoActionsFragment : BottomSheetDialogFragment(){

    lateinit var binding: FragmentProfilePhotoActionsBinding

    val viewmodel: SettingsViewModel by navGraphViewModels(R.id.settings_nav){
        defaultViewModelProviderFactory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfilePhotoActionsBinding.inflate(layoutInflater,container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.takePhoto.setOnClickListener {
            takePhoto()
        }

        binding.uploadPhoto.setOnClickListener {
            callGallery()
        }

        binding.removePhoto.setOnClickListener {
            viewmodel.removePhoto()
            findNavController().popBackStack()
        }



        return binding.root
    }

    @AfterPermissionGranted(Utils.REQUEST_CAMERA)
    private fun takePhoto() {
        if (EasyPermissions.hasPermissions(requireContext(), Utils.CAMERA_PERMISSION)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (activity?.packageManager?.let { intent.resolveActivity(it) } != null) {
                intent.also {
                    val file: File? = try {
                        viewmodel.imageFile
                    }catch (e: Exception){
                        null
                        //todo inform user that images cannot be saved/created/taken
                    }
                    file?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requireContext(),
                            "com.spaceandjonin.mycrd.fileprovider",
                            it)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        Log.d("TAG", "takePhoto: $photoURI")
                        startActivityForResult(intent, Utils.REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.require_camera),
                Utils.REQUEST_CAMERA, Utils.CAMERA_PERMISSION)
        }

    }

    @AfterPermissionGranted(Utils.REQUEST_PHOTO)
    private fun callGallery() {
        if (EasyPermissions.hasPermissions(requireContext(), Utils.STORAGE_PERMISSION)) {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            if (activity?.packageManager?.let { intent.resolveActivity(it) } != null) {
                startActivityForResult(intent, Utils.REQUEST_IMAGE_GET)
            }
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.require_gallery),
                Utils.REQUEST_PHOTO, Utils.STORAGE_PERMISSION)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // handle result of pick image chooser
        if (requestCode == Utils.REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {

            val fullPhotoUri: Uri? = data?.data
            CropImage.activity(fullPhotoUri)
                .setAspectRatio(1,1)
                .start(requireContext(), this)

        }

        if (requestCode == Utils.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            Log.d("TAG", "onActivityResult: ${viewmodel.imageFile?.absolutePath}")
            val fullPhotoUri: Uri? = Uri.fromFile(viewmodel.imageFile)
            CropImage.activity(fullPhotoUri)
                .setAspectRatio(1,1)
                .start(requireContext(), this)
            /*
            CropImage.activity(imageUri)
                .start(requireContext(), this)*/

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                viewmodel.profileImageUri.value = (result?.uri)
                viewmodel.updateProfile()

                findNavController().popBackStack()
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


}