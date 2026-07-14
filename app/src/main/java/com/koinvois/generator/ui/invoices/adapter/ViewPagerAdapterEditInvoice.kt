package com.koinvois.generator.ui.invoices.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.koinvois.generator.ui.invoices.add_invoice.sub_fragments.EditInvoiceFragment
import com.koinvois.generator.ui.invoices.add_invoice.sub_fragments.PreviewInvoiceFragment

class ViewPagerAdapterEditInvoice(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> EditInvoiceFragment()
            1 -> PreviewInvoiceFragment()

            else -> EditInvoiceFragment()
        }
    }

}