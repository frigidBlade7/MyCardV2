
package com.spaceandjonin.mycrd.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentConfirmNumberResetBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConfirmNumberResetFragment : Fragment() {

    lateinit var binding: FragmentConfirmNumberResetBinding

    val viewmodel: SettingsViewModel by navGraphViewModels(R.id.settings_nav){
        defaultViewModelProviderFactory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConfirmNumberResetBinding.inflate(layoutInflater,container, false)

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