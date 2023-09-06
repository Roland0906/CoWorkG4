package app.appworks.school.stylish.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope

import androidx.navigation.fragment.findNavController
import app.appworks.school.stylish.NavigationDirections
import app.appworks.school.stylish.databinding.FragmentHomeBinding
import app.appworks.school.stylish.ext.getVmFactory
import app.appworks.school.stylish.login.UserManager
import app.appworks.school.stylish.network.StylishApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class HomeFragment : Fragment() {

    /**
     * Lazily initialize our [HomeViewModel].
     */
    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }
    lateinit var binding: FragmentHomeBinding
    private var apiJob: Job? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        init()
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        // post tracker api when user gets into Home page

        viewModel.tracking("view", "hots")

        binding.marquee.text = ""
        binding.marquee.start()
        binding.marquee.textSize = 50f
        binding.marquee.isResetLocation = false

        viewModel.resultText.observe(viewLifecycleOwner){
            binding.marquee.text = it
            binding.marquee.start()
            binding.marquee.textSize = 50f
            binding.marquee.isResetLocation = false
        }

        binding.recyclerHome.adapter = HomeAdapter(
            HomeAdapter.OnClickListener {
                viewModel.tracking("click", "tinder_product_image")
                viewModel.navigateToDetail(it)
            }
        )

        binding.layoutSwipeRefreshHome.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.refreshStatus.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    binding.layoutSwipeRefreshHome.isRefreshing = it
                }
            }
        )

        viewModel.navigateToDetail.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    findNavController().navigate(NavigationDirections.navigateToDetailFragment(it))
                    viewModel.onDetailNavigated()
                }
            }
        )

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        viewModel.startApiCalls()
    }

    override fun onPause(){
        super.onPause()
        viewModel.stopApiCalls()
        binding.marquee.stop()
    }



//    private fun init() {
//        activity?.let {
//            ViewModelProviders.of(it).get(MainViewModel::class.java).apply {
//                currentFragmentType.value = CurrentFragmentType.HOME
//            }
//        }
//    }
}
