package com.codedevtech.mycardv2.fragments.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.CardsFragmentBinding
import com.codedevtech.mycardv2.databinding.FragmentSetupAccountOnboardingBinding
import com.codedevtech.mycardv2.databinding.FragmentSignUpBinding
import com.codedevtech.mycardv2.databinding.FragmentSkipOnboardingBinding
import com.codedevtech.mycardv2.event.Event
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.viewmodel.DashboardViewModel
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel


class CardsFragment : Fragment() {

    lateinit var binding: CardsFragmentBinding

    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.dashboard_nav)
    override fun onResume() {
        super.onResume()
        binding.addCard.show()
    }

    override fun onPause() {
        super.onPause()
        binding.addCard.hide()
        Log.d("TAG", "onPause: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = CardsFragmentBinding.inflate(layoutInflater,container, false)

        binding.addCard.setOnClickListener {
            viewmodel.goToAddCard()
        }

        binding.filter.setOnClickListener {
            viewmodel.goToSearch()
        }

        binding.search.setEndIconOnClickListener {
            viewmodel.showFilter()
        }

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            //if(it.actionId == R.id.action_dashboardFragment_to_add_card_nav)
                requireParentFragment().requireParentFragment().findNavController().navigate(it)

        })


        return binding.root
    }


}