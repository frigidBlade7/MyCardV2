
package com.spaceandjonin.mycrd.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentAddWorkBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.AddCardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class EditWorkFragment : Fragment() {

    lateinit var binding: FragmentAddWorkBinding
    val viewmodel: AddCardViewModel by navGraphViewModels(R.id.add_card_nav){
        defaultViewModelProviderFactory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddWorkBinding.inflate(layoutInflater,container, false)

        binding.info.visibility = View.GONE
        binding.companyLogo.visibility = View.GONE
        binding.updateIcon.visibility = View.GONE

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


/*        binding.updateIcon.setOnClickListener {
            callGallery()
        }*/

        return binding.root
    }


}