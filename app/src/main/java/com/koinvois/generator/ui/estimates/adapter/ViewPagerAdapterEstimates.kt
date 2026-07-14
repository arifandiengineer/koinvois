package com.koinvois.generator.ui.estimates.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.koinvois.generator.ui.estimates.sub_fragments.AllEstimatesFragment
import com.koinvois.generator.ui.estimates.sub_fragments.ClosedEstimatesFragment
import com.koinvois.generator.ui.estimates.sub_fragments.OpenEstimatesFragment

class ViewPagerAdapterEstimates(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> AllEstimatesFragment()
            1 -> OpenEstimatesFragment()
            2 -> ClosedEstimatesFragment()

            else -> AllEstimatesFragment()
        }
    }
}