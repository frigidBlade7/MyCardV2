package com.codedevtech.mycardv2.fragments.onboarding

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.AddedCardAdapter
import com.codedevtech.mycardv2.adapter.CardAdapter
import com.codedevtech.mycardv2.adapter.rv.*
import com.codedevtech.mycardv2.databinding.FragmentAddedCardDetailsBinding
import com.codedevtech.mycardv2.databinding.FragmentCardDetailsBinding
import com.codedevtech.mycardv2.databinding.FragmentConfirmDetailsBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.models.LiveCard
import com.codedevtech.mycardv2.models.Name
import com.codedevtech.mycardv2.viewmodel.AddPersonalCardViewModel
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.card.MaterialCardView.OnCheckedChangeListener
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "CardDetailsFragment"

@AndroidEntryPoint
class CardDetailsFragment : Fragment() {

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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CardDetailsFragmentArgs.fromBundle(requireArguments()).card?.let {
            onboardingViewModel.selectedCard.value  = it
        }

/*        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = Color.TRANSPARENT
        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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

        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.options -> {
                    onboardingViewModel.selectedCard.value?.note?.let {
                        onboardingViewModel.showCardOptions(it.isNotEmpty())
                    }
                }
                R.id.share -> onboardingViewModel.showShare()
            }

            return@setOnMenuItemClickListener true
        }
            //binding.toolbar.menu.
        return binding.root
    }

}