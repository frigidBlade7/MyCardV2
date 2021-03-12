
package com.codedevtech.mycardv2.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.FragmentDeleteCardDialogBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.viewmodel.CardViewModel
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel


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