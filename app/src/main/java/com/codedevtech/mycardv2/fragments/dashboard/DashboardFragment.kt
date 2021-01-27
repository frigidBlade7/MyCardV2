package com.codedevtech.mycardv2.fragments.dashboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.DashboardFragmentBinding
import com.codedevtech.mycardv2.viewmodel.DashboardViewModel

class DashboardFragment : Fragment() {

    val viewModel: DashboardViewModel by navGraphViewModels(R.id.dashboard_nav){
        defaultViewModelProviderFactory
    }

    private lateinit var binding: DashboardFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DashboardFragmentBinding.inflate(layoutInflater,container,false)

        childFragmentManager.findFragmentById(R.id.dashboard_fragment)?.let {
            val nestedNavController = NavHostFragment.findNavController(it)
            val appBarConfiguration = AppBarConfiguration(setOf(R.id.cardsFragment, R.id.meFragment))

            NavigationUI.setupWithNavController(binding.toolbar,nestedNavController,appBarConfiguration)
            NavigationUI.setupWithNavController(binding.bottomNavigationView,nestedNavController)

        }

        return binding.root


    }



}