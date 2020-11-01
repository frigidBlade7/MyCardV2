package com.codedevtech.mycardv2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.FragmentCaptureCardBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel

class CaptureCardFragment : Fragment() {

    lateinit var binding: FragmentCaptureCardBinding
    val viewModel : OnboardingViewModel by navGraphViewModels(R.id.onboarding_nav){
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCaptureCardBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel

        viewModel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })
        return binding.root
    }

    companion object {

    }
}