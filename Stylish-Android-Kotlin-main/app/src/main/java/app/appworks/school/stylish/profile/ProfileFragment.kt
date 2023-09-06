package app.appworks.school.stylish.profile

import android.graphics.Path.Direction
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import app.appworks.school.stylish.MainViewModel
import app.appworks.school.stylish.NavigationDirections
import app.appworks.school.stylish.databinding.FragmentProfileBinding
import app.appworks.school.stylish.ext.getVmFactory
import java.nio.file.DirectoryIteratorException

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class ProfileFragment : Fragment() {

    /**
     * Lazily initialize our [ProfileViewModel].
     */
    private val viewModel by viewModels<ProfileViewModel> { getVmFactory(ProfileFragmentArgs.fromBundle(requireArguments()).userKey) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        init()
        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        viewModel.tracking("view")

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.buttonProfileNotification.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToTinderFragment())
        }
        binding.buttonProfileStarred.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToTinderSuccessFragment())
        }
        binding.buttonCoupon.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToCouponFragment())
        }


        if (viewModel.user.value == null) {
            // user info will be null if user already logged in, and it will get user info from server,
            // here will show you how to set user info to MainViewModel
            val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
            viewModel.user.observe(
                viewLifecycleOwner,
                Observer {
                    if (null != it) {
                        mainViewModel.setupUser(it)
                    }
                }
            )
        }

        return binding.root
    }

//    private fun init() {
//        activity?.let {
//            ViewModelProviders.of(it).get(MainViewModel::class.java).apply {
//                currentFragmentType.value = CurrentFragmentType.PROFILE
//            }
//        }
//    }
}
