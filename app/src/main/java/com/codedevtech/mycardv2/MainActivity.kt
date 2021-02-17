package com.codedevtech.mycardv2

import android.content.ComponentCallbacks2
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnAttach
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.codedevtech.mycardv2.databinding.ActivityMainBinding
import com.codedevtech.mycardv2.fragments.dashboard.CardsFragmentDirections
import com.codedevtech.mycardv2.fragments.onboarding.WelcomeFragmentDirections
import com.codedevtech.mycardv2.utils.hide
import com.codedevtech.mycardv2.utils.show
import com.google.android.material.transition.MaterialElevationScale
import com.google.firebase.auth.FirebaseAuth
//import com.scanlibrary.IScanner
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    val currentNavigationFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.fragment)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




       binding.apply {
           navController = Navigation.findNavController(this@MainActivity, R.id.fragment)
           bottomNav.setupWithNavController(navController)

           addCard.setOnClickListener {

               currentNavigationFragment?.apply {
                   exitTransition = MaterialElevationScale(false)
                   reenterTransition = MaterialElevationScale(true)
               }

               navController.navigate(CardsFragmentDirections.actionCardsFragmentToAddCardNav())
           }
           // Hide bottom nav on screens which don't require it
          lifecycleScope.launchWhenResumed {
               navController.addOnDestinationChangedListener { _, destination, _ ->
                   when (destination.id) {
                       R.id.cardsFragment, R.id.meFragment -> {
                           bottomAppBar.visibility = View.VISIBLE
                           bottomAppBar.performShow()
                       }
                       //R.id.welcomeFragment-> bottomAppBar.performHide()
                       else -> bottomAppBar.performHide()
                   }
                   when(destination.id){
                       R.id.cardsFragment -> addCard.show()
                       else -> addCard.hide()
                   }

               }
           }
       }

   }

    override fun onBackPressed() {
        if(navController.currentDestination?.id==R.id.cardsFragment) {
            finish()
            return
        }
        super.onBackPressed()
    }
}