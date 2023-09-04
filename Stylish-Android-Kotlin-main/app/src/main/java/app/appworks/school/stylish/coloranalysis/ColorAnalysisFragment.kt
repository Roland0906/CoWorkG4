package app.appworks.school.stylish.coloranalysis

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import app.appworks.school.stylish.data.Color
import app.appworks.school.stylish.databinding.FragmentColorAnalysisBinding
import app.appworks.school.stylish.ext.getVmFactory
import app.appworks.school.stylish.login.UserManager

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
        binding.recyclerYourSkinColorSelector.adapter = ColorAnalysisAdapter2(viewModel)
        binding.recyclerRecommendColor.adapter = ColorAnalysisAdapter3(viewModel)

        // test color
        val darkRed = Color("darkRed", "902B0B")
        val orange = Color("orange", "F29A68")
        val bestColors = listOf(darkRed, orange)


        Log.i("API Testing1", UserManager.cid)
        Log.i("API Testing3", UserManager.getSimpleDate())
        Log.i("API Testing4", UserManager.getTimeStamp().toString())
        Log.i("API Testing7", viewModel.colorsInString.toString())
        Log.i("API Testing8", viewModel.jsonFormattedString!!)


        binding.buttonSeeResult.setOnClickListener {

            // send api to data
            val resultColor = viewModel.postUserHairSkin().data?.recommendColor?.let { it1 ->
                Color("the color", it1)}
            val colorResult = listOf(resultColor)
            // get color from data to update vm's bestColorFromApi
            viewModel.getBestColor(colorResult)


            binding.result.visibility = View.VISIBLE
            binding.recyclerRecommendColor.visibility = View.VISIBLE
            binding.textCaSelectSize.visibility = View.VISIBLE
            binding.recyclerCaSizeSelector.visibility = View.VISIBLE
        }




        return binding.root
    }


}