package com.codedevtech.mycardv2.fragments.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.FragmentSetupAccountOnboardingBinding
import com.codedevtech.mycardv2.databinding.FragmentSignUpBinding
import com.codedevtech.mycardv2.databinding.FragmentSkipOnboardingBinding
import com.codedevtech.mycardv2.databinding.MeFragmentBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel


class MeFragment : Fragment() {

    lateinit var binding: MeFragmentBinding
    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.dashboard_nav)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = MeFragmentBinding.inflate(layoutInflater,container, false)
        //binding.viewModel = viewmodel

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        return binding.root
    }


}