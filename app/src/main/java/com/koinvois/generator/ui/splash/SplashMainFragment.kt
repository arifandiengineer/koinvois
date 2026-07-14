package com.koinvois.generator.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.viewpager2.widget.ViewPager2
import com.koinvois.generator.R
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.databinding.SplashMainFragmentBinding
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SplashMainFragment : Fragment() {

    @Inject lateinit var appPreferences: AppPreferencesDataStore

    private var binding: SplashMainFragmentBinding? = null
    private var isFirstTime = true
    private var lockMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SplashMainFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            isFirstTime = appPreferences.getIsFirstTime().first() == true
            lockMode = appPreferences.getLockMode().first() == true

            withContext(Dispatchers.Main) {
                if (isFirstTime) {
                    setupOnboarding()
                } else {
                    navigateToNextScreen()
                }
            }
        }
    }

    private fun setupOnboarding() {
        val onboardingItems = listOf(
            OnboardingItem(
                R.string.onboarding_title_1,
                R.string.onboarding_desc_1,
                R.drawable.ic_splash
            ),
            OnboardingItem(
                R.string.onboarding_title_2,
                R.string.onboarding_desc_2,
                R.drawable.ic_splash
            ),
            OnboardingItem(
                R.string.onboarding_title_3,
                R.string.onboarding_desc_3,
                R.drawable.ic_splash
            )
        )

        val adapter = OnboardingAdapter(onboardingItems)
        binding?.viewPagerOnboarding?.adapter = adapter

        setupIndicators(onboardingItems.size)
        setCurrentIndicator(0)

        binding?.viewPagerOnboarding?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                if (position == onboardingItems.size - 1) {
                    binding?.btnNext?.hide()
                    binding?.btnGetStarted?.visible()
                } else {
                    binding?.btnNext?.visible()
                    binding?.btnGetStarted?.hide()
                }
            }
        })

        binding?.btnNext?.setSafeOnClickListener {
            binding?.viewPagerOnboarding?.let {
                it.currentItem = it.currentItem + 1
            }
        }

        binding?.btnSkip?.setSafeOnClickListener {
            navigateToNextScreen()
        }

        binding?.btnGetStarted?.setSafeOnClickListener {
            navigateToNextScreen()
        }
    }

    private fun setupIndicators(count: Int) {
        val indicators = arrayOfNulls<ImageView>(count)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8)
        
        binding?.layoutIndicators?.removeAllViews()
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.apply {
                setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.onboarding_indicator_inactive))
                this.layoutParams = layoutParams
            }
            binding?.layoutIndicators?.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = binding?.layoutIndicators?.childCount ?: 0
        for (i in 0 until childCount) {
            val imageView = binding?.layoutIndicators?.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.onboarding_indicator_active))
            } else {
                imageView.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.onboarding_indicator_inactive))
            }
        }
    }

    private fun navigateToNextScreen() {
        when {
            isFirstTime -> {
                val action = SplashMainFragmentDirections.actionFragmentSplashMainToFragmentAddBusinessSplash()
                findNavController().navigate(action)
            }
            lockMode -> {
                val action = SplashMainFragmentDirections.actionFragmentSplashMainToFragmentEnterPassword()
                findNavController().navigate(action)
            }
            else -> {
                findNavController().navigate(
                    R.id.dashboard_navigation_graph,
                    null,
                    navOptions {
                        popUpTo(R.id.splash_navigation_graph) { inclusive = true }
                        launchSingleTop = true
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
