
package com.codedevtech.mycardv2.fragments.dashboard

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.rv.CardPagingAdapter
import com.codedevtech.mycardv2.databinding.*
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.listeners.ItemViewInteraction
import com.codedevtech.mycardv2.models.AddedCard
import com.codedevtech.mycardv2.utils.Utils
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.codedevtech.mycardv2.viewmodel.SearchCardViewModel
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "SearchCardsFragment"
@AndroidEntryPoint
class SearchCardsFragment : Fragment(), ItemViewInteraction<AddedCard?> {

    lateinit var binding:FragmentSearchCardsBinding

    val viewmodel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

    val searchCardViewModel: SearchCardViewModel by viewModels()

    val pagedAdapter= CardPagingAdapter(this)




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(MaterialColors.getColor(requireView(),R.attr.colorSurface))
        }

/*        viewLifecycleOwner.lifecycleScope.launch {
            searchCardViewModel.pagedAddedCardsFlow.collectLatest {
                pagedAdapter.submitData(it)
            }
        }*/


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchCardsBinding.inflate(layoutInflater,container, false)

        binding.viewmodel = viewmodel
        binding.searchviewmodel = searchCardViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.cardList.adapter = pagedAdapter
        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.filters.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                binding.all.id -> searchCardViewModel.searchMode.value = Utils.FILTER_ALL
                binding.name.id -> searchCardViewModel.searchMode.value = Utils.FILTER_NAME
                binding.role.id -> searchCardViewModel.searchMode.value = Utils.FILTER_ROLE
                binding.company.id -> searchCardViewModel.searchMode.value = Utils.FILTER_COMPANY
            }
        }

        binding.searchLayout.setEndIconOnClickListener {
            binding.search.text?.clear()
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        searchCardViewModel.pagedAddedCardsFlow.observe(viewLifecycleOwner) {
            Log.d(TAG, "onCreateView: $it")

            pagedAdapter.submitData(viewLifecycleOwner.lifecycle,it)

        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagedAdapter.loadStateFlow.collectLatest {
                when(it.refresh){
                    is LoadState.Error-> {}
                    is LoadState.NotLoading->{
                        binding.noResults.isVisible = pagedAdapter.itemCount<1

                    }
                    is LoadState.Loading->{}
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
            val directions = SearchCardsFragmentDirections.actionSearchCardsFragmentToViewAddedCardDetailsFragment(it)
            findNavController().navigate(directions, extras)
        }
    }


}