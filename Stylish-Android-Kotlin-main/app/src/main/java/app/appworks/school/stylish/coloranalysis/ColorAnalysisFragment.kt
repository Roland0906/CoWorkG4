package app.appworks.school.stylish.coloranalysis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import app.appworks.school.stylish.data.Color
import app.appworks.school.stylish.databinding.FragmentColorAnalysisBinding
import app.appworks.school.stylish.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 * Use the [ColorAnalysisFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ColorAnalysisFragment : Fragment() {

    private val viewModel by viewModels <ColorAnalysisViewModel> { getVmFactory(ColorAnalysisFragmentArgs.fromBundle(requireArguments()).productKey)}

    private lateinit var binding: FragmentColorAnalysisBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentColorAnalysisBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerYourHairColorSelector.adapter = ColorAnalysisAdapter(viewModel)
        binding.recyclerYourSkinColorSelector.adapter = ColorAnalysisAdapter(viewModel)
        binding.recyclerRecommendColor.adapter = ColorAnalysisAdapter(viewModel)

        // test color
        val darkRed = Color("darkRed", "902B0B")
        val orange = Color("orange", "F29A68")
        val bestColors = listOf(darkRed, orange)


        binding.buttonSeeResult.setOnClickListener {
            // send api to data
            // get color from data to update vm's bestColorFromApi
//            viewModel.getBestColor(bestColors)
            binding.result.visibility = View.VISIBLE
            binding.recyclerRecommendColor.visibility = View.VISIBLE
            binding.textCaSelectSize.visibility = View.VISIBLE
            binding.recyclerCaSizeSelector.visibility = View.VISIBLE
        }




        return binding.root
    }


}