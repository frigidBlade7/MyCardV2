package com.spaceandjonin.mycrd.fragments.dashboard

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.adapter.rv.AddedListCardAdapter
import com.spaceandjonin.mycrd.databinding.CardsFragmentBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.listeners.ItemViewInteraction
import com.spaceandjonin.mycrd.models.AddedCard
import com.spaceandjonin.mycrd.models.EmailAddress
import com.spaceandjonin.mycrd.models.PhoneNumber
import com.spaceandjonin.mycrd.models.Resource
import com.spaceandjonin.mycrd.utils.exportContactIntent
import com.spaceandjonin.mycrd.viewmodel.CardViewModel
import com.spaceandjonin.mycrd.viewmodel.FilterViewModel
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "CardsFragment"

@AndroidEntryPoint
class CardsFragment : Fragment(), ItemViewInteraction<AddedCard?> {

    lateinit var binding: CardsFragmentBinding
    val cardAdapter = AddedListCardAdapter(this)

    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

    val filterViewModel: FilterViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

    val cardViewmodel: CardViewModel by viewModels()
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
        binding = CardsFragmentBinding.inflate(layoutInflater, container, false)


        enterTransition = MaterialFadeThrough()

/*        binding.addCard.setOnClickListener {
            viewmodel.goToAddCard()
        }*/

        binding.search.setOnClickListener {

            exitTransition = MaterialElevationScale(false)
            reenterTransition = MaterialElevationScale(true)

            val extras = FragmentNavigatorExtras(binding.search to "search")

            findNavController().navigate(
                CardsFragmentDirections.actionCardsFragmentToSearchCardsFragment(),
                extras
            )
            //viewmodel.goToSearch()
        }

        binding.sort.setOnClickListener {
            viewmodel.showFilter()
        }

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            //if(it.actionId == R.id.action_dashboardFragment_to_add_card_nav)
            findNavController().navigate(it)

        })

        filterViewModel.sortMode.observe(viewLifecycleOwner){
            it?.let {
                cardViewmodel.sortMode.value = it
            }
        }

        cardViewmodel.cardsLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    binding.empty.isVisible = it.data.isEmpty()
                    cardAdapter.submitList(it.data)
                    cardViewmodel.save(it.data)
                }
                is Resource.Error -> {
                    Toast.makeText(context, it.errorCode, Toast.LENGTH_SHORT).show()
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

    override fun onItemLongClicked(item: AddedCard?, view: View, position: Int) {
        viewmodel.selectedCard.value = item

        val popupMenu = PopupMenu(requireContext(), view, GravityCompat.END)
        popupMenu.inflate(R.menu.card_options)
        //val popupHelper = MenuPopupHelper(requireContext(), popupMenu.menu as MenuBuilder)

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.edit -> {
                    viewmodel.editCard()

                }
                R.id.export -> {

                    val card = viewmodel.selectedCard.value
                    startActivity(card?.exportContactIntent(viewmodel.tempCardByteArray))
                }
                R.id.delete -> {
                    viewmodel.confirmCardDeletion()
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }


}