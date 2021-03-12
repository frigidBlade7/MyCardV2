package com.codedevtech.mycardv2.fragments.dashboard

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.rv.SelectPhoneAdapter
import com.codedevtech.mycardv2.databinding.FragmentAddSocialsBinding
import com.codedevtech.mycardv2.databinding.FragmentSelectPhoneNumberBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.listeners.ItemInteraction
import com.codedevtech.mycardv2.models.PhoneNumber
import com.codedevtech.mycardv2.viewmodel.AddCardViewModel
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectPhoneNumberFragment : BottomSheetDialogFragment(), ItemInteraction<PhoneNumber>{

    lateinit var binding: FragmentSelectPhoneNumberBinding
    lateinit var adapter: SelectPhoneAdapter

    val viewmodel: OnboardingViewModel by navGraphViewModels(R.id.onboarding_nav){
        defaultViewModelProviderFactory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        //dialog.behavior.skipCollapsed = true
        //dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED


        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectPhoneNumberBinding.inflate(layoutInflater,container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = SelectPhoneAdapter(this)
        viewmodel.selectedCard.observe(viewLifecycleOwner){
            it?.phoneNumbers?.let {
                adapter.submitList(it)
                binding.nestedScrollView.adapter = adapter
            }
        }



        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })


        return binding.root
    }

    override fun onItemClicked(item: PhoneNumber) {
        //TODO("Not yet implemented")
        Uri.parse("tel:${item.number}")?.let { number ->
            try {
                startActivity(Intent(Intent.ACTION_DIAL, number))
            } catch (e: ActivityNotFoundException) {
                // Define what your app should do if no activity can handle the intent.
            }
        }

        dismiss()
    }


}