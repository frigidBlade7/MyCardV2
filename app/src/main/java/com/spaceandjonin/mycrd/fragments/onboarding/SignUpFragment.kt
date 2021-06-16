package com.spaceandjonin.mycrd.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentSignUpBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel
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
            val formattedNumber =  binding.countryCodeHolder.fullNumberWithPlus
            viewmodel.phoneNumberFormatted.value = formattedNumber
            viewmodel.goToConfirmNumber(formattedNumber)
        }

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })


        binding.toolbar.setNavigationOnClickListener{
            viewmodel.resetLiveDataParams()
            findNavController().popBackStack()
        }

        viewmodel.snackbarInt.observe(viewLifecycleOwner,EventObserver{
            Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }


}