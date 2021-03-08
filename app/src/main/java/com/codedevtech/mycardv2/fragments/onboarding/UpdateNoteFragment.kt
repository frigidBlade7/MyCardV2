package com.codedevtech.mycardv2.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.AddedCardAdapter
import com.codedevtech.mycardv2.adapter.CardAdapter
import com.codedevtech.mycardv2.adapter.rv.ExtraEmailAddressAdapter
import com.codedevtech.mycardv2.adapter.rv.ExtraPhoneNumbersAdapter
import com.codedevtech.mycardv2.adapter.rv.SocialAdapter
import com.codedevtech.mycardv2.databinding.FragmentAddConfirmDetailsBinding
import com.codedevtech.mycardv2.databinding.FragmentAddNoteBinding
import com.codedevtech.mycardv2.databinding.FragmentUpdateNoteBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.utils.hideKeyboard
import com.codedevtech.mycardv2.utils.notifyObserver
import com.codedevtech.mycardv2.utils.showKeyboard
import com.codedevtech.mycardv2.viewmodel.AddCardViewModel
import com.codedevtech.mycardv2.viewmodel.AddPersonalCardViewModel
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.google.android.material.tabs.TabLayoutMediator

class UpdateNoteFragment : Fragment() {

    lateinit var binding : FragmentUpdateNoteBinding
    val onboardingViewModel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentUpdateNoteBinding.inflate(layoutInflater, container, false)
        binding.viewModel = onboardingViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        onboardingViewModel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        onboardingViewModel.selectedCard.value?.note?.let {
            if(it.isEmpty()){
                goToSaveState()
            }
            else{
                goToEditState()
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.save -> {

                    //onboardingViewModel.selectedCard.value?.note = binding.noteText.text.toString()
                    //onboardingViewModel.selectedCard.notifyObserver()
                    goToEditState()
                    onboardingViewModel.updateNote()
                }
                R.id.edit -> {

                    goToSaveState()
                }
            }

            return@setOnMenuItemClickListener true
        }


        onboardingViewModel.snackbarInt.observe(viewLifecycleOwner,EventObserver{
            Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
        })


        binding.toolbar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }
        return binding.root
    }


    private fun goToSaveState() {
        binding.toolbar.menu.findItem(R.id.edit).isVisible=false
        binding.toolbar.menu.findItem(R.id.save).isVisible=true
        binding.groupNote.isVisible = true
        binding.role.isVisible = false
        binding.noteText.requestFocus()
        activity?.showKeyboard(binding.root)
    }

    private fun goToEditState() {

        binding.toolbar.menu.findItem(R.id.edit).isVisible=true
        binding.toolbar.menu.findItem(R.id.save).isVisible=false
        binding.groupNote.isVisible = false
        binding.role.isVisible = true
        binding.role.requestFocus()
        activity?.hideKeyboard(binding.root)
    }
}