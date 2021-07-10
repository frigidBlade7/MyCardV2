package com.spaceandjonin.mycrd.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentQrBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewCardQrFragment : Fragment() {

    private lateinit var binding: FragmentQrBinding

    val onboardingViewModel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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