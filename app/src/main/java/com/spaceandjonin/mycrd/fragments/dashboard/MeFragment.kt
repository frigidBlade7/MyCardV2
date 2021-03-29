package com.spaceandjonin.mycrd.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.adapter.PersonalCardSpecialAdapter
import com.spaceandjonin.mycrd.adapter.rv.ExtraEmailAddressAdapter
import com.spaceandjonin.mycrd.adapter.rv.ExtraPhoneNumbersAdapter
import com.spaceandjonin.mycrd.adapter.rv.SocialAdapter
import com.spaceandjonin.mycrd.databinding.MeFragmentBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.listeners.SocialItemInteraction
import com.spaceandjonin.mycrd.models.Resource
import com.spaceandjonin.mycrd.models.SocialMediaProfile
import com.spaceandjonin.mycrd.viewmodel.CardViewModel
import com.spaceandjonin.mycrd.viewmodel.OnboardingViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.spaceandjonin.mycrd.utils.backgroundColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeFragment : Fragment(), SocialItemInteraction {

    lateinit var binding: MeFragmentBinding
    val onboardingViewModel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)
    val cardViewModel: CardViewModel by viewModels()

    val cardAdapter = PersonalCardSpecialAdapter()
    val socialAdapter= SocialAdapter(null)
    val extraEmailAddressAdapter =ExtraEmailAddressAdapter()
    val extraPhoneNumbersAdapter= ExtraPhoneNumbersAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
/*
            val mainActivity = requireActivity() as MainActivity
            mainActivity.binding.bottomNav.show()*/
        }
    }

    override fun onResume() {
        super.onResume()
        cardAdapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = MeFragmentBinding.inflate(layoutInflater,container, false)
        binding.onboardingViewModel = onboardingViewModel
        binding.cardViewModel = cardViewModel

        enterTransition = MaterialFadeThrough()

        binding.include.pager.apply {
            val recyclerView = getChildAt(0) as RecyclerView
            recyclerView.clipToPadding=false
            recyclerView.setPadding(48,recyclerView.paddingTop,48, recyclerView.bottom)

        }


        binding.include.pager.adapter = cardAdapter
        binding.include.email.list.adapter = extraEmailAddressAdapter
        binding.include.phone.list.adapter = extraPhoneNumbersAdapter
        binding.include.socialMedia.socialItems.adapter = socialAdapter
        binding.include.socialMedia.socialItems.setHasFixedSize(true)

        cardViewModel.selectedPersonalCard.observe(viewLifecycleOwner){
            it?.let {
                if (it.phoneNumbers.size > 1)
                    extraPhoneNumbersAdapter.submitList(it.phoneNumbers.drop(1))
                if (it.emailAddresses.size > 1)
                    extraEmailAddressAdapter.submitList(it.emailAddresses.drop(1))
                socialAdapter.submitList(it.socialMediaProfiles.toList())
            }

        }

        onboardingViewModel.getUser().observe(viewLifecycleOwner){
            when(it){
                is Resource.Success->{
                    binding.displayName.text = it.data.name
                    Glide.with(this).asBitmap().load(it.data.profileUrl).transform(
                        CenterCrop(),
                        CircleCrop()
                    ).placeholder(R.drawable.user_default).error(R.drawable
                        .user_default).thumbnail(0.1f).into(binding.profileIcon)
                }
            }
        }

        onboardingViewModel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        TabLayoutMediator(binding.include.tabLayout, binding.include.pager) { tab, position ->
        }.attach()



        binding.include.pager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                binding.include.item = cardAdapter.currentList[position]
                cardViewModel.selectedPersonalCard.value = cardAdapter.currentList[position]

                binding.include.email.chevron.setOnClickListener {
                    it.isSelected = !it.isSelected
                    if(it.isSelected)
                        binding.include.email.list.visibility = View.VISIBLE
                    else
                        binding.include.email.list.visibility = View.GONE
                }

                binding.include.phone.chevron.setOnClickListener {
                    it.isSelected = !it.isSelected

                    if(it.isSelected)
                        binding.include.phone.list.visibility = View.VISIBLE
                    else
                        binding.include.phone.list.visibility = View.GONE
                }

                if(cardAdapter.currentList.isEmpty())
                    binding.include.socialMedia.socialItems.visibility = View.GONE
                else
                    binding.include.socialMedia.socialItems.visibility = View.VISIBLE
            }
        })


        onboardingViewModel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        cardViewModel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })



        cardViewModel.personalCardsLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success->{
                    binding.empty.isVisible = it.data.isEmpty()
                    if(it.data.isEmpty()) {
                        binding.include.parent.isVisible = it.data.isNotEmpty()
                        binding.cardCount.isVisible = false

                        binding.appbar.setBackgroundColor(android.R.color.white)
                    }else {
                        binding.appbar.backgroundColor(R.color.mc_gray_light)
                        binding.cardCount.isVisible = true
                    }
                    cardAdapter.submitList(it.data)
                    binding.cardCount.text = resources.getQuantityString(R.plurals.cards, it.data.size, it.data.size);

                    cardViewModel.savePersonal(it.data)
                }
                is Resource.Error->{
                    Toast.makeText(context,it.errorCode, Toast.LENGTH_SHORT).show()
                    //tell user
                }
            }

        }

        binding.include.editCard.setOnClickListener {
            onboardingViewModel.selectedPersonalCard.value = cardViewModel.selectedPersonalCard.value
            findNavController().navigate(MeFragmentDirections.actionMeFragmentToAddPersonalCardNav(isEdit = true, onboardingViewModel.selectedPersonalCard.value?.copy()))
        }

        binding.include.deleteCard.setOnClickListener {
            onboardingViewModel.selectedPersonalCard.value = cardViewModel.selectedPersonalCard.value
            findNavController().navigate(MeFragmentDirections.actionGlobalDeletePersonalCardDialogFragment())
        }

        binding.toolbar.setOnMenuItemClickListener {

            exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

            //val extras = FragmentNavigatorExtras(binding.toolbar to "search")

            when(it.itemId){
                R.id.add_personal_card -> {
                    //findNavController().navigate(MeFragmentDirections.actionMeFragmentToAddPersonalCardNav(),extras)
                    onboardingViewModel.goToAddPersonalCard()
                }
                R.id.settings -> onboardingViewModel.goToSettings()
            }

            return@setOnMenuItemClickListener true
        }

        return binding.root
    }

    override fun onItemClicked(item: SocialMediaProfile) {
        //TODO("Not yet implemented")
    }


}