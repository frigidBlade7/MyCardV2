package com.spaceandjonin.mycrd.fragments.dashboard

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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.adapter.rv.SelectPhoneAdapter
import com.spaceandjonin.mycrd.databinding.FragmentSelectPhoneNumberBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.listeners.ItemInteraction
import com.spaceandjonin.mycrd.models.PhoneNumber
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectPhoneNumberFragment : BottomSheetDialogFragment(), ItemInteraction<PhoneNumber> {

    private lateinit var binding: FragmentSelectPhoneNumberBinding
    private lateinit var adapter: SelectPhoneAdapter

    val viewmodel: OnboardingViewModel by navGraphViewModels(R.id.onboarding_nav) {
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
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSelectPhoneNumberBinding.inflate(layoutInflater, container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = SelectPhoneAdapter(this)
        viewmodel.selectedCard.observe(viewLifecycleOwner) {
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
        Uri.parse("tel:${item.number.trim()}")?.let { number ->
            try {
                startActivity(Intent(Intent.ACTION_DIAL, number))
            } catch (e: ActivityNotFoundException) {
                // Define what your app should do if no activity can handle the intent.
            }
        }

        dismiss()
    }


}