package com.koinvois.generator.ui.estimates.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.EditEstimateFragment
import com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.PreviewEstimateFragment

class ViewPagerAdapterEditEstimate(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> EditEstimateFragment()
            1 -> PreviewEstimateFragment()

            else -> EditEstimateFragment()
        }
    }

}