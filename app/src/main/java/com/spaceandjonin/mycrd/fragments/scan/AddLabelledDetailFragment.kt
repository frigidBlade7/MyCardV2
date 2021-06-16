package com.spaceandjonin.mycrd.fragments.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentAddLabelledDetailBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.ReviewScannedDetailsViewModel

class AddLabelledDetailFragment : Fragment() {

    lateinit var binding : FragmentAddLabelledDetailBinding

    val viewmodel: ReviewScannedDetailsViewModel by navGraphViewModels(R.id.scan_nav){
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAddLabelledDetailBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })


        binding.toolbar.setNavigationOnClickListener {
            viewmodel.selectedLabel.value =""
            viewmodel.selectedLabelType.value =""
            viewmodel.selectedDetail.value =""
            findNavController().popBackStack()
        }


        viewmodel.snackbarInt.observe(viewLifecycleOwner,EventObserver{
            Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
        })


        binding.toolbar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }

        binding.create.setOnClickListener {
            viewmodel.addLabelledDetail(viewmodel.selectedLabel.value!!, null)
            findNavController().popBackStack()
        }
        return binding.root
    }


}