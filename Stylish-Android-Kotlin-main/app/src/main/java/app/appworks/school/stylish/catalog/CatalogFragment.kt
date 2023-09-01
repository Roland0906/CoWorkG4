package app.appworks.school.stylish.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.appworks.school.stylish.databinding.FragmentCatalogBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class CatalogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentCatalogBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            with(viewpagerCatalog) {
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
