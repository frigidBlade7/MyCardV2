package com.codedevtech.mycardv2.fragments.dashboard

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddSocialsFragment : BottomSheetDialogFragment(){

    lateinit var binding: FragmentAddSocialsBinding

    val viewmodel: AddPersonalCardViewModel by navGraphViewModels(R.id.add_card_nav){
        defaultViewModelProviderFactory
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.behavior.skipCollapsed = true

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddSocialsBinding.inflate(layoutInflater,container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.linkedin.item = viewmodel.socials.value!![0]
        binding.facebook.item = viewmodel.socials.value!![1]
        binding.twitter.item = viewmodel.socials.value!![2]
        binding.instagram.item = viewmodel.socials.value!![3]

/*
        binding.linkedin.isSelected = viewmodel.socials.value!![0].usernameOrUrl.isNotEmpty()
        binding.facebook.isSelected = viewmodel.socials.value!![1].usernameOrUrl.isNotEmpty()
        binding.twitter.isSelected = viewmodel.socials.value!![2].usernameOrUrl.isNotEmpty()
        binding.instagram.isSelected = viewmodel.socials.value!![3].usernameOrUrl.isNotEmpty()*/

        binding.facebook.edit.setOnClickListener {
            binding.facebook.edit.isSelected = !binding.facebook.edit.isSelected

            if(!binding.facebook.edit.isSelected) {
                binding.facebook.usernameLayout.visibility = View.GONE
                binding.facebook.username.editableText.clear()
            }else
                binding.facebook.usernameLayout.visibility = View.VISIBLE

        }

        binding.twitter.edit.setOnClickListener {
            binding.twitter.edit.isSelected = !binding.twitter.edit.isSelected

            if(!binding.twitter.edit.isSelected) {
                binding.twitter.usernameLayout.visibility = View.GONE
                binding.twitter.username.editableText.clear()
            }else
                binding.twitter.usernameLayout.visibility = View.VISIBLE
        }
        binding.instagram.edit.setOnClickListener {
            binding.instagram.edit.isSelected = !binding.instagram.edit.isSelected

            if(!binding.instagram.edit.isSelected) {
                binding.instagram.usernameLayout.visibility = View.GONE
                binding.instagram.username.editableText.clear()
            }else
                binding.instagram.usernameLayout.visibility = View.VISIBLE
        }
        binding.linkedin.edit.setOnClickListener {
            binding.linkedin.edit.isSelected = !binding.linkedin.edit.isSelected

            if(!binding.linkedin.edit.isSelected) {
                binding.linkedin.usernameLayout.visibility = View.GONE
                binding.linkedin.username.editableText.clear()
            }else
                binding.linkedin.usernameLayout.visibility = View.VISIBLE

        }

        binding.button.setOnClickListener {
            viewmodel.socials.value?.let {
                viewmodel.socials.value = it.toMutableList()
            }
            dismiss()
        }

        return binding.root
    }


}