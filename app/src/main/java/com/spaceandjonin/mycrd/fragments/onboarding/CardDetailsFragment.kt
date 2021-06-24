package com.spaceandjonin.mycrd.fragments.onboarding

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.adapter.AddedCardAdapter
import com.spaceandjonin.mycrd.adapter.rv.*
import com.spaceandjonin.mycrd.databinding.FragmentAddedCardDetailsBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.fragments.dashboard.AlertDialogFragmentDirections
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "CardDetailsFragment"

@AndroidEntryPoint
class CardDetailsFragment : Fragment() {

    lateinit var binding: FragmentAddedCardDetailsBinding
    lateinit var cardAdapter: AddedCardAdapter
    lateinit var socialAdapter: SocialAdapter
    lateinit var extraEmailAddressAdapter: ExtraEmailAddressAdapter
    lateinit var extraPhoneNumbersAdapter: ExtraPhoneNumbersAdapter


    val onboardingViewModel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CardDetailsFragmentArgs.fromBundle(requireArguments()).card?.let {
            onboardingViewModel.selectedCard.value = it
        }

/*        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = Color.TRANSPARENT
        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentAddedCardDetailsBinding.inflate(layoutInflater, container, false)
        binding.onboardingViewModel = onboardingViewModel
        //binding.viewModel = addPersonalCardViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        cardAdapter = AddedCardAdapter()
        extraEmailAddressAdapter = ExtraEmailAddressAdapter()
        extraPhoneNumbersAdapter = ExtraPhoneNumbersAdapter()

        binding.include.email.list.adapter = extraEmailAddressAdapter
        binding.include.phone.list.adapter = extraPhoneNumbersAdapter

        onboardingViewModel.selectedCard.observe(viewLifecycleOwner) {
            cardAdapter.submitList(listOf(it))
            if (it.phoneNumbers.size > 1)
                extraPhoneNumbersAdapter.submitList(it.phoneNumbers.drop(1))
            if (it.emailAddresses.size > 1)
                extraEmailAddressAdapter.submitList(it.emailAddresses.drop(1))
            socialAdapter.submitList(it.socialMediaProfiles)

        }

        binding.include.makeCall.setOnClickListener {
            onboardingViewModel.selectedCard.value?.phoneNumbers?.let {

                if (it.isEmpty()) {
                    val destination = AlertDialogFragmentDirections.actionGlobalAlertDialogFragment(
                        R.string.no_phone_specified_for_this_card_add_and_try_again,
                        R.drawable.phone_icon
                    )
                    findNavController().navigate(destination)

                } else if (it.size == 1) {
                    Uri.parse("tel:${it[0].number.trim()}")?.let { number ->
                        try {
                            startActivity(Intent(Intent.ACTION_DIAL, number))
                        } catch (e: ActivityNotFoundException) {
                            // Define what your app should do if no activity can handle the intent.
                        }
                    }
                } else {
                    findNavController().navigate(CardDetailsFragmentDirections.actionGlobalSelectPhoneNumberFragment())

                }
            }
        }

        binding.include.sendMail.setOnClickListener {
            onboardingViewModel.selectedCard.value?.emailAddresses?.let {

                if (it.isEmpty()) {

                    val destination = AlertDialogFragmentDirections.actionGlobalAlertDialogFragment(
                        R.string.no_email_specified_for_this_card_add_and_try_again,
                        R.drawable.mail_icon
                    )
                    findNavController().navigate(destination)


                } else if (it.size == 1) {
                    try {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data =
                                Uri.parse("mailto:${it[0].address}") // only email apps should handle this
                            putExtra(Intent.EXTRA_EMAIL, it[0].address)
                        }
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        // Define what your app should do if no activity can handle the intent.
                    }

                } else {
                    findNavController().navigate(CardDetailsFragmentDirections.actionGlobalSelectEmailFragment())


                }
            }
        }

        binding.include.showLocation.setOnClickListener {
            onboardingViewModel.selectedCard.value?.businessInfo?.companyAddress?.let {

                if (it.isEmpty()) {

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


        binding.include.email.chevron.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected)
                binding.include.email.list.visibility = View.VISIBLE
            else
                binding.include.email.list.visibility = View.GONE
        }

        binding.include.phone.chevron.setOnClickListener {
            it.isSelected = !it.isSelected

            if (it.isSelected)
                binding.include.phone.list.visibility = View.VISIBLE
            else
                binding.include.phone.list.visibility = View.GONE
        }

        socialAdapter = SocialAdapter(null)
        binding.include.socialMedia.socialItems.adapter = socialAdapter


        onboardingViewModel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })



        onboardingViewModel.selectedCard.value?.note?.let {
            if (it.isNotEmpty()) {
                binding.include.note.role.text = it
            } else
                binding.include.note.role.setOnClickListener {
                    //todo show modal
                }
        }

        binding.include.note.cardView.setOnClickListener {
            findNavController().navigate(CardDetailsFragmentDirections.actionGlobalUpdateNoteFragment())
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.options -> {
                    onboardingViewModel.selectedCard.value?.note?.let {
                        //onboardingViewModel.tempCardByteArray = binding.include.pager[0].findViewById<ImageView>(R.id.icon).drawToBitmap().toByteArray()
                        onboardingViewModel.showCardOptions(it.isNotEmpty())
                    }
                }
            }

            return@setOnMenuItemClickListener true
        }
        //binding.toolbar.menu.
        return binding.root
    }

}