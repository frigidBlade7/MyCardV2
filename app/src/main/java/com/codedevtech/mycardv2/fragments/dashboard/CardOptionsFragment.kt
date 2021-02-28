
package com.codedevtech.mycardv2.fragments.dashboard

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CardOptionsFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentCardOptionsBinding

    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)
    val cardViewmodel: CardViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

/*    override fun getTheme(): Int {
        return R.style.Theme_MyCardStyles_Options
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //setStyle(STYLE_NORMAL, R.style.ShapeAppearance_MyCardStyles_ExtraLargeComponent);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCardOptionsBinding.inflate(layoutInflater,container, false)

        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.editNote.isVisible = CardOptionsFragmentArgs.fromBundle(requireArguments()).isEdit

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        cardViewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.delete.setOnClickListener {
            dismiss()
            viewmodel.confirmCardDeletion()
        }

        binding.edit.setOnClickListener {
            dismiss()
            viewmodel.editCard()
        }
        binding.view.setOnClickListener {
            dismiss()
            viewmodel.showCardQr()
        }
        return binding.root
    }

}