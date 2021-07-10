package com.spaceandjonin.mycrd.fragments.scan

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.adapter.rv.ChildEmailAdapter
import com.spaceandjonin.mycrd.adapter.rv.ChildPhoneAdapter
import com.spaceandjonin.mycrd.adapter.rv.SocialDisplayAdapter
import com.spaceandjonin.mycrd.databinding.FragmentAssignLabelsBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.listeners.EmailTypeInteraction
import com.spaceandjonin.mycrd.listeners.ItemInteraction
import com.spaceandjonin.mycrd.listeners.PhoneTypeInteraction
import com.spaceandjonin.mycrd.models.EmailAddress
import com.spaceandjonin.mycrd.models.PhoneNumber
import com.spaceandjonin.mycrd.models.SocialMediaProfile
import com.spaceandjonin.mycrd.viewmodel.ReviewScannedDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditLabelsFragment : BottomSheetDialogFragment(),
    ItemInteraction<SocialMediaProfile.SocialMedia>, PhoneTypeInteraction,
    EmailTypeInteraction {

    private lateinit var binding: FragmentAssignLabelsBinding
    private lateinit var socialDisplayAdapter: SocialDisplayAdapter
    private lateinit var phoneNumberAdapter: ChildPhoneAdapter
    private lateinit var emailAddressAdapter: ChildEmailAdapter

    val viewmodel: ReviewScannedDetailsViewModel by navGraphViewModels(R.id.scan_nav) {
        defaultViewModelProviderFactory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.behavior.skipCollapsed = true
        dialog.behavior.setPeekHeight(resources.displayMetrics.heightPixels, true)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAssignLabelsBinding.inflate(layoutInflater, container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        val listOfTextViews = listOf<TextView>(
            binding.websiteLabel, binding.locationLabel, binding.roleLabel,
            binding.companyNameLabel, binding.fullNameLabel
        )

        socialDisplayAdapter = SocialDisplayAdapter(this)
        binding.socialOptions.adapter = socialDisplayAdapter
        socialDisplayAdapter.submitList(SocialMediaProfile.SocialMedia.values().toMutableList())

        phoneNumberAdapter = ChildPhoneAdapter(this)
        binding.phoneOptions.adapter = phoneNumberAdapter
        phoneNumberAdapter.submitList(PhoneNumber.PhoneNumberType.values().toMutableList())

        emailAddressAdapter = ChildEmailAdapter(this)
        binding.emailOptions.adapter = emailAddressAdapter
        emailAddressAdapter.submitList(EmailAddress.EmailType.values().toMutableList())


        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        listOfTextViews.forEach { textview ->
            textview.setOnClickListener {
                viewmodel.selectedLabelType.value = ""
                viewmodel.selectedLabel.value = textview.text.toString()
                dismiss()
            }
        }


        return binding.root
    }


    override fun onItemClicked(item: SocialMediaProfile.SocialMedia) {
        viewmodel.selectedLabelType.value = ""
        viewmodel.selectedLabel.value = item.name
        dismiss()
    }

    override fun onItemClicked(item: EmailAddress.EmailType) {
        viewmodel.selectedLabelType.value = getString(R.string.email_address)
        viewmodel.selectedLabel.value = item.name
        dismiss()
    }

    override fun onItemClicked(item: PhoneNumber.PhoneNumberType) {
        viewmodel.selectedLabelType.value = getString(R.string.phone_number)
        viewmodel.selectedLabel.value = item.name
        dismiss()
    }

}