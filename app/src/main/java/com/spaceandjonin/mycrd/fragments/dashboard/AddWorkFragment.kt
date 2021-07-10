package com.spaceandjonin.mycrd.fragments.dashboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.canhub.cropper.CropImage
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentAddWorkBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.utils.Utils
import com.spaceandjonin.mycrd.viewmodel.AddCardViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint

class AddWorkFragment : Fragment() {

    private lateinit var binding: FragmentAddWorkBinding
    val viewmodel: AddCardViewModel by navGraphViewModels(R.id.add_card_nav) {
        defaultViewModelProviderFactory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddWorkBinding.inflate(layoutInflater, container, false)

        binding.info.visibility = View.GONE
        binding.companyLogo.visibility = View.GONE
        binding.updateIcon.visibility = View.GONE

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewmodel.isEditFlow.observe(viewLifecycleOwner) {
            if (it) {
                binding.button.text = resources.getText(R.string.save)
                binding.button.setOnClickListener {
                    viewmodel.updateCard()
                }
            }
        }

        binding.updateIcon.setOnClickListener {
            callGallery()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // handle result of pick image chooser
        if (requestCode == Utils.REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            val fullPhotoUri: Uri? = data?.data
            CropImage.activity(fullPhotoUri)
                .start(requireContext(), this)

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                viewmodel.updateBusiness(result?.uri)
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
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            if (activity?.packageManager?.let { intent.resolveActivity(it) } != null) {
                startActivityForResult(intent, Utils.REQUEST_IMAGE_GET)
            }
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.require_gallery),
                Utils.REQUEST_PHOTO, Utils.STORAGE_PERMISSION
            )
        }

    }


}