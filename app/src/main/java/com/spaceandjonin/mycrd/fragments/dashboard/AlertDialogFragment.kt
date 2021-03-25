
package com.spaceandjonin.mycrd.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentAlertDialogBinding


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