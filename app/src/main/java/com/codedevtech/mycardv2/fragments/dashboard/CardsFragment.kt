package com.codedevtech.mycardv2.fragments.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.rv.AddedListCardAdapter
import com.codedevtech.mycardv2.adapter.rv.PersonalCardListAdapter
import com.codedevtech.mycardv2.databinding.CardsFragmentBinding
import com.codedevtech.mycardv2.di.AppModule.dataStore
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.listeners.ItemViewInteraction
import com.codedevtech.mycardv2.models.AddedCard
import com.codedevtech.mycardv2.models.LiveCard
import com.codedevtech.mycardv2.models.Resource
import com.codedevtech.mycardv2.utils.Utils
import com.codedevtech.mycardv2.viewmodel.CardViewModel
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val TAG = "CardsFragment"

@AndroidEntryPoint
class CardsFragment : Fragment(), ItemViewInteraction<AddedCard?> {

    lateinit var binding: CardsFragmentBinding
    val cardAdapter = AddedListCardAdapter(this)

    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

    val cardViewmodel: CardViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)
/*
    private val closeCardOnBackPressed = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            requireActivity().finish()
        }
    }*/


    override fun onPause() {
        super.onPause()
        //binding.addCard.hide()
        Log.d("TAG", "onPause: ")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
/*
            val mainActivity = requireActivity() as MainActivity
            mainActivity.binding.bottomNav.show()*/
        }

        binding.list.adapter = cardAdapter

/*
        viewLifecycleOwner.lifecycleScope.launch {
            pagedAdapter.loadStateFlow.collectLatest {
                binding.progressBar.isVisible = it.refresh is LoadState.Loading
                binding.group.isVisible = it.refresh is LoadState.Error
            }
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = CardsFragmentBinding.inflate(layoutInflater,container, false)


        enterTransition = MaterialFadeThrough()

/*        binding.addCard.setOnClickListener {
            viewmodel.goToAddCard()
        }*/

        binding.search.setOnClickListener {

            exitTransition = MaterialElevationScale(false)
            reenterTransition = MaterialElevationScale(true)

            val extras = FragmentNavigatorExtras(binding.search to "search")

            findNavController().navigate(CardsFragmentDirections.actionCardsFragmentToSearchCardsFragment(),extras)
            //viewmodel.goToSearch()
        }

        binding.sort.setOnClickListener {
            viewmodel.showFilter()
        }

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            //if(it.actionId == R.id.action_dashboardFragment_to_add_card_nav)
                findNavController().navigate(it)

        })



        cardViewmodel.cardsLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success->{
                    binding.empty.isVisible = it.data.isEmpty()
                    cardAdapter.submitList(it.data)
                    cardViewmodel.save(it.data)
                }
                is Resource.Error->{
                    Toast.makeText(context,it.errorCode,Toast.LENGTH_SHORT).show()
                    //tell user
                }
            }

        }

/*        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            cardViewmodel.cardsLiveData.collect {
                it?.awaitContinuous()?.collect {
                    Log.d(TAG, "onCreateView: ${it?.toObjects(LiveCard::class.java)}")
                    //pagedAdapter.sub
                }
            }
        }*/
/*        binding.apply {

            val navController = Navigation.findNavController(requireActivity(), R.id.fragment)
            bottomNav.setupWithNavController(navController)

            lifecycleScope.launchWhenResumed {
                findNavController().addOnDestinationChangedListener { _, destination, _ ->
                    when (destination.id) {
                        R.id.cardsFragment, R.id.meFragment -> bottomNav.show()
                        else -> binding.bottomNav.hide()
                    }
                }
            }
        }*/


        return binding.root
    }

    override fun onItemClicked(item: AddedCard?, view: View, position: Int) {

        item?.position = position
        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)

        viewmodel.selectedCard.value = item

        item?.let {

            val cardDetailTransitionName = it.id
            val extras = FragmentNavigatorExtras(view to cardDetailTransitionName)
            val directions = CardsFragmentDirections.actionCardsFragmentToViewCardDetailsFragment(it)
            findNavController().navigate(directions, extras)
        }

    }


/*    suspend fun addSessionUrl() {
        context?.dataStore?.edit { settings ->
            //val currentCounterValue = settings[EXAMPLE_COUNTER] ?: 0
            //settings[EXAMPLE_COUNTER] = currentCounterValue + 1
        }
    }*/
}