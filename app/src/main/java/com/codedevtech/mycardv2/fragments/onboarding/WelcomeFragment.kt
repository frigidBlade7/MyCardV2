package com.codedevtech.mycardv2.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.FragmentWelcomeBinding
import com.codedevtech.mycardv2.event.Event
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    lateinit var binding : FragmentWelcomeBinding

    val onboardingViewModel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

    override fun onResume() {
        super.onResume()

        if(FirebaseAuth.getInstance().currentUser!=null)
            binding.apply {
                //findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToCardsFragment())
            }
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)


   }
   override fun onCreateView(
       inflater: LayoutInflater, container: ViewGroup?,
       savedInstanceState: Bundle?
   ): View? {
       // Inflate the layout for this fragment

       binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
       binding.viewModel = onboardingViewModel
       binding.lifecycleOwner = viewLifecycleOwner

       onboardingViewModel.destination.observe(viewLifecycleOwner, EventObserver {
           findNavController().navigate(it)
       })
/*
       binding.title.setOnClickListener {
           findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToCardsFragment())
       }*/


      return binding.root
  }

}