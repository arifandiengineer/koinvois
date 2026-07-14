package com.koinvois.generator.ui.reports

import android.content.Context
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.koinvois.generator.MainActivity
import com.koinvois.generator.R
import com.koinvois.generator.databinding.ReportsMainFragmentBinding
import com.koinvois.generator.ui.reports.adapter.ViewPagerAdapterReport
import com.koinvois.generator.utilities.extensions.inVisible
import com.koinvois.generator.utilities.extensions.visible
import com.google.android.material.tabs.TabLayoutMediator

class ReportsMainFragment : Fragment() {


    private lateinit var viewModel: ReportsMainViewModel
    private var binding: ReportsMainFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ReportsMainFragmentBinding.inflate(inflater, container, false)


        setUpViewPager()

        return binding?.root
    }



    private fun setUpViewPager() {
        val viewPagerAdapter = ViewPagerAdapterReport(this)
        binding?.viewPager?.adapter = viewPagerAdapter
        binding?.viewPager?.visible()
        binding?.tabLayout?.visible()

        binding?.tabLayout?.let {
            binding?.viewPager?.let { it1 ->
                TabLayoutMediator(
                    it,
                    it1
                ) { tab, position ->
                    tab.text = when (position) {
                        0 -> getString(R.string.report_tab_paid)
                        1 -> getString(R.string.report_tab_clients)

                        else -> getString(R.string.report_tab_paid)
                    }
                }.attach()
            }
        }

        val tabCount: Int = binding?.tabLayout?.tabCount ?: 0
        for (i in 0 until tabCount) {
            val tabView: View = (binding?.tabLayout?.getChildAt(0) as ViewGroup).getChildAt(i)
            tabView.requestLayout()
            ViewCompat.setBackground(tabView, activity?.let { setImageButtonStateNew(it) })
            ViewCompat.setPaddingRelative(
                tabView,
                tabView.paddingStart,
                tabView.paddingTop,
                tabView.paddingEnd,
                tabView.paddingBottom
            );
        }


    }

    private fun setImageButtonStateNew(mContext: Context): StateListDrawable {
        val states = StateListDrawable()
        states.addState(
            intArrayOf(android.R.attr.state_selected),
            ContextCompat.getDrawable(mContext, R.drawable.tab_bg_normal_blue)
        )
        states.addState(
            intArrayOf(-android.R.attr.state_selected),
            ContextCompat.getDrawable(mContext, R.drawable.tab_bg_normal)
        )

        return states
    }
}