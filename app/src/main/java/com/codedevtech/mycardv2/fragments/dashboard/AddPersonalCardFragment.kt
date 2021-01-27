
package com.codedevtech.mycardv2.fragments.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.DropDownAdapter
import com.codedevtech.mycardv2.adapter.rv.EmailAdapter
import com.codedevtech.mycardv2.adapter.rv.PhoneNumberAdapter
import com.codedevtech.mycardv2.adapter.rv.SocialAdapter
import com.codedevtech.mycardv2.databinding.*
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.listeners.EmailItemInteraction
import com.codedevtech.mycardv2.listeners.ItemInteraction
import com.codedevtech.mycardv2.listeners.SocialItemInteraction
import com.codedevtech.mycardv2.models.EmailAddress
import com.codedevtech.mycardv2.models.PhoneNumber
import com.codedevtech.mycardv2.models.SocialMediaProfile
import com.codedevtech.mycardv2.viewmodel.AddPersonalCardViewModel


class AddPersonalCardFragment : Fragment(),ItemInteraction<PhoneNumber>,
    EmailItemInteraction, SocialItemInteraction {

    lateinit var binding: FragmentAddPersonalCardBinding
    lateinit var phoneNumberAdapter: PhoneNumberAdapter
    lateinit var emailAdapter: EmailAdapter
    lateinit var socialAdapter: SocialAdapter

    lateinit var phoneTypes: DropDownAdapter
    lateinit var emailTypes: DropDownAdapter

    val viewmodel: AddPersonalCardViewModel by navGraphViewModels(R.id.add_card_nav){
        defaultViewModelProviderFactory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPersonalCardBinding.inflate(layoutInflater,container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        viewmodel.isNameExpanded.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.fullNameChevron.isSelected = it
            }
        })

        phoneTypes = DropDownAdapter(requireContext(),R.layout.spinner_item, resources.getStringArray(R.array.phone_types))
        emailTypes = DropDownAdapter(requireContext(),R.layout.spinner_item, resources.getStringArray(R.array.email_types))

        phoneNumberAdapter = PhoneNumberAdapter(this, phoneTypes)
        emailAdapter = EmailAdapter(this,emailTypes)
        socialAdapter = SocialAdapter(this)

        phoneNumberAdapter.submitList(listOf(PhoneNumber()))
        emailAdapter.submitList(listOf(EmailAddress()))

        viewmodel.socials.observe(viewLifecycleOwner){ it ->
            socialAdapter.submitList(it.filter { it.usernameOrUrl.isNotEmpty()})
        }

        binding.phoneItems.adapter = phoneNumberAdapter
        binding.emailItems.adapter = emailAdapter
        binding.socialItems.adapter = socialAdapter

        binding.fullNameChevron.setOnClickListener {
            viewmodel.isNameExpanded.value = !viewmodel.isNameExpanded.value!!
        }

        binding.addPhone.setOnClickListener {
            if(phoneNumberAdapter.currentList.none { it.number.isEmpty() })
                phoneNumberAdapter.addPhoneNumber()
        }

        binding.addEmail.setOnClickListener {
            if(emailAdapter.currentList.none { it.address.isEmpty() })
                emailAdapter.addItem()
        }


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onItemClicked(item: PhoneNumber) {
        phoneNumberAdapter.removePhoneNumber(item)
    }

    override fun onItemClicked(item: EmailAddress) {
        emailAdapter.removeItem(item)
    }

    override fun onItemClicked(item: SocialMediaProfile) {
        viewmodel.goToSocialProfile()
    }


}