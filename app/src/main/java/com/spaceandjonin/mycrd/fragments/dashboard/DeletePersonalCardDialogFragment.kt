
package com.spaceandjonin.mycrd.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentDeleteCardDialogBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.CardViewModel
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel


class DeletePersonalCardDialogFragment : DialogFragment() {

    lateinit var binding: FragmentDeleteCardDialogBinding

    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

    val cardViewModel: CardViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

    override fun getTheme(): Int {
        return R.style.Theme_MyCardStyles_Dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeleteCardDialogBinding.inflate(layoutInflater,container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewmodel

        //todo complete refactor to move selected card and selected personal card implementations
        // to card view model and stop this copy life na wanyin


        cardViewModel.selectedPersonalCard.value = viewmodel.selectedPersonalCard.value



        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        cardViewModel.destination.observe(viewLifecycleOwner, EventObserver {
            //findNavController().navigateUp()
            dismiss()
        })

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.delete.setOnClickListener {
            dismiss()
            cardViewModel.deletePersonalCard()
        }

        return binding.root
    }

}