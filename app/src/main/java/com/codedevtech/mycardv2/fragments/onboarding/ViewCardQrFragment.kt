package com.codedevtech.mycardv2.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.CardAdapter
import com.codedevtech.mycardv2.databinding.FragmentCardDetailsBinding
import com.codedevtech.mycardv2.databinding.FragmentConfirmDetailsBinding
import com.codedevtech.mycardv2.databinding.FragmentQrBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.models.Card
import com.codedevtech.mycardv2.models.Name
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.google.android.material.tabs.TabLayoutMediator

class ViewCardQrFragment : Fragment() {

    lateinit var binding : FragmentQrBinding

    val onboardingViewModel: OnboardingViewModel by navGraphViewModels(R.id.onboarding_nav){
        defaultViewModelProviderFactory
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentQrBinding.inflate(layoutInflater, container, false)
        binding.viewModel = onboardingViewModel


        onboardingViewModel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

}