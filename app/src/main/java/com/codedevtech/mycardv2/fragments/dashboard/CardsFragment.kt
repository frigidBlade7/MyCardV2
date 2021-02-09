package com.codedevtech.mycardv2.fragments.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.rv.CardPagingAdapter
import com.codedevtech.mycardv2.databinding.CardsFragmentBinding
import com.codedevtech.mycardv2.databinding.FragmentSetupAccountOnboardingBinding
import com.codedevtech.mycardv2.databinding.FragmentSignUpBinding
import com.codedevtech.mycardv2.databinding.FragmentSkipOnboardingBinding
import com.codedevtech.mycardv2.event.Event
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.listeners.ItemInteraction
import com.codedevtech.mycardv2.listeners.ItemViewInteraction
import com.codedevtech.mycardv2.models.Card
import com.codedevtech.mycardv2.models.Resource
import com.codedevtech.mycardv2.viewmodel.CardViewModel
import com.codedevtech.mycardv2.viewmodel.DashboardViewModel
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "CardsFragment"

@AndroidEntryPoint
class CardsFragment : Fragment(), ItemViewInteraction<Card?> {

    lateinit var binding: CardsFragmentBinding
    val pagedAdapter= CardPagingAdapter(this)

    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.dashboard_nav)

    val cardViewmodel: CardViewModel by hiltNavGraphViewModels(R.id.dashboard_nav)

/*
    private val closeCardOnBackPressed = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            findNavController().navigateUp()
        }
    }*/

    override fun onResume() {
        super.onResume()

        binding.addCard.show()
    }

    override fun onPause() {
        super.onPause()
        binding.addCard.hide()
        Log.d("TAG", "onPause: ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requireActivity().onBackPressedDispatcher.addCallback(this, closeCardOnBackPressed)

        enterTransition = MaterialFadeThrough()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: ${findNavController().currentDestination} ${findNavController().currentDestination?.id} ${findNavController().previousBackStackEntry}")
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        binding.list.adapter = pagedAdapter


        viewLifecycleOwner.lifecycleScope.launch {
            pagedAdapter.loadStateFlow.collectLatest {
                binding.progressBar.isVisible = it.refresh is LoadState.Loading
                binding.group.isVisible = it.refresh is LoadState.Error
            }
        }
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


        viewLifecycleOwner.lifecycleScope.launch {
            cardViewmodel.cardsLiveDataPaged.collectLatest { data->
                Log.d(TAG, "onCreateView: $data")
                pagedAdapter.submitData(data)
            }
        }


        return binding.root
    }

    override fun onItemClicked(item: Card?, view: View) {

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


}