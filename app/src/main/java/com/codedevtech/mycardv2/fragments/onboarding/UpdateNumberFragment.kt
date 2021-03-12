package com.codedevtech.mycardv2.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.FragmentUpdateNumberBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateNumberFragment : Fragment() {

    lateinit var binding: FragmentUpdateNumberBinding
    val viewmodel: SettingsViewModel by hiltNavGraphViewModels(R.id.settings_nav)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateNumberBinding.inflate(layoutInflater,container, false)
        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.countryCodeHolder.registerCarrierNumberEditText(binding.phoneNumber)

        binding.skip.setOnClickListener {
            //viewmodel.user.value?.phoneNumber = binding.countryCodeHolder.fullNumberWithPlus
            //viewmodel.user.notifyObserver()
            //go to verify
            findNavController().navigate(UpdateNumberFragmentDirections.actionUpdateNumberFragmentToVerifyNewNumberFragment(binding.countryCodeHolder.fullNumberWithPlus))
        }

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })


        binding.toolbar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }

        viewmodel.snackbarInt.observe(viewLifecycleOwner,EventObserver{
            Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }


}