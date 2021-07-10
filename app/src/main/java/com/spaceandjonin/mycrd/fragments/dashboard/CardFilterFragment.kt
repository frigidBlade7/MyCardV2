package com.spaceandjonin.mycrd.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentCardFilterBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.utils.Utils
import com.spaceandjonin.mycrd.viewmodel.FilterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardFilterFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCardFilterBinding

    val viewmodel: FilterViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCardFilterBinding.inflate(layoutInflater, container, false)

        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        when (viewmodel.sortMode.value) {
            Utils.SORT_MODE_NAME -> binding.name.isChecked = true
            Utils.SORT_MODE_RECENT -> binding.recent.isChecked = true
        }

        binding.list.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                binding.name.id -> viewmodel.sortMode.value = Utils.SORT_MODE_NAME
                binding.recent.id -> viewmodel.sortMode.value = Utils.SORT_MODE_RECENT
            }
            dismiss()
        }

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        return binding.root
    }

}