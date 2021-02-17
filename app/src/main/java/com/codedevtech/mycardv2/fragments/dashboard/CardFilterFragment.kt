
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
import com.codedevtech.mycardv2.utils.MyCardToggleGroup
import com.codedevtech.mycardv2.utils.Utils
import com.codedevtech.mycardv2.viewmodel.AddPersonalCardViewModel
import com.codedevtech.mycardv2.viewmodel.CardViewModel
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardFilterFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentCardFilterBinding

    val viewmodel: CardViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCardFilterBinding.inflate(layoutInflater,container, false)

        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        when(viewmodel.sortMode.value){
            Utils.SORT_MODE_NAME-> binding.name.isChecked =true
            Utils.SORT_MODE_RECENT -> binding.recent.isChecked = true
        }

        binding.list.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                binding.name.id -> viewmodel.sortMode.value = Utils.SORT_MODE_NAME
                binding.recent.id -> viewmodel.sortMode.value = Utils.SORT_MODE_RECENT
            }
            dismiss()
        }

        //todo recycler selection binding.list.adapter = ArrayAdapter<String>(requireContext(),R.layout.filter_item,resources.getStringArray(R.array.filters))

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

/*        binding.name.setOnClickListener {
            dismiss()
        }

        binding.recent.setOnClickListener {
            dismiss()
        }*/


        return binding.root
    }

}