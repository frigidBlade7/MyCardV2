
package com.codedevtech.mycardv2.fragments.dashboard

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.DropDownAdapter
import com.codedevtech.mycardv2.adapter.rv.EmailAdapter
import com.codedevtech.mycardv2.adapter.rv.PhoneNumberAdapter
import com.codedevtech.mycardv2.adapter.rv.SocialAdapter
import com.codedevtech.mycardv2.databinding.*
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.listeners.EmailItemInteraction
import com.codedevtech.mycardv2.listeners.ItemInteraction
import com.codedevtech.mycardv2.listeners.SocialItemInteraction
import com.codedevtech.mycardv2.models.EmailAddress
import com.codedevtech.mycardv2.models.PhoneNumber
import com.codedevtech.mycardv2.models.SocialMediaProfile
import com.codedevtech.mycardv2.viewmodel.AddPersonalCardViewModel
import com.codedevtech.mycardv2.viewmodel.CardViewModel
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DeleteCardDialogFragment : DialogFragment() {

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
        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        //todo complete refactor to move selected card and selected personal card implementations
        // to card view model and stop this copy life na wanyin


        cardViewModel.selectedCard.value = viewmodel.selectedCard.value

        cardViewModel.destination.observe(viewLifecycleOwner, EventObserver {
/*            if(it.actionId==R.id.cardsFragment)
                findNavController().navigateUp()
            else*/

            findNavController().navigateUp()

            dismiss()

        })



        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.delete.setOnClickListener {
            cardViewModel.deleteCard()

        }

        return binding.root
    }

}