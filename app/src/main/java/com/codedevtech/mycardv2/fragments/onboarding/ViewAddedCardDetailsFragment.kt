package com.codedevtech.mycardv2.fragments.onboarding

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.drawToBitmap
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.AddedCardAdapter
import com.codedevtech.mycardv2.adapter.rv.*
import com.codedevtech.mycardv2.databinding.FragmentAddedCardDetailsBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.fragments.dashboard.AlertDialogFragmentDirections
import com.codedevtech.mycardv2.utils.toByteArray
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.google.android.material.color.MaterialColors
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "CardDetailsFragment"

@AndroidEntryPoint
class ViewAddedCardDetailsFragment : Fragment() {

    lateinit var binding : FragmentAddedCardDetailsBinding
    lateinit var cardAdapter: AddedCardAdapter
    lateinit var socialAdapter: SocialAdapter
    lateinit var extraEmailAddressAdapter: ExtraEmailAddressAdapter
    lateinit var extraPhoneNumbersAdapter: ExtraPhoneNumbersAdapter


    val onboardingViewModel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)
/*

    val addPersonalCardViewModel: AddPersonalCardViewModel by navGraphViewModels(R.id.add_card_nav){
        defaultViewModelProviderFactory
    }
*/



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(MaterialColors.getColor(requireView(),R.attr.colorSurface))
        }

        binding.toolbar.setNavigationOnClickListener{
            findNavController().navigateUp()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAddedCardDetailsBinding.inflate(layoutInflater, container, false)
        binding.onboardingViewModel = onboardingViewModel
        //binding.viewModel = addPersonalCardViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        cardAdapter= AddedCardAdapter()
        extraEmailAddressAdapter = ExtraEmailAddressAdapter()
        extraPhoneNumbersAdapter = ExtraPhoneNumbersAdapter()

        binding.include.pager.adapter = cardAdapter
        binding.include.email.list.adapter = extraEmailAddressAdapter
        binding.include.phone.list.adapter = extraPhoneNumbersAdapter

        onboardingViewModel.selectedCard.observe(viewLifecycleOwner){
            cardAdapter.submitList(listOf(it))
            if(it.phoneNumbers.size>1)
                extraPhoneNumbersAdapter.submitList(it.phoneNumbers.drop(1))
            if(it.emailAddresses.size>1)
                extraEmailAddressAdapter.submitList(it.emailAddresses.drop(1))
            socialAdapter.submitList(it.socialMediaProfiles)

        }

        binding.include.email.chevron.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected)
                binding.include.email.list.visibility = View.VISIBLE
            else
                binding.include.email.list.visibility = View.GONE
        }

        binding.include.phone.chevron.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected)
                binding.include.phone.list.visibility = View.VISIBLE
            else
                binding.include.phone.list.visibility = View.GONE
        }

        socialAdapter = SocialAdapter(null)
        binding.include.socialMedia.socialItems.adapter = socialAdapter

        onboardingViewModel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        TabLayoutMediator(binding.include.tabLayout, binding.include.pager) { _, _ ->
        }.attach()


        onboardingViewModel.selectedCard.value?.note?.let {
            if(it.isNotEmpty()) {
                binding.include.note.role.text = it
            }
            else
                binding.include.note.role.setOnClickListener {
                    //todo show modal
                }
        }

        binding.include.note.cardView.setOnClickListener {
            //it.isVisible = false
            //binding.categories.note.groupNote.isVisible = true
            findNavController().navigate(ViewAddedCardDetailsFragmentDirections.actionGlobalUpdateNoteFragment())
        }

        binding.include.makeCall.setOnClickListener {
            onboardingViewModel.selectedCard.value?.phoneNumbers?.let {

                if(it.isEmpty()) {
                    val destination = AlertDialogFragmentDirections.actionGlobalAlertDialogFragment(
                        R.string.no_phone_specified_for_this_card_add_and_try_again,
                        R.drawable.phone_icon)
                    findNavController().navigate(destination)

                } else if(it.size==1){
                    Uri.parse("tel:${it[0].number}")?.let { number ->
                        try {
                            startActivity(Intent(Intent.ACTION_DIAL, number))
                        } catch (e: ActivityNotFoundException) {
                            // Define what your app should do if no activity can handle the intent.
                        }
                    }
                }else {
                    findNavController().navigate(ViewAddedCardDetailsFragmentDirections.actionGlobalSelectPhoneNumberFragment())
                }
            }
        }

        binding.include.sendMail.setOnClickListener {
            onboardingViewModel.selectedCard.value?.emailAddresses?.let {

                if(it.isEmpty()) {

                    val destination = AlertDialogFragmentDirections.actionGlobalAlertDialogFragment(
                        R.string.no_email_specified_for_this_card_add_and_try_again,
                        R.drawable.mail_icon
                    )
                    findNavController().navigate(destination)


                } else if(it.size==1) {
                            try {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:${it[0].address}") // only email apps should handle this
                                    putExtra(Intent.EXTRA_EMAIL, it[0].address)
                                }
                                startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                // Define what your app should do if no activity can handle the intent.
                            }

                }

                else {
                    findNavController().navigate(ViewAddedCardDetailsFragmentDirections.actionGlobalSelectEmailFragment())

                }
            }
        }

        binding.include.showLocation.setOnClickListener {
            onboardingViewModel.selectedCard.value?.businessInfo?.companyAddress?.let {

                if(it.isEmpty()) {

                    val destination = AlertDialogFragmentDirections.actionGlobalAlertDialogFragment(
                        R.string.no_location_specified_for_this_card_add_and_try_again,
                        R.drawable.location_icon
                    )
                    findNavController().navigate(destination)


                } else {
                    Uri.parse(
                        "geo:0,0?q=$it"
                    )?.let { location ->
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, location))
                        } catch (e: ActivityNotFoundException) {
                            // Define what your app should do if no activity can handle the intent.
                        }
                    }
                }
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.options-> {
                    onboardingViewModel.selectedCard.value?.note?.let {
                        //onboardingViewModel.tempCardByteArray = binding.include.pager[0].findViewById<ImageView>(R.id.icon).drawToBitmap().toByteArray()
                        //onboardingViewModel.setUpImageBytearray()
                        onboardingViewModel.showCardOptions(it.isNotEmpty())
                    }
                }
/*
                R.id.share-> onboardingViewModel.showShare()
*/
            }

            return@setOnMenuItemClickListener true
        }
            //binding.toolbar.menu.
        return binding.root
    }

}