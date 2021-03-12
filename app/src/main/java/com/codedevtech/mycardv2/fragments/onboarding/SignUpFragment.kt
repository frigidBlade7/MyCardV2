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
import com.codedevtech.mycardv2.databinding.FragmentSignUpBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    lateinit var binding: FragmentSignUpBinding
    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater,container, false)
        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.countryCodeHolder.registerCarrierNumberEditText(binding.phoneNumber)

        binding.skip.setOnClickListener {
            viewmodel.phoneNumberFormatted.value = binding.countryCodeHolder.fullNumberWithPlus
            viewmodel.goToConfirmNumber(binding.countryCodeHolder.fullNumberWithPlus)
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