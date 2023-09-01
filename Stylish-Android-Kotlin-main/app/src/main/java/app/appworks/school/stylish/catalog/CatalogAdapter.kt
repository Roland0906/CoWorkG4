package app.appworks.school.stylish.catalog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.appworks.school.stylish.catalog.item.CatalogItemFragment

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class CatalogAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return CatalogItemFragment(CatalogTypeFilter.values()[position])
    }

    override fun getItemCount() = CatalogTypeFilter.values().size

    fun getPageTitle(position: Int): CharSequence? {
        return CatalogTypeFilter.values()[position].value
    }
}
