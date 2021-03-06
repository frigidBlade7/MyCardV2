
package com.spaceandjonin.mycrd.fragments.dashboard

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentCardOptionsBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.models.EmailAddress
import com.spaceandjonin.mycrd.models.PhoneNumber
import com.spaceandjonin.mycrd.viewmodel.CardViewModel
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.spaceandjonin.mycrd.utils.exportContactIntent
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


        viewmodel.storeTempCardByteArray()

        //setStyle(STYLE_NORMAL, R.style.ShapeAppearance_MyCardStyles_ExtraLargeComponent);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCardOptionsBinding.inflate(layoutInflater, container, false)

        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        //binding.editNote.isVisible = CardOptionsFragmentArgs.fromBundle(requireArguments()).isEdit

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

        binding.export.setOnClickListener {
            dismiss()
            //todo gotta fix this man. modularize in viewmodel
            val card = viewmodel.selectedCard.value
            startActivity(card?.exportContactIntent(viewmodel.tempCardByteArray))

        }


        binding.view.setOnClickListener {
            dismiss()
            viewmodel.showCardQr()
        }
        return binding.root
    }

    override fun onDestroy() {
        viewmodel.tempCardByteArray = null
        super.onDestroy()

    }

}