package com.codedevtech.mycardv2.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.CardAdapter
import com.codedevtech.mycardv2.databinding.FragmentCardDetailsBinding
import com.codedevtech.mycardv2.databinding.FragmentConfirmDetailsBinding
import com.codedevtech.mycardv2.databinding.FragmentQrBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.models.LiveCard
import com.codedevtech.mycardv2.models.Name
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewCardQrFragment : Fragment() {

    lateinit var binding : FragmentQrBinding

    val onboardingViewModel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentQrBinding.inflate(layoutInflater, container, false)
        binding.viewModel = onboardingViewModel
        binding.lifecycleOwner = viewLifecycleOwner


        onboardingViewModel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

}