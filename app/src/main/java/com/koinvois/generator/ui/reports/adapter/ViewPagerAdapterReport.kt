package com.koinvois.generator.ui.reports.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.koinvois.generator.ui.reports.subfragments.ClientsReportFragment
import com.koinvois.generator.ui.reports.subfragments.PaidReportFragment

class ViewPagerAdapterReport(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> PaidReportFragment()
            1 -> ClientsReportFragment()

            else -> PaidReportFragment()
        }
    }
}