package com.spaceandjonin.mycrd.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentAddPersonalWorkBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.AddPersonalCardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class AddPersonalWorkFragment : Fragment() {

    lateinit var binding: FragmentAddPersonalWorkBinding

    val viewmodel: AddPersonalCardViewModel by navGraphViewModels(R.id.add_personal_card_nav) {
        defaultViewModelProviderFactory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddPersonalWorkBinding.inflate(layoutInflater, container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewmodel.isEditFlow.observe(viewLifecycleOwner) {
            if (it) {
                binding.button.text = resources.getText(R.string.save)
                binding.button.setOnClickListener {
                    viewmodel.updateCard()
                }
            }
        }

        return binding.root
    }

}