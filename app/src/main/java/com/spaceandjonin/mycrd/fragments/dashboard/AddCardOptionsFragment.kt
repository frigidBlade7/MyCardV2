
package com.spaceandjonin.mycrd.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentAddCardOptionsBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.utils.Utils
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddCardOptionsFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentAddCardOptionsBinding

    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

/*    override fun getTheme(): Int {
        return R.style.Theme_MyCardStyles_Options
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewmodel.storeTempCardByteArray()

        //setStyle(STYLE_NORMAL, R.style.ShapeAppearance_MyCardStyles_ExtraLargeComponent);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddCardOptionsBinding.inflate(layoutInflater, container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        //binding.editNote.isVisible = CardOptionsFragmentArgs.fromBundle(requireArguments()).isEdit

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.scanCard.setOnClickListener {
            dismiss()
            viewmodel.goToScanCard(Utils.SCAN_TYPE_ADDED)
        }

        binding.enterManually.setOnClickListener {
            dismiss()
            viewmodel.goToAddCard()
        }

        return binding.root
    }

    override fun onDestroy() {
        viewmodel.tempCardByteArray = null
        super.onDestroy()

    }

}