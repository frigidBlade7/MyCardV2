
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.databinding.FragmentSetupAccountOnboardingBinding
import com.codedevtech.mycardv2.databinding.FragmentSkipOnboardingBinding
import com.codedevtech.mycardv2.event.EventObserver
import com.codedevtech.mycardv2.viewmodel.OnboardingViewModel


class SetUpAccountFragment : Fragment() {

    lateinit var binding: FragmentSetupAccountOnboardingBinding
    val viewmodel: OnboardingViewModel by navGraphViewModels(R.id.onboarding_nav){
        defaultViewModelProviderFactory
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSetupAccountOnboardingBinding.inflate(layoutInflater,container, false)
        binding.viewModel = viewmodel

        viewmodel.destination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it)
        })

        binding.toolbar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }


        binding.useCard.isChecked = true
        return binding.root
    }


}