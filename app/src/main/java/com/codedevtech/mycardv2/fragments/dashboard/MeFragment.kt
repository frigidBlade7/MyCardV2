package com.codedevtech.mycardv2.fragments.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.databinding.adapters.ImageViewBindingAdapter
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.adapter.AddedCardAdapter
import com.codedevtech.mycardv2.adapter.PersonalCardAdapter
import com.codedevtech.mycardv2.adapter.PersonalCardSpecialAdapter
import com.codedevtech.mycardv2.adapter.rv.ExtraEmailAddressAdapter
import com.codedevtech.mycardv2.adapter.rv.ExtraPhoneNumbersAdapter
import com.codedevtech.mycardv2.adapter.rv.SocialAdapter
import com.codedevtech.mycardv2.databinding.FragmentSetupAccountOnboardingBinding
import com.codedevtech.mycardv2.databinding.FragmentSignUpBinding
import com.codedevtech.mycardv2.databinding.FragmentSkipOnboardingBinding
import com.codedevtech.mycardv2.databinding.MeFragmentBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.fragments.onboarding.ViewPersonalCardDetailsFragmentDirections
import com.codedevtech.mycardv2.listeners.SocialItemInteraction
import com.codedevtech.mycardv2.models.Resource
import com.codedevtech.mycardv2.models.SocialMediaProfile
import com.codedevtech.mycardv2.utils.backgroundColor
import com.codedevtech.mycardv2.viewmodel.CardViewModel
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeFragment : Fragment(), SocialItemInteraction {

    lateinit var binding: MeFragmentBinding
    val onboardingViewModel: OnboardingViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)
    val cardViewModel: CardViewModel by hiltNavGraphViewModels(R.id.onboarding_nav)

    val cardAdapter = PersonalCardSpecialAdapter()
    val socialAdapter= SocialAdapter(this)
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


        cardViewModel.selectedPersonalCard.observe(viewLifecycleOwner){
            if(it.phoneNumbers.size>1)
                extraPhoneNumbersAdapter.submitList(it.phoneNumbers.drop(1))
            if(it.emailAddresses.size>1)
                extraEmailAddressAdapter.submitList(it.emailAddresses.drop(1))
            socialAdapter.submitList(it.socialMediaProfiles)

        }

        onboardingViewModel.getUser().observe(viewLifecycleOwner){
            when(it){
                is Resource.Success->{
                    binding.displayName.text = it.data.name
                    Glide.with(this).asBitmap().load(it.data.profileUrl).transform(
                        CenterCrop(),
                        CircleCrop()
                    ).thumbnail(0.1f).into(binding.profileIcon)
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


            findNavController().navigate(MeFragmentDirections.actionMeFragmentToViewPersonalCardDetailsFragment())
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