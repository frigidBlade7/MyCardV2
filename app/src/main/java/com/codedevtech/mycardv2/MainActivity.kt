package com.codedevtech.mycardv2

//import com.scanlibrary.IScanner
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.codedevtech.mycardv2.databinding.ActivityMainBinding
import com.codedevtech.mycardv2.fragments.dashboard.CardsFragmentDirections
import com.codedevtech.mycardv2.utils.hideKeyboard
import com.codedevtech.mycardv2.utils.showKeyboard
import com.google.android.material.transition.MaterialElevationScale
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


/*        val deferringInsetsListener = RootViewDeferringInsetsCallback(
            persistentInsetTypes = WindowInsets.Type.systemBars(),
            deferredInsetTypes = WindowInsets.Type.ime()
        )
        // RootViewDeferringInsetsCallback is both an WindowInsetsAnimation.Callback and an
        // OnApplyWindowInsetsListener, so needs to be set as so.
        binding.root.setWindowInsetsAnimationCallback(deferringInsetsListener)
        binding.root.setOnApplyWindowInsetsListener(deferringInsetsListener)

        binding.bottomNav.setWindowInsetsAnimationCallback(
            TranslateDeferringInsetsAnimationCallback(
                view = binding.conversationRecyclerview,
                persistentInsetTypes = WindowInsets.Type.systemBars(),
                deferredInsetTypes = WindowInsets.Type.ime()
            )
        )*/

       binding.apply {
           navController = Navigation.findNavController(this@MainActivity, R.id.fragment)
           bottomNav.setupWithNavController(navController)

           addCard.setOnClickListener {

               when(navController.currentDestination?.id) {
                   R.id.viewPersonalCardDetailsFragment, R.id.viewAddedCardDetailsFragment ->
                   currentNavigationFragment?.apply {
                       exitTransition = MaterialElevationScale(false)
                       reenterTransition = MaterialElevationScale(true)
                   }
               }

               navController.navigate(CardsFragmentDirections.actionCardsFragmentToAddCardNav())
           }
           // Hide bottom nav on screens which don't require it
          lifecycleScope.launchWhenResumed {
               navController.addOnDestinationChangedListener { _, destination, _ ->
                   when (destination.id) {
                       R.id.cardsFragment, R.id.meFragment -> {
                           hideKeyboard(binding.root)
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
                   when(destination.id){
                       R.id.viewAddedCardDetailsFragment,R.id.confirmAddDetailsFragment,R.id.cardDetailsFragment -> hideKeyboard(binding.root)
                   }
                   when(destination.id){
                       R.id.searchCardsFragment-> {
                           showKeyboard(binding.root)
                       }
                   }

               }
           }
       }

   }



    override fun onBackPressed() {
        when(navController.currentDestination?.id){
            R.id.cardsFragment, R.id.welcomeFragment -> {
                finish()
                return
            }
        }
        super.onBackPressed()
    }
}