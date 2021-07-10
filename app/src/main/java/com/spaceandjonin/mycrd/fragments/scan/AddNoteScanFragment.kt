package com.spaceandjonin.mycrd.fragments.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentAddNoteScanBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.ReviewScannedDetailsViewModel

class AddNoteScanFragment : Fragment() {

    private lateinit var binding: FragmentAddNoteScanBinding

    val viewmodel: ReviewScannedDetailsViewModel by navGraphViewModels(R.id.scan_nav) {
        defaultViewModelProviderFactory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentAddNoteScanBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        viewmodel.note.value?.let {
            if (it.isEmpty()) {
                goToSaveState()
            } else {
                goToEditState()
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save -> {
                    viewmodel.cardNote.value = binding.noteText.text.toString()
                    goToEditState()
                }
                R.id.edit -> {
                    goToSaveState()
                }
            }

            return@setOnMenuItemClickListener true
        }


        viewmodel.snackbarInt.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    private fun goToSaveState() {
        binding.toolbar.menu.findItem(R.id.edit).isVisible = false
        binding.toolbar.menu.findItem(R.id.save).isVisible = true
        binding.groupNote.isVisible = true
        binding.role.isVisible = false


    }

    private fun goToEditState() {

        binding.toolbar.menu.findItem(R.id.edit).isVisible = true
        binding.toolbar.menu.findItem(R.id.save).isVisible = false
        binding.groupNote.isVisible = false
        binding.role.isVisible = true
        binding.role.requestFocus()

    }

}