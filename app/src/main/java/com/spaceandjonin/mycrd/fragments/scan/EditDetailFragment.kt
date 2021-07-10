package com.spaceandjonin.mycrd.fragments.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentEditLabelledDetailBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.models.LabelDetail
import com.spaceandjonin.mycrd.viewmodel.ReviewScannedDetailsViewModel

class EditDetailFragment : BottomSheetDialogFragment() {

    var existingLabelDetail: LabelDetail? = null

    private lateinit var binding: FragmentEditLabelledDetailBinding

    val viewmodel: ReviewScannedDetailsViewModel by navGraphViewModels(R.id.scan_nav) {
        defaultViewModelProviderFactory
    }

    override fun getTheme(): Int {
        return R.style.Theme_MyCardStyles_DialogFragment_AboveKeyboard
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        existingLabelDetail =
            EditDetailFragmentArgs.fromBundle(requireArguments()).existingLabelDetail

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentEditLabelledDetailBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })


        viewmodel.snackbarInt.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


        binding.create.setOnClickListener {
            dismiss()
            viewmodel.addLabelledDetail(viewmodel.selectedLabel.value!!, existingLabelDetail)
        }
        return binding.root
    }


}