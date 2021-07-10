package com.spaceandjonin.mycrd.fragments.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import com.spaceandjonin.mycrd.MainActivity
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.adapter.DropDownAdapter
import com.spaceandjonin.mycrd.adapter.rv.EmailAdapter
import com.spaceandjonin.mycrd.adapter.rv.PhoneNumberAdapter
import com.spaceandjonin.mycrd.adapter.rv.SocialAdapter
import com.spaceandjonin.mycrd.databinding.FragmentAddCardBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.listeners.EmailItemInteraction
import com.spaceandjonin.mycrd.listeners.ItemInteraction
import com.spaceandjonin.mycrd.listeners.SocialItemInteraction
import com.spaceandjonin.mycrd.models.EmailAddress
import com.spaceandjonin.mycrd.models.PhoneNumber
import com.spaceandjonin.mycrd.models.SocialMediaProfile
import com.spaceandjonin.mycrd.utils.aggregateNameToFullName
import com.spaceandjonin.mycrd.utils.notifyObserver
import com.spaceandjonin.mycrd.utils.segregateFullName
import com.spaceandjonin.mycrd.viewmodel.AddCardViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AddPersonalCardFragment"

@AndroidEntryPoint
class AddCardFragment : Fragment(), ItemInteraction<PhoneNumber>,
    EmailItemInteraction, SocialItemInteraction {

    private lateinit var binding: FragmentAddCardBinding
    private lateinit var phoneNumberAdapter: PhoneNumberAdapter
    private lateinit var emailAdapter: EmailAdapter
    private lateinit var socialAdapter: SocialAdapter

    private lateinit var phoneTypes: DropDownAdapter
    private lateinit var emailTypes: DropDownAdapter

    val viewmodel: AddCardViewModel by navGraphViewModels(R.id.add_card_nav) {
        defaultViewModelProviderFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isedit = AddCardFragmentArgs.fromBundle(requireArguments()).isEdit
        viewmodel.isEditFlow.value = isedit
        if (isedit) {

            AddCardFragmentArgs.fromBundle(requireArguments()).existingCard?.let {
                viewmodel.card.value?.id = it.id
                viewmodel.name.value = it.name.copy()
                viewmodel.businessInfo.value = it.businessInfo.copy()
                viewmodel.phoneNumbers.value = it.phoneNumbers.toMutableList()
                viewmodel.emailAddresses.value = it.emailAddresses.toMutableList()
                viewmodel.card.value?.createdAt = it.createdAt
                viewmodel.card.value?.profilePicUrl = it.profilePicUrl
                viewmodel.card.value?.note = it.note

            }

            AddCardFragmentArgs.fromBundle(requireArguments()).existingCard?.socialMediaProfiles?.let {
                for (item in it) {
                    viewmodel.socials.value?.set(it.indexOf(item), item)
                }
            }
            viewmodel.note.value =
                AddCardFragmentArgs.fromBundle(requireArguments()).existingCard?.note
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = requireActivity() as MainActivity

        viewmodel.isEditFlow.observe(viewLifecycleOwner) {
            if (!it) {
                enterTransition = MaterialContainerTransform().apply {
                    // Manually add the Views to be shared since this is not a standard Fragment to
                    // Fragment shared element transition.
                    startView = mainActivity.binding.addCard
                    endView = binding.layout
/*
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
*/
                    scrimColor = Color.TRANSPARENT
                    containerColor = MaterialColors.getColor(view, R.attr.colorSurface)
                    startContainerColor = MaterialColors.getColor(view, R.attr.colorPrimary)
                    endContainerColor = MaterialColors.getColor(view, R.attr.colorSurface)
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddCardBinding.inflate(layoutInflater, container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        viewmodel.isNameExpanded.observe(viewLifecycleOwner) {
            it?.let {
                binding.fullNameChevron.isSelected = it
            }
        }

        phoneTypes = DropDownAdapter(
            requireContext(),
            R.layout.spinner_item,
            resources.getStringArray(R.array.phone_types)
        )
        emailTypes = DropDownAdapter(
            requireContext(),
            R.layout.spinner_item,
            resources.getStringArray(R.array.email_types)
        )

        phoneNumberAdapter = PhoneNumberAdapter(this, phoneTypes)
        emailAdapter = EmailAdapter(this, emailTypes)
        socialAdapter = SocialAdapter(this)

        viewmodel.socials.observe(viewLifecycleOwner) { it ->
            socialAdapter.submitList(it.filter { it.usernameOrUrl.isNotEmpty() })
        }

        viewmodel.phoneNumbers.observe(viewLifecycleOwner) { it ->
            phoneNumberAdapter.submitList(it.toMutableList())
        }

        viewmodel.emailAddresses.observe(viewLifecycleOwner) { it ->
            emailAdapter.submitList(it.toMutableList())
        }


        binding.phoneItems.adapter = phoneNumberAdapter
        binding.emailItems.adapter = emailAdapter
        binding.socialItems.adapter = socialAdapter


        binding.fullNameChevron.setOnClickListener {
            viewmodel.isNameExpanded.value = !it.isSelected

            if (it.isSelected)
                viewmodel.name.value?.segregateFullName()
            else
                viewmodel.name.value?.aggregateNameToFullName()

            viewmodel.name.notifyObserver()
        }


        binding.fullNameField.addTextChangedListener {
            it?.let {
                binding.next.isEnabled = it.toString().trim().isNotEmpty()
                viewmodel.name.value?.segregateFullName()
            }
        }
        binding.firstName.addTextChangedListener {
            it?.let {
                binding.next.isEnabled = it.toString().trim().isNotEmpty()
                viewmodel.name.value?.aggregateNameToFullName()
            }
        }
        binding.middleName.addTextChangedListener {
            it?.let {
                binding.next.isEnabled = it.toString().trim().isNotEmpty()
                viewmodel.name.value?.aggregateNameToFullName()
            }
        }
        binding.lastName.addTextChangedListener {
            it?.let {
                binding.next.isEnabled = it.toString().trim().isNotEmpty()
                viewmodel.name.value?.aggregateNameToFullName()
            }
        }

        binding.addPhone.setOnClickListener {
            if (phoneNumberAdapter.currentList.none { it.number.trim().isEmpty() }) {
                viewmodel.phoneNumbers.value?.add(PhoneNumber())
                viewmodel.phoneNumbers.notifyObserver()
            }

        }

        binding.addEmail.setOnClickListener {
            if (emailAdapter.currentList.none { it.address.trim().isEmpty() }) {
                viewmodel.emailAddresses.value?.add(EmailAddress())
                viewmodel.emailAddresses.notifyObserver()

            }

        }

/*
        binding.updateIcon.setOnClickListener {
            callGallery()
        }*/


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }



        return binding.root
    }


    override fun onItemClicked(item: PhoneNumber) {
        viewmodel.phoneNumbers.value?.remove(item)
        viewmodel.phoneNumbers.notifyObserver()
    }

    override fun onItemClicked(item: EmailAddress) {
        viewmodel.emailAddresses.value?.remove(item)
        viewmodel.emailAddresses.notifyObserver()
    }

    override fun onItemClicked(item: SocialMediaProfile) {
        viewmodel.goToSocialProfile()
    }


}