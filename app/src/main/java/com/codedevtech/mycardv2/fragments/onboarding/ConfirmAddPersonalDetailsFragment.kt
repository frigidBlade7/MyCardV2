package com.codedevtech.mycardv2.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.CardAdapter
import com.codedevtech.mycardv2.adapter.rv.ExtraEmailAddressAdapter
import com.codedevtech.mycardv2.adapter.rv.ExtraPhoneNumbersAdapter
import com.codedevtech.mycardv2.adapter.rv.SocialAdapter
import com.codedevtech.mycardv2.databinding.FragmentAddPersonalConfirmDetailsBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.viewmodel.AddPersonalCardViewModel
import com.google.android.material.tabs.TabLayoutMediator

class ConfirmAddPersonalDetailsFragment : Fragment() {

    lateinit var binding : FragmentAddPersonalConfirmDetailsBinding
    lateinit var cardAdapter: CardAdapter
    lateinit var socialAdapter: SocialAdapter

    lateinit var extraEmailAddressAdapter: ExtraEmailAddressAdapter
    lateinit var extraPhoneNumbersAdapter: ExtraPhoneNumbersAdapter

    val addPersonalCardViewModel: AddPersonalCardViewModel by hiltNavGraphViewModels(R.id.add_personal_card_nav)




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAddPersonalConfirmDetailsBinding.inflate(layoutInflater, container, false)
        binding.viewModel = addPersonalCardViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        cardAdapter= CardAdapter()

        extraEmailAddressAdapter = ExtraEmailAddressAdapter()
        extraPhoneNumbersAdapter = ExtraPhoneNumbersAdapter()

        binding.categories.pager.adapter = cardAdapter

        socialAdapter = SocialAdapter(null)
        binding.categories.socialMedia.socialItems.adapter = socialAdapter


        //phoneNumberAdapter.submitList(listOf(PhoneNumber()))
        //emailAdapter.submitList(listOf(EmailAddress()))

        binding.categories.email.list.adapter = extraEmailAddressAdapter
        binding.categories.phone.list.adapter = extraPhoneNumbersAdapter

        addPersonalCardViewModel.card.observe(viewLifecycleOwner){
            cardAdapter.submitList(listOf(it))
            if(it.phoneNumbers.size>1)
                extraPhoneNumbersAdapter.submitList(it.phoneNumbers.drop(1))
            if(it.emailAddresses.size>1)
                extraEmailAddressAdapter.submitList(it.emailAddresses.drop(1))
            socialAdapter.submitList(it.socialMediaProfiles)

        }

        binding.categories.email.chevron.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected)
                binding.categories.email.list.visibility = View.VISIBLE
            else
                binding.categories.email.list.visibility = View.GONE
        }

        binding.categories.phone.chevron.setOnClickListener {
            it.isSelected = !it.isSelected

            if(it.isSelected)
                binding.categories.phone.list.visibility = View.VISIBLE
            else
                binding.categories.phone.list.visibility = View.GONE
        }


        addPersonalCardViewModel.destination.observe(viewLifecycleOwner, EventObserver {
/*            if(it.actionId== R.id.action_global_cardDetailsFragment){
                requireParentFragment().findNavController().navigate(it)
            }*/
            findNavController().navigate(it)
        })


        TabLayoutMediator(binding.categories.tabLayout, binding.categories.pager) { _, _ ->
        }.attach()



        addPersonalCardViewModel.snackbarInt.observe(viewLifecycleOwner,EventObserver{
            Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
        })


        binding.toolbar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }
        return binding.root
    }

}