package com.spaceandjonin.mycrd.fragments.dashboard

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.adapter.rv.CardPagingAdapter
import com.spaceandjonin.mycrd.databinding.FragmentSearchCardsBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.listeners.ItemViewInteraction
import com.spaceandjonin.mycrd.models.AddedCard
import com.spaceandjonin.mycrd.utils.Utils
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel
import com.spaceandjonin.mycrd.viewmodel.SearchCardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

private const val TAG = "SearchCardsFragment"

@AndroidEntryPoint
class
SearchCardsFragment : Fragment(), ItemViewInteraction<AddedCard?> {

    lateinit var binding: FragmentSearchCardsBinding

    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

    val searchCardViewModel: SearchCardViewModel by viewModels()

    val pagedAdapter = CardPagingAdapter(this)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(MaterialColors.getColor(requireView(), R.attr.colorSurface))
        }

        binding.search.requestFocus()
/*        viewLifecycleOwner.lifecycleScope.launch {
            searchCardViewModel.pagedAddedCardsFlow.collectLatest {
                pagedAdapter.submitData(it)
            }
        }*/


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchCardsBinding.inflate(layoutInflater, container, false)

        binding.viewmodel = viewmodel
        binding.searchviewmodel = searchCardViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.cardList.adapter = pagedAdapter
        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.filters.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.all.id -> searchCardViewModel.searchMode.value = Utils.FILTER_ALL
                binding.name.id -> searchCardViewModel.searchMode.value = Utils.FILTER_NAME
                binding.role.id -> searchCardViewModel.searchMode.value = Utils.FILTER_ROLE
                binding.company.id -> searchCardViewModel.searchMode.value = Utils.FILTER_COMPANY
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        searchCardViewModel.pagedAddedCardsFlow.observe(viewLifecycleOwner) {
            Timber.d( "onCreateView: $it")

            pagedAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagedAdapter.loadStateFlow.collectLatest {
                when (it.refresh) {
                    is LoadState.Error -> {
                        Timber.d("loadstate error")
                    }
                    is LoadState.NotLoading -> {
                        binding.noResults.isVisible = pagedAdapter.itemCount < 1

                    }
                    is LoadState.Loading -> {
                        Timber.d("loadstate loading")
                    }
                }
                //binding.progressBar.isVisible = it.refresh is LoadState.Loading
            }
        }
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
            val directions =
                SearchCardsFragmentDirections.actionSearchCardsFragmentToViewAddedCardDetailsFragment(
                    it
                )
            findNavController().navigate(directions, extras)
        }
    }

    override fun onItemLongClicked(item: AddedCard?, view: View, position: Int) {
        viewmodel.selectedCard.value = item

        val popupMenu = PopupMenu(requireContext(), view, R.menu.card_options)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
                    viewmodel.editCard()
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