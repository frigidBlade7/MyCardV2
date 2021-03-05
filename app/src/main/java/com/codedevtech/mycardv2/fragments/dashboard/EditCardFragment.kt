
package com.codedevtech.mycardv2.fragments.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.util.Util
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.codedevtech.mycardv2.MainActivity
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
import com.codedevtech.mycardv2.utils.Utils
import com.codedevtech.mycardv2.utils.aggregateNameToFullName
import com.codedevtech.mycardv2.utils.notifyObserver
import com.codedevtech.mycardv2.utils.segregateFullName
import com.codedevtech.mycardv2.viewmodel.AddCardViewModel
import com.codedevtech.mycardv2.viewmodel.AddPersonalCardViewModel
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

private const val TAG = "AddPersonalCardFragment"

@AndroidEntryPoint
class EditCardFragment : Fragment(),ItemInteraction<PhoneNumber>,
    EmailItemInteraction, SocialItemInteraction {

    lateinit var binding: FragmentAddCardBinding
    lateinit var phoneNumberAdapter: PhoneNumberAdapter
    lateinit var emailAdapter: EmailAdapter
    lateinit var socialAdapter: SocialAdapter

    lateinit var phoneTypes: DropDownAdapter
    lateinit var emailTypes: DropDownAdapter

    val viewmodel: AddCardViewModel by navGraphViewModels(R.id.add_card_nav){
        defaultViewModelProviderFactory
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = requireActivity() as MainActivity
        enterTransition = MaterialContainerTransform().apply {
            // Manually add the Views to be shared since this is not a standard Fragment to
            // Fragment shared element transition.
            startView = mainActivity.binding.addCard
            endView = binding.layout
/*
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
*/
            scrimColor = Color.TRANSPARENT
            containerColor = MaterialColors.getColor(view,R.attr.colorSurface)
            startContainerColor = MaterialColors.getColor(view,R.attr.colorPrimary)
            endContainerColor = MaterialColors.getColor(view,R.attr.colorSurface)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddCardBinding.inflate(layoutInflater,container, false)

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

        //phoneNumberAdapter.submitList(listOf(PhoneNumber()))
        //emailAdapter.submitList(listOf(EmailAddress()))

        viewmodel.socials.observe(viewLifecycleOwner){ it ->
            socialAdapter.submitList(it.filter { it.usernameOrUrl.isNotEmpty()})
        }

        viewmodel.phoneNumbers.observe(viewLifecycleOwner){ it ->
            phoneNumberAdapter.submitList(it.toMutableList())
        }

        viewmodel.emailAddresses.observe(viewLifecycleOwner){ it ->
            emailAdapter.submitList(it.toMutableList())
        }


        binding.phoneItems.adapter = phoneNumberAdapter
        binding.emailItems.adapter = emailAdapter
        binding.socialItems.adapter = socialAdapter


        binding.fullNameChevron.setOnClickListener {
            viewmodel.isNameExpanded.value = !it.isSelected

            if(it.isSelected)
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
            if(phoneNumberAdapter.currentList.none { it.number.isEmpty()}) {
                viewmodel.phoneNumbers.value?.add(PhoneNumber())
                viewmodel.phoneNumbers.notifyObserver()
            }

        }

        binding.addEmail.setOnClickListener {
            if(emailAdapter.currentList.none { it.address.isEmpty() }) {
                viewmodel.emailAddresses.value?.add(EmailAddress())
                viewmodel.emailAddresses.notifyObserver()

            }

        }


        binding.updateIcon.setOnClickListener {
            callGallery()
        }


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }



        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // handle result of pick image chooser
        if (requestCode == Utils.REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            val imageUri: Bitmap? = data?.getParcelableExtra("data")

            val fullPhotoUri: Uri? = data?.data
            CropImage.activity(fullPhotoUri)
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
            EasyPermissions.requestPermissions(this, getString(R.string.require_gallery),
                Utils.REQUEST_PHOTO, Utils.STORAGE_PERMISSION)
        }

    }

    override fun onItemClicked(item: PhoneNumber) {
        viewmodel.phoneNumbers.value?.add(item)
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