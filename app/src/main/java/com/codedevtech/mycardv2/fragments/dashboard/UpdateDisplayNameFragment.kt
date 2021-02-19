
package com.codedevtech.mycardv2.fragments.dashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.codedevtech.mycardv2.MainActivity
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.DropDownAdapter
import com.codedevtech.mycardv2.adapter.rv.EmailAdapter
import com.codedevtech.mycardv2.adapter.rv.PhoneNumberAdapter
import com.codedevtech.mycardv2.adapter.rv.SocialAdapter
import com.codedevtech.mycardv2.databinding.*
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.listeners.EmailItemInteraction
import com.codedevtech.mycardv2.listeners.ItemInteraction
import com.codedevtech.mycardv2.listeners.SocialItemInteraction
import com.codedevtech.mycardv2.models.EmailAddress
import com.codedevtech.mycardv2.models.PhoneNumber
import com.codedevtech.mycardv2.models.Resource
import com.codedevtech.mycardv2.models.SocialMediaProfile
import com.codedevtech.mycardv2.utils.Utils
import com.codedevtech.mycardv2.utils.aggregateNameToFullName
import com.codedevtech.mycardv2.utils.notifyObserver
import com.codedevtech.mycardv2.utils.segregateFullName
import com.codedevtech.mycardv2.viewmodel.*
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

private const val TAG = "SettingsFragment"

@AndroidEntryPoint
class UpdateDisplayNameFragment : Fragment() {

    lateinit var binding: FragmentUpdateNameBinding

    val viewmodel: SettingsViewModel by navGraphViewModels(R.id.settings_nav){
        defaultViewModelProviderFactory
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*enterTransition = MaterialSharedAxis(MaterialSharedAxis.X,true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X,false)*/
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateNameBinding.inflate(layoutInflater,container, false)

        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            if(it.actionId==0)
                findNavController().popBackStack()
            else
                findNavController().navigate(it)
        })





        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

}