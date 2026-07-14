package com.koinvois.generator.ui.invoices.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.koinvois.generator.ui.invoices.sub_fragments.AllInvoices
import com.koinvois.generator.ui.invoices.sub_fragments.OutstandingInvoices
import com.koinvois.generator.ui.invoices.sub_fragments.PaidInvoices

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> AllInvoices()
            1 -> OutstandingInvoices()
            2 -> PaidInvoices()

            else -> AllInvoices()
        }
    }
}