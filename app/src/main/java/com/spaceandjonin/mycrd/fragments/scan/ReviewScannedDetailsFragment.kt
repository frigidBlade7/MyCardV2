package com.spaceandjonin.mycrd.fragments.scan

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.adapter.rv.*
import com.spaceandjonin.mycrd.databinding.FragmentReviewScannedDetailsBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.listeners.LabelledDetailItemInteraction
import com.spaceandjonin.mycrd.listeners.UnlabelledDetailItemInteraction
import com.spaceandjonin.mycrd.models.*
import com.spaceandjonin.mycrd.utils.Utils
import com.spaceandjonin.mycrd.utils.notifyObserver
import com.spaceandjonin.mycrd.viewmodel.ReviewScannedDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AddPersonalCardFragment"

@AndroidEntryPoint
class ReviewScannedDetailsFragment : Fragment(), UnlabelledDetailItemInteraction,
    LabelledDetailItemInteraction {

    private lateinit var binding: FragmentReviewScannedDetailsBinding
    private lateinit var unlabelledStringsAdapter: UnlabelledStringsAdapter
    private lateinit var labelledDetailAdapter: LabelledDetailAdapter

    private lateinit var extraEmailAddressActionsAdapter: ExtraEmailAddressActionsAdapter
    private lateinit var extraPhoneNumbersActionsAdapter: ExtraPhoneNumbersActionsAdapter

    val viewmodel: ReviewScannedDetailsViewModel by navGraphViewModels(R.id.scan_nav) {
        defaultViewModelProviderFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val details = ReviewScannedDetailsFragmentArgs.fromBundle(requireArguments()).lines
        viewmodel.retrieveScannedDetails(
            details, getString(R.string.phone_number),
            getString(R.string.email_address), getString(R.string.website)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentReviewScannedDetailsBinding.inflate(layoutInflater, container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        unlabelledStringsAdapter = UnlabelledStringsAdapter(this)
        labelledDetailAdapter = LabelledDetailAdapter(this)

        extraEmailAddressActionsAdapter = ExtraEmailAddressActionsAdapter(this)
        extraPhoneNumbersActionsAdapter = ExtraPhoneNumbersActionsAdapter(this)

        viewmodel.unlabelledStrings.observe(viewLifecycleOwner) {
            unlabelledStringsAdapter.submitList(it.toList())
        }

        viewmodel.labelledStrings.observe(viewLifecycleOwner) {
            labelledDetailAdapter.submitList(it.toList())
        }

        binding.note.cardView.setOnClickListener {
            viewmodel.note.value = viewmodel.cardNote.value
            findNavController().navigate(ReviewScannedDetailsFragmentDirections.actionGlobalAddNoteScanFragment())
        }

        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.full_divider
            )!!
        )

        viewmodel.labelledStrings.observe(viewLifecycleOwner) {
            val card = Card()

            it.forEach { labelDetail ->
                when (labelDetail.label) {
                    getString(R.string.full_name) -> card.name.fullName = labelDetail.detail
                    getString(R.string.website) -> card.businessInfo.website = labelDetail.detail
                    getString(R.string.work_location) -> card.businessInfo.companyAddress =
                        labelDetail.detail
                    getString(R.string.company_name) -> card.businessInfo.companyName =
                        labelDetail.detail
                    getString(R.string.role) -> card.businessInfo.role = labelDetail.detail
                }

                when (labelDetail.parentLabel) {
                    getString(R.string.phone_number) -> viewmodel.phoneNumbers.value?.add(
                        PhoneNumber(labelDetail.detail, PhoneNumber.PhoneNumberType.values()
                            .first { labelParam -> labelParam.name == labelDetail.label })
                    )
                    getString(R.string.email_address) -> viewmodel.emailAddresses.value?.add(
                        EmailAddress(labelDetail.detail, EmailAddress.EmailType.values()
                            .first { labelParam -> labelParam.name == labelDetail.label })
                    )
                }
            }

            syncCardData(card)
        }


        binding.unlabelledRv.adapter = unlabelledStringsAdapter
        binding.labelledRv.adapter = labelledDetailAdapter

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_detail -> {
                    viewmodel.addLabelledDetail()
                }
                else -> return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener true

        }


        return binding.root
    }

    private fun syncCardData(card: Card) {

        viewmodel.card.value = card
        viewmodel.updateLists()
        viewmodel.card.notifyObserver()
    }

    override fun onTagClicked(item: String) {
        viewmodel.selectedLabel.value = Utils.UNLABELLED
        viewmodel.selectedDetail.value = item
        findNavController().navigate(R.id.action_reviewScannedDetailsFragment_to_assignLabelsFragment)
        //show fragment
    }

    override fun onCopyClicked(item: String) {
        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText(item, item)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(
            requireContext(),
            getString(R.string.copied_to_clipboard),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onRemoveClicked(item: String) {
        viewmodel.removeDetail(item)
    }

    override fun onItemClicked(item: String) {
        viewmodel.selectedLabel.value = Utils.UNLABELLED
        viewmodel.editDetail(item)
    }

    override fun onRemoveLabelClicked(item: LabelDetail) {
        viewmodel.executeRemoveLabel(item)
    }

    override fun onEditClicked(item: LabelDetail) {
        viewmodel.executeEdit(item)
    }

    override fun onSwapClicked(item: LabelDetail) {
        viewmodel.executeSwap(item)
        //todo include parameter to clear the previous labeldetail item

    }


}