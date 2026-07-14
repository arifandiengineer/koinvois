package com.koinvois.generator.ui.estimates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.koinvois.generator.R
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.database.models.Estimate
import com.koinvois.generator.databinding.EstimatesMainFragmentBinding
import com.koinvois.generator.ui.estimates.add_estimate.AddEstimateMainActivity
import com.koinvois.generator.ui.estimates.adapter.ViewPagerAdapterEstimates
import com.koinvois.generator.utilities.enums.EstimateStatusEnum
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class EstimatesMainFragment : Fragment() {

    @Inject lateinit var appPreferences: AppPreferencesDataStore

    private var _binding: EstimatesMainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EstimatesMainViewModel by hiltNavGraphViewModels(R.id.estimate_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EstimatesMainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()
        observeData()
        setClickListeners()
    }

    private fun observeData() {
        viewModel.allEstimatesLive.observe(viewLifecycleOwner) { estimates ->
            updateSummary(estimates)
            updateTabLabels(estimates)
        }
    }

    private fun updateSummary(estimates: List<Estimate>) {
        val total = estimates.size
        val open = estimates.count { it.estimateStatus == EstimateStatusEnum.OPEN.status }
        val closed = estimates.count { it.estimateStatus == EstimateStatusEnum.CLOSED.status }

        binding.txtTotalCount.text = total.toString()
        binding.txtOpenCount.text = open.toString()
        binding.txtClosedCount.text = closed.toString()
    }

    private fun updateTabLabels(estimates: List<Estimate>) {
        val total = estimates.size
        val open = estimates.count { it.estimateStatus == EstimateStatusEnum.OPEN.status }
        val closed = estimates.count { it.estimateStatus == EstimateStatusEnum.CLOSED.status }

        binding.tabLayout.getTabAt(0)?.text = "All ($total)"
        binding.tabLayout.getTabAt(1)?.text = "Open ($open)"
        binding.tabLayout.getTabAt(2)?.text = "Closed ($closed)"
    }

    private fun setClickListeners() {
        binding.btnAddEstimate.setSafeOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                val tsLong = System.currentTimeMillis()
                val estimateNumber = appPreferences.getEstimateNumber().first()
                
                viewModel.estimatePrimaryId = tsLong.div(1000)
                viewModel.estimateNumber = estimateNumber
                viewModel.estimateStatus = EstimateStatusEnum.OPEN.status

                withContext(Dispatchers.IO) {
                    viewModel.insertEstimate(
                        Estimate(
                            (tsLong.div(1000)).toInt(),
                            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                            EstimateStatusEnum.OPEN.status,
                            null, null, null, null, null, null, null, null, null, null,
                        )
                    )
                }
                startActivity(AddEstimateMainActivity.newIntent(requireContext()))
            }
        }
    }

    private fun setUpViewPager() {
        binding.viewPager.adapter = ViewPagerAdapterEstimates(this)
        binding.viewPager.visible()
        binding.tabLayout.visible()

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "All"
                1 -> "Open"
                2 -> "Closed"
                else -> "All"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
