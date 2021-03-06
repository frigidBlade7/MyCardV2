
package com.spaceandjonin.mycrd.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentSettingsBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.models.Resource
import com.spaceandjonin.mycrd.viewmodel.SettingsViewModel
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SettingsFragment"

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding

    val viewmodel: SettingsViewModel by navGraphViewModels(R.id.settings_nav){
        defaultViewModelProviderFactory
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater,container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            if(it.actionId==0)
                findNavController().popBackStack()
            else
                findNavController().navigate(it)
        })

/*
        binding.updateIcon.setOnClickListener {
            callGallery()
        }*/


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        viewmodel.getLoggedInUser().observe(viewLifecycleOwner){
            when(it){
                is Resource.Success->{
                    viewmodel.user.value = it.data
                }
            }
        }

        return binding.root
    }


}