package com.spaceandjonin.mycrd.fragments.dashboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.canhub.cropper.CropImage
import com.google.android.material.transition.MaterialSharedAxis
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.adapter.DropDownAdapter
import com.spaceandjonin.mycrd.adapter.rv.EmailAdapter
import com.spaceandjonin.mycrd.adapter.rv.PhoneNumberAdapter
import com.spaceandjonin.mycrd.adapter.rv.SocialAdapter
import com.spaceandjonin.mycrd.databinding.FragmentAddPersonalCardBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.listeners.EmailItemInteraction
import com.spaceandjonin.mycrd.listeners.ItemInteraction
import com.spaceandjonin.mycrd.listeners.SocialItemInteraction
import com.spaceandjonin.mycrd.models.EmailAddress
import com.spaceandjonin.mycrd.models.PhoneNumber
import com.spaceandjonin.mycrd.models.SocialMediaProfile
import com.spaceandjonin.mycrd.utils.Utils
import com.spaceandjonin.mycrd.utils.aggregateNameToFullName
import com.spaceandjonin.mycrd.utils.notifyObserver
import com.spaceandjonin.mycrd.utils.segregateFullName
import com.spaceandjonin.mycrd.viewmodel.AddPersonalCardViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

private const val TAG = "AddPersonalCardFragment"

@AndroidEntryPoint
class AddPersonalCardFragment : Fragment(), ItemInteraction<PhoneNumber>,
    EmailItemInteraction, SocialItemInteraction {

    lateinit var binding: FragmentAddPersonalCardBinding
    lateinit var phoneNumberAdapter: PhoneNumberAdapter
    lateinit var emailAdapter: EmailAdapter
    lateinit var socialAdapter: SocialAdapter

    lateinit var phoneTypes: DropDownAdapter
    lateinit var emailTypes: DropDownAdapter

    val viewmodel: AddPersonalCardViewModel by navGraphViewModels(R.id.add_personal_card_nav) {
        defaultViewModelProviderFactory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isedit = AddPersonalCardFragmentArgs.fromBundle(requireArguments()).isEdit
        viewmodel.isEditFlow.value = isedit
        if (isedit) {
            AddPersonalCardFragmentArgs.fromBundle(requireArguments()).existingCard?.let {
                viewmodel.card.value?.id = it.id
                viewmodel.name.value = it.name.copy()
                viewmodel.businessInfo.value = it.businessInfo.copy()
                viewmodel.phoneNumbers.value = it.phoneNumbers.toMutableList()
                viewmodel.emailAddresses.value = it.emailAddresses.toMutableList()
                viewmodel.card.value?.createdAt = it.createdAt
                viewmodel.card.value?.profilePicUrl = it.profilePicUrl

                //viewmodel.updateProfile(it.profilePicUrl?.toUri())

            }

            AddPersonalCardFragmentArgs.fromBundle(requireArguments()).existingCard?.socialMediaProfiles?.let {
                for (item in it) {
                    viewmodel.socials.value?.set(it.indexOf(item), item)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddPersonalCardBinding.inflate(layoutInflater, container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


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

        //phoneNumberAdapter.submitList(listOf(PhoneNumber()))
        //emailAdapter.submitList(listOf(EmailAddress()))

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
        }
*/


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }



        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // handle result of pick image chooser
        if (requestCode == Utils.REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            //val imageUri: Bitmap? = data?.getParcelableExtra("data")

            val fullPhotoUri: Uri? = data?.data
            CropImage.activity(fullPhotoUri)
                .setAspectRatio(1, 1)
                .start(requireContext(), this)

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                viewmodel.updateProfile(result?.uri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context, result?.error?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

    }

    @AfterPermissionGranted(Utils.REQUEST_PHOTO)
    private fun callGallery() {
        if (EasyPermissions.hasPermissions(requireContext(), Utils.STORAGE_PERMISSION)) {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            if (activity?.packageManager?.let { intent.resolveActivity(it) } != null) {
                startActivityForResult(intent, Utils.REQUEST_IMAGE_GET)
            }
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.require_gallery),
                Utils.REQUEST_PHOTO, Utils.STORAGE_PERMISSION
            )
        }

    }

    @AfterPermissionGranted(Utils.REQUEST_CAMERA)
    private fun takePhoto() {
        if (EasyPermissions.hasPermissions(requireContext(), Utils.CAMERA_PERMISSION)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (activity?.packageManager?.let { intent.resolveActivity(it) } != null) {
                startActivityForResult(intent, Utils.REQUEST_IMAGE_GET)
            }
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.require_gallery),
                Utils.REQUEST_PHOTO, Utils.STORAGE_PERMISSION
            )
        }

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