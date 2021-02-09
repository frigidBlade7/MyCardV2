package com.codedevtech.mycardv2.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.DashboardFragmentBinding
import com.codedevtech.mycardv2.viewmodel.DashboardViewModel
import com.google.android.material.animation.AnimationUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment(), NavController.OnDestinationChangedListener {

    val viewModel: DashboardViewModel by hiltNavGraphViewModels(R.id.dashboard_nav)
    private lateinit var binding: DashboardFragmentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DashboardFragmentBinding.inflate(layoutInflater, container, false)

        childFragmentManager.findFragmentById(R.id.dashboard_fragment)?.let {
            val nestedNavController = NavHostFragment.findNavController(it)
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.cardsFragment,
                    R.id.meFragment
                )
            )

            setupWithNavController(
                binding.toolbar,
                nestedNavController,
                appBarConfiguration
            )

            setupWithNavController(binding.bottomNavigationView, nestedNavController)


            binding.run {
                nestedNavController.addOnDestinationChangedListener(this@DashboardFragment)
            }
        }


        return binding.root


    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {


        when(destination.id){
            R.id.viewCardDetailsFragment -> {
                hideBottomViewAndToolbar()
            }
            R.id.cardsFragment -> {
                showBottomViewAndToolbar()
            }
        }
    }

    private fun hideBottomViewAndToolbar() {
        binding.run {
            //binding.bottomNavigationView.hide

            binding.bottomNavigationView.animate().translationY(bottomNavigationView.height.toFloat())
                .setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR)
                .setDuration(225).start()

        }
    }

    private fun showBottomViewAndToolbar() {
        binding.run {
            //binding.appbar.setExpanded(true,true)

            binding.bottomNavigationView.animate().translationY(0f)
                .setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR)
                .setDuration(175).start()


        }
    }




}