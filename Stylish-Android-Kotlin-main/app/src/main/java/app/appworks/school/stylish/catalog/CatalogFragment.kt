package app.appworks.school.stylish.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.appworks.school.stylish.databinding.FragmentCatalogBinding
import app.appworks.school.stylish.ext.getVmFactory
import app.appworks.school.stylish.tinder.TinderViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class CatalogFragment : Fragment() {
    var type = 0
    private val viewModel by viewModels<CatalogViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentCatalogBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            with(viewpagerCatalog) {
                type = this.currentItem
                when(type){
                    0 -> viewModel.tracking("view","product_category_women")
                    1 -> viewModel.tracking("view","product_category_men")
                    2 -> viewModel.tracking("view","product_category_accessories")
                }
                val adapter = CatalogAdapter(childFragmentManager, lifecycle)
                this.adapter = adapter
                TabLayoutMediator(tabsCatalog, this) { tab, position ->
                    tab.text = adapter.getPageTitle(position)
                }.attach()
            }
            return@onCreateView root
        }
    }
}
