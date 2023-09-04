package app.appworks.school.stylish.tinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.appworks.school.stylish.NavigationDirections
import app.appworks.school.stylish.databinding.FragmentTinderSuccessBinding

class TinderSuccessFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTinderSuccessBinding.inflate(inflater, container, false)
        binding.keepBrowsing.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToHomeFragment())
        }
        binding.signupText.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToProfileFragment())
        }
        return binding.root
    }
}