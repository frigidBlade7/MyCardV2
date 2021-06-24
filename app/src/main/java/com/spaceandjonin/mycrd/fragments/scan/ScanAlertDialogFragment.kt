package com.spaceandjonin.mycrd.fragments.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentScanAlertDialogBinding
import com.spaceandjonin.mycrd.viewmodel.ReviewScannedDetailsViewModel


class ScanAlertDialogFragment : DialogFragment() {

    lateinit var binding: FragmentScanAlertDialogBinding

    val viewmodel: ReviewScannedDetailsViewModel by hiltNavGraphViewModels(R.id.scan_nav)


    override fun getTheme(): Int {
        return R.style.Theme_MyCardStyles_Dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentScanAlertDialogBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        //todo complete refactor to move selected card and selected personal card implementations
        // to card view model and stop this copy life na wanyin


        binding.delete.setOnClickListener {
            viewmodel.setDialogDismissed()
            findNavController().popBackStack()
        }

        return binding.root
    }

}