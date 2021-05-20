package com.spaceandjonin.mycrd.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentUpdateNoteBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.utils.hideKeyboard
import com.spaceandjonin.mycrd.utils.showKeyboard
import com.spaceandjonin.mycrd.viewmodel.AddCardViewModel
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateNoteFragment : Fragment() {

    lateinit var binding : FragmentUpdateNoteBinding
    val onboardingViewModel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)
    val cardViewModel: AddCardViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentUpdateNoteBinding.inflate(layoutInflater, container, false)
        binding.cardViewModel = cardViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        cardViewModel.card.value = onboardingViewModel.selectedCard.value?.copy()
        cardViewModel.note.value = onboardingViewModel.selectedCard.value?.note

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
                    cardViewModel.updateNote()
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

        cardViewModel.snackbarInt.observe(viewLifecycleOwner,EventObserver{
            Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
        })

        cardViewModel.destination.observe(viewLifecycleOwner,EventObserver{
            if(it.actionId==0) {
                //success
                onboardingViewModel.selectedCard.value = cardViewModel.card.value?.copy()
                findNavController().popBackStack()
            }
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
        activity?.showKeyboard(/*binding.root*/)
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