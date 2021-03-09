
package com.codedevtech.mycardv2.fragments.dashboard

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.FragmentCardOptionsBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.models.EmailAddress
import com.codedevtech.mycardv2.models.PhoneNumber
import com.codedevtech.mycardv2.viewmodel.CardViewModel
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CardOptionsFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentCardOptionsBinding

    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)
    val cardViewmodel: CardViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

/*    override fun getTheme(): Int {
        return R.style.Theme_MyCardStyles_Options
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //setStyle(STYLE_NORMAL, R.style.ShapeAppearance_MyCardStyles_ExtraLargeComponent);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCardOptionsBinding.inflate(layoutInflater, container, false)

        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        //binding.editNote.isVisible = CardOptionsFragmentArgs.fromBundle(requireArguments()).isEdit

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        cardViewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.delete.setOnClickListener {
            dismiss()
            viewmodel.confirmCardDeletion()
        }

        binding.edit.setOnClickListener {
            dismiss()
            viewmodel.editCard()
        }

        binding.export.setOnClickListener {
            dismiss()
            val intent = Intent(Intent.ACTION_INSERT_OR_EDIT).apply {
                type = ContactsContract.Contacts.CONTENT_ITEM_TYPE
                val card = viewmodel.selectedCard.value

                val data= ArrayList<ContentValues>()

                //primary phone
                card?.phoneNumbers?.filter { it.type== PhoneNumber.PhoneNumberType.Mobile || it.type== PhoneNumber.PhoneNumberType.Home}?.let {
                    for(item in it) {
                        val row = ContentValues()
                        row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        row.put(ContactsContract.CommonDataKinds.Phone.NUMBER, item.number)
                        if(item.type== PhoneNumber.PhoneNumberType.Mobile )
                            row.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        else if(item.type== PhoneNumber.PhoneNumberType.Home)
                            row.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)

                        data.add(row)
                    }
                }

                //secondary phone
                card?.phoneNumbers?.filter { it.type== PhoneNumber.PhoneNumberType.Work}?.let {
                    for(item in it) {
                        val row = ContentValues()
                        row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        row.put(ContactsContract.CommonDataKinds.Phone.NUMBER, item.number)
                        row.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                        data.add(row)
                    }
                }
                //tertiary phone
                card?.phoneNumbers?.filter { it.type== PhoneNumber.PhoneNumberType.Other}?.let {
                    for(item in it) {
                        val row = ContentValues()
                        row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        row.put(ContactsContract.CommonDataKinds.Phone.NUMBER, item.number)
                        row.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_OTHER)
                        data.add(row)
                    }
                }

                //primary email
                card?.emailAddresses?.filter { it.type== EmailAddress.EmailType.Personal}?.let {
                    for(item in it) {
                        val row = ContentValues()
                        row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        row.put(ContactsContract.CommonDataKinds.Email.ADDRESS, item.address)
                        row.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                        data.add(row)
                    }
                }
                //secondary email
                card?.emailAddresses?.filter { it.type== EmailAddress.EmailType.Work}?.let {
                    for(item in it) {
                        val row = ContentValues()
                        row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        row.put(ContactsContract.CommonDataKinds.Email.ADDRESS, item.address)
                        row.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        data.add(row)
                    }
                }
                //tertiary email
                card?.emailAddresses?.filter { it.type== EmailAddress.EmailType.Other}?.let {
                    for(item in it) {
                        val row = ContentValues()
                        row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        row.put(ContactsContract.CommonDataKinds.Email.ADDRESS, item.address)
                        row.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_OTHER)
                        data.add(row)
                    }
                }

                /*card?.phoneNumbers?.let {
                    for(item in it) {

                        when (item.type) {
                            PhoneNumber.PhoneNumberType.Mobile -> {
                                putExtra(ContactsContract.Intents.Insert.PHONE, item.number)

                                putExtra(
                                    ContactsContract.Intents.Insert.PHONE_TYPE,
                                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                                )
                            }
                            PhoneNumber.PhoneNumberType.Home -> {
                                putExtra(
                                    ContactsContract.Intents.Insert.SECONDARY_PHONE,
                                    item.number
                                )

                                putExtra(
                                    ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE,
                                    ContactsContract.CommonDataKinds.Phone.TYPE_HOME,
                                )
                            }
                            PhoneNumber.PhoneNumberType.Work -> {
                                putExtra(
                                    ContactsContract.Intents.Insert.TERTIARY_PHONE,
                                    item.number
                                )

                                putExtra(
                                    ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE,
                                    ContactsContract.CommonDataKinds.Phone.TYPE_WORK,
                                )
                            }
                            PhoneNumber.PhoneNumberType.Other -> {
                                putExtra(
                                    ContactsContract.Intents.Insert.TERTIARY_PHONE,
                                    item.number
                                )

                                putExtra(
                                    ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE,
                                    ContactsContract.CommonDataKinds.Phone.TYPE_OTHER,
                                )
                            }
                        }

                    }
                }
                card?.emailAddresses?.let {
                    for(item in it) {

                        when (item.type) {
                            EmailAddress.EmailType.Personal -> {
                                putExtra(ContactsContract.Intents.Insert.EMAIL, item.address)

                                putExtra(
                                    ContactsContract.Intents.Insert.EMAIL_TYPE,
                                    ContactsContract.CommonDataKinds.Email.TYPE_HOME,
                                )
                            }
                            EmailAddress.EmailType.Work -> {
                                putExtra(
                                    ContactsContract.Intents.Insert.SECONDARY_EMAIL,
                                    item.address
                                )

                                putExtra(
                                    ContactsContract.Intents.Insert.SECONDARY_EMAIL_TYPE,
                                    ContactsContract.CommonDataKinds.Email.TYPE_WORK,
                                )
                            }
                            EmailAddress.EmailType.Other -> {
                                putExtra(
                                    ContactsContract.Intents.Insert.TERTIARY_EMAIL,
                                    item.address
                                )


                                putExtra(
                                    ContactsContract.Intents.Insert.TERTIARY_EMAIL_TYPE,
                                    ContactsContract.CommonDataKinds.Email.TYPE_OTHER,
                                )
                            }
                        }
                    }
                }*/


                card?.name?.let {
                    putExtra(ContactsContract.Intents.Insert.NAME, it.fullName)


                    val nameRow = ContentValues()
                    nameRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    nameRow.put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, it.lastName)
                    data.add(nameRow)

                    nameRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    nameRow.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, it.firstName)
                    data.add(nameRow)

                    nameRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    nameRow.put(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME, it.middleName)
                    data.add(nameRow)

                    nameRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    nameRow.put(ContactsContract.CommonDataKinds.StructuredName.PREFIX, it.prefix)
                    data.add(nameRow)

                    nameRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    nameRow.put(ContactsContract.CommonDataKinds.StructuredName.SUFFIX, it.suffix)
                    data.add(nameRow)

                }

                putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data)

                card?.businessInfo?.let {
                    putExtra(ContactsContract.Intents.Insert.COMPANY, it.companyName)
                    putExtra(ContactsContract.Intents.Insert.JOB_TITLE, it.role)
                }

                card?.note?.let {
                    putExtra(ContactsContract.Intents.Insert.NOTES, it)
                }

                putExtra("finishActivityOnSaveCompleted", true)
            }

            startActivity(intent)
        }

        binding.view.setOnClickListener {
            dismiss()
            viewmodel.showCardQr()
        }
        return binding.root
    }

}