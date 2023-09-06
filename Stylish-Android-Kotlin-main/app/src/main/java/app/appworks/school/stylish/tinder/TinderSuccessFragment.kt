package app.appworks.school.stylish.tinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.appworks.school.stylish.NavigationDirections
import app.appworks.school.stylish.databinding.FragmentTinderSuccessBinding
import app.appworks.school.stylish.ext.getVmFactory

class TinderSuccessFragment: Fragment() {
    private val viewModel by viewModels<TinderViewModel> { getVmFactory() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTinderSuccessBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.keepBrowsing.setOnClickListener {
            viewModel.tracking("click","tinder_keep_shopping")
            findNavController().navigate(NavigationDirections.navigateToHomeFragment())
        }
        binding.signupText.setOnClickListener {
            viewModel.tracking("click","tinder_keep_shopping")
            findNavController().navigate(NavigationDirections.navigateToLoginDialog())
        }

        binding.imageView.setImageResource(viewModel.stylePicture.value!!)


        return binding.root
    }
}