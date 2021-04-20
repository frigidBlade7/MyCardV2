
package com.spaceandjonin.mycrd.fragments.scan

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.ScanNavDirections
import com.spaceandjonin.mycrd.databinding.FragmentConfirmTextRecognitionBinding
import com.spaceandjonin.mycrd.event.Event
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.models.Resource
import com.spaceandjonin.mycrd.utils.TextGraphic
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ConfirmTextRec"

@AndroidEntryPoint
class ConfirmTextRecognitionFragment : Fragment() {

    lateinit var binding: FragmentConfirmTextRecognitionBinding

    val viewModel : OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConfirmTextRecognitionBinding.inflate(layoutInflater, container, false)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.elementListLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                binding.graphicOverlay.clear()
                for (element in it) {
                    binding.graphicOverlay.add(TextGraphic(binding.graphicOverlay, element))

                }
            }
        })
        val uri = Uri.parse(ConfirmTextRecognitionFragmentArgs.fromBundle(requireArguments()).uriString)
        binding.imageView.setImageURI(uri)

        viewModel.processPhysicalCard(binding.imageView.drawable.toBitmap())


        Glide.with(this).asBitmap().load(uri).error(
            R.drawable
                .user_default
        ).transform(
            CenterCrop(),
            RoundedCorners(8)
        ).thumbnail(0.1f).into(binding.preview)
        viewModel.destination.observe(viewLifecycleOwner, EventObserver {
            if (it.actionId == 0)
                findNavController().popBackStack()
            else
                findNavController().navigate(it)
        })

        binding.confirmButton.setOnClickListener {
            if(FirebaseAuth.getInstance().currentUser==null)
                viewModel._destination.postValue(Event(ScanNavDirections.actionGlobalAddPersonalCardNav()))
            else
                findNavController().navigate(ScanNavDirections.actionGlobalAddCardNav())

        }


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }


}