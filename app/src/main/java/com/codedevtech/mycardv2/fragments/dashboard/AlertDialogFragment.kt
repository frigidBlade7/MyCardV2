
package com.codedevtech.mycardv2.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.FragmentAlertDialogBinding
import com.codedevtech.mycardv2.databinding.FragmentConfirmNumberDialogBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel


class AlertDialogFragment : DialogFragment() {

    lateinit var binding: FragmentAlertDialogBinding

    override fun getTheme(): Int {
        return R.style.Theme_MyCardStyles_Dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlertDialogBinding.inflate(layoutInflater,container, false)

        binding.icon.apply {
            setImageResource(AlertDialogFragmentArgs.fromBundle(requireArguments()).drawableId)
        }
        binding.add.apply {
            setText(AlertDialogFragmentArgs.fromBundle(requireArguments()).messageId)
        }


        binding.okay.setOnClickListener {
            dismiss()
        }


        return binding.root
    }

}