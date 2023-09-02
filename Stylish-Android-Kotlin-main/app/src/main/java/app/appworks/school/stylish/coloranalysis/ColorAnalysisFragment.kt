package app.appworks.school.stylish.coloranalysis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import app.appworks.school.stylish.R
import app.appworks.school.stylish.databinding.FragmentColorAnalysisBinding
import app.appworks.school.stylish.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 * Use the [ColorAnalysisFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ColorAnalysisFragment : Fragment() {

    private val viewModel by viewModels <ColorAnalysisViewModel> { getVmFactory(ColorAnalysisFragmentArgs.fromBundle(requireArguments()).productKey)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentColorAnalysisBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel







        return binding.root
    }


}