package com.spaceandjonin.mycrd.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentUpdateNameBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SettingsFragment"

@AndroidEntryPoint
class UpdateDisplayNameFragment : Fragment() {

    lateinit var binding: FragmentUpdateNameBinding

    val viewmodel: SettingsViewModel by navGraphViewModels(R.id.settings_nav) {
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
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpdateNameBinding.inflate(layoutInflater, container, false)

        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            if (it.actionId == 0)
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