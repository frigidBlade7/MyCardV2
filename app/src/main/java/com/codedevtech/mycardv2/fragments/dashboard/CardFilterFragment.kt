
package com.codedevtech.mycardv2.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.FragmentCardFilterBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.utils.Utils
import com.codedevtech.mycardv2.viewmodel.CardViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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