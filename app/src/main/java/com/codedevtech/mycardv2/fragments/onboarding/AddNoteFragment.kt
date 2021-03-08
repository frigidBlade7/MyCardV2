package com.codedevtech.mycardv2.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.FragmentAddNoteBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.utils.notifyObserver
import com.codedevtech.mycardv2.viewmodel.AddCardViewModel

class AddNoteFragment : Fragment() {

    lateinit var binding : FragmentAddNoteBinding

    val viewmodel: AddCardViewModel by navGraphViewModels(R.id.add_card_nav){
        defaultViewModelProviderFactory
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAddNoteBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        viewmodel.card.value?.note?.let {
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
                    viewmodel.card.value?.note = binding.noteText.text.toString()
                    viewmodel.card.notifyObserver()
                    goToEditState()
                }
                R.id.edit -> {
                    goToSaveState()
                }
            }

            return@setOnMenuItemClickListener true
        }


        viewmodel.snackbarInt.observe(viewLifecycleOwner,EventObserver{
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


    }

    private fun goToEditState() {

        binding.toolbar.menu.findItem(R.id.edit).isVisible=true
        binding.toolbar.menu.findItem(R.id.save).isVisible=false
        binding.groupNote.isVisible = false
        binding.role.isVisible = true
        binding.role.requestFocus()

    }

}