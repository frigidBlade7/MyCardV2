package com.spaceandjonin.mycrd.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.adapter.AddedCardAdapter
import com.spaceandjonin.mycrd.adapter.rv.ExtraEmailAddressAdapter
import com.spaceandjonin.mycrd.adapter.rv.ExtraPhoneNumbersAdapter
import com.spaceandjonin.mycrd.adapter.rv.SocialAdapter
import com.spaceandjonin.mycrd.databinding.FragmentAddConfirmDetailsBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.AddCardViewModel

class ConfirmAddDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAddConfirmDetailsBinding
    private lateinit var cardAdapter: AddedCardAdapter
    private lateinit var socialAdapter: SocialAdapter

    private lateinit var extraEmailAddressAdapter: ExtraEmailAddressAdapter
    private lateinit var extraPhoneNumbersAdapter: ExtraPhoneNumbersAdapter

    val viewmodel: AddCardViewModel by navGraphViewModels(R.id.add_card_nav) {
        defaultViewModelProviderFactory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentAddConfirmDetailsBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner
        cardAdapter = AddedCardAdapter()

        extraEmailAddressAdapter = ExtraEmailAddressAdapter()
        extraPhoneNumbersAdapter = ExtraPhoneNumbersAdapter()

        binding.categories.pager.adapter = cardAdapter

        socialAdapter = SocialAdapter(null)
        binding.categories.socialMedia.socialItems.adapter = socialAdapter

        binding.categories.email.list.adapter = extraEmailAddressAdapter
        binding.categories.phone.list.adapter = extraPhoneNumbersAdapter

        viewmodel.card.observe(viewLifecycleOwner) {
            cardAdapter.submitList(listOf(it))
            if (it.phoneNumbers.size > 1)
                extraPhoneNumbersAdapter.submitList(it.phoneNumbers.drop(1))
            if (it.emailAddresses.size > 1)
                extraEmailAddressAdapter.submitList(it.emailAddresses.drop(1))
            socialAdapter.submitList(it.socialMediaProfiles)

        }



        binding.categories.email.chevron.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected)
                binding.categories.email.list.visibility = View.VISIBLE
            else
                binding.categories.email.list.visibility = View.GONE
        }

        binding.categories.phone.chevron.setOnClickListener {
            it.isSelected = !it.isSelected

            if (it.isSelected)
                binding.categories.phone.list.visibility = View.VISIBLE
            else
                binding.categories.phone.list.visibility = View.GONE
        }


        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })


        TabLayoutMediator(binding.categories.tabLayout, binding.categories.pager) { _, _ ->
        }.attach()



        viewmodel.snackbarInt.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


        binding.categories.note.cardView.setOnClickListener {
            //it.isVisible = false
            //binding.categories.note.groupNote.isVisible = true
            findNavController().navigate(ConfirmAddDetailsFragmentDirections.actionGlobalAddNoteFragment())
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

}