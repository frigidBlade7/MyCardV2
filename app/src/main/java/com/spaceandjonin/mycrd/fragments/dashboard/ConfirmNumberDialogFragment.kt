package com.spaceandjonin.mycrd.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentConfirmNumberDialogBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel


class ConfirmNumberDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentConfirmNumberDialogBinding

    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

    override fun getTheme(): Int {
        return R.style.Theme_MyCardStyles_Dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentConfirmNumberDialogBinding.inflate(layoutInflater, container, false)

        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.delete.setOnClickListener {
            dismiss()
            viewmodel.phoneNumberFormatted.value?.let {
                viewmodel.goToVerify()
            }

        }

        return binding.root
    }

}