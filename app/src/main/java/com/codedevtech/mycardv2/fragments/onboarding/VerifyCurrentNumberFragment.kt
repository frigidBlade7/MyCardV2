package com.codedevtech.mycardv2.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.FragmentVerifyCurrentNumberBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VerifyCurrentNumberFragment : Fragment() {

    lateinit var binding: FragmentVerifyCurrentNumberBinding
    val viewmodel: SettingsViewModel by hiltNavGraphViewModels(R.id.settings_nav)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.smsCode.value=""
/*
        val number = VerifyNumberFragmentArgs.fromBundle(requireArguments()).number
        viewmodel.sendVerificationCode(number)*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerifyCurrentNumberBinding.inflate(layoutInflater,container, false)
        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        viewmodel.user.value?.phoneNumber?.let {
            viewmodel.sendVerificationCode(it)
        }

        viewmodel.snackbarInt.observe(viewLifecycleOwner,EventObserver{
            Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
        })


        binding.toolbar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }
        return binding.root
    }


}