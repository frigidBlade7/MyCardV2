package com.spaceandjonin.mycrd.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.spaceandjonin.mycrd.databinding.FragmentVerifyNewNumberBinding
import com.spaceandjonin.mycrd.event.EventObserver
import com.spaceandjonin.mycrd.viewmodel.VerificationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VerifyNewNumberFragment : Fragment() {

    private lateinit var binding: FragmentVerifyNewNumberBinding
    val viewmodel: VerificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel.updateNumberService.setActivity(requireActivity())

        val number = VerifyNewNumberFragmentArgs.fromBundle(requireArguments()).newPhoneNumber
        viewmodel.newPhoneNumber.value = (number)
        viewmodel.sendVerificationCode(number)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVerifyNewNumberBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })


        viewmodel.snackbarInt.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }


}