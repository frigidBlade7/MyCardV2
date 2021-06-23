package com.spaceandjonin.mycrd.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.databinding.FragmentSetupAccountOnboardingBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SetUpAccountFragment : Fragment() {

    lateinit var binding: FragmentSetupAccountOnboardingBinding
    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSetupAccountOnboardingBinding.inflate(layoutInflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

/*        binding.useCard.addOnCheckedChangeListener { button, isChecked ->
            //Toast.makeText(context,isChecked.toString(),Toast.LENGTH_SHORT).show()

            if(isChecked){

            }
            else{

            }
        }*/

        binding.skip.setOnClickListener {
            viewmodel.goToSignUp(binding.useCard.isChecked)
        }

        binding.useCard.isChecked = true
        return binding.root
    }


}