package com.codedevtech.mycardv2.fragments.onboarding

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.PersonalCardAdapter
import com.codedevtech.mycardv2.adapter.rv.*
import com.codedevtech.mycardv2.databinding.FragmentPersonalCardDetailsBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.google.android.material.color.MaterialColors
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "CardDetailsFragment"

@AndroidEntryPoint
class ViewPersonalCardDetailsFragment : Fragment() {

    lateinit var binding : FragmentPersonalCardDetailsBinding
    lateinit var cardAdapter: PersonalCardAdapter
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



    }

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

        binding = FragmentPersonalCardDetailsBinding.inflate(layoutInflater, container, false)
        binding.onboardingViewModel = onboardingViewModel
        //binding.viewModel = addPersonalCardViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        cardAdapter= PersonalCardAdapter()
        extraEmailAddressAdapter = ExtraEmailAddressAdapter()
        extraPhoneNumbersAdapter = ExtraPhoneNumbersAdapter()

        binding.include.pager.adapter = cardAdapter
        binding.include.email.list.adapter = extraEmailAddressAdapter
        binding.include.phone.list.adapter = extraPhoneNumbersAdapter

        onboardingViewModel.selectedPersonalCard.observe(viewLifecycleOwner){
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


        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.options-> onboardingViewModel.showPersonalCardOptions()
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