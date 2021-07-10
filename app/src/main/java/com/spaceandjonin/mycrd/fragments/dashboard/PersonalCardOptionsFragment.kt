package com.spaceandjonin.mycrd.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentPersonalCardOptionsBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.CardViewModel
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PersonalCardOptionsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPersonalCardOptionsBinding

    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)
    val cardViewmodel: CardViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPersonalCardOptionsBinding.inflate(layoutInflater, container, false)

        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        cardViewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })


        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.delete.setOnClickListener {
            dismiss()
            viewmodel.confirmPersonalCardDeletion()
        }

        binding.edit.setOnClickListener {
            dismiss()
            findNavController().navigate(
                PersonalCardOptionsFragmentDirections.actionGlobalAddPersonalCardNav(
                    isEdit = true,
                    viewmodel.selectedPersonalCard.value
                )
            )
        }

        binding.view.setOnClickListener {
            dismiss()
            viewmodel.showCardQr()
        }
        return binding.root
    }

}