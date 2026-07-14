package com.koinvois.generator.ui.splash

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.LocaleListCompat
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.viewpager2.widget.ViewPager2
import com.koinvois.generator.R
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.databinding.FragmentSplashMainBinding
import com.koinvois.generator.ui.lock.EnterPasswordActivity
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

    private var binding: FragmentSplashMainBinding? = null
    private var isFirstTime = true
    private var lockMode = false
    private var languageChosen = false

    private val enterPasswordLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                navigateToDashboard()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashMainBinding.inflate(inflater, container, false)
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

        // The very first slide is a mandatory language gate: it can't be swiped
        // past or skipped until a language is chosen. Once chosen (here or
        // already chosen on a previous run), swiping and Skip work normally.
        languageChosen = !AppCompatDelegate.getApplicationLocales().isEmpty
        binding?.viewPagerOnboarding?.isUserInputEnabled = languageChosen
        updateFirstSlideGate(position = 0, slideCount = onboardingItems.size)

        binding?.viewPagerOnboarding?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                updateFirstSlideGate(position, onboardingItems.size)
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

        binding?.btnOnboardingEnglish?.setSafeOnClickListener {
            chooseOnboardingLanguage("en")
        }

        binding?.btnOnboardingIndonesia?.setSafeOnClickListener {
            chooseOnboardingLanguage("in")
        }
    }

    /**
     * Slide 0 shows the language picker instead of Skip/indicators/Next until
     * a language is chosen; every other slide always shows normal navigation.
     */
    private fun updateFirstSlideGate(position: Int, slideCount: Int) {
        if (position == 0 && !languageChosen) {
            binding?.txtChooseLanguage?.visible()
            binding?.layoutLanguagePicker?.visible()
            binding?.btnSkip?.hide()
            binding?.layoutIndicators?.hide()
            binding?.btnNext?.hide()
            binding?.btnGetStarted?.hide()
        } else {
            binding?.txtChooseLanguage?.hide()
            binding?.layoutLanguagePicker?.hide()
            binding?.btnSkip?.visible()
            binding?.layoutIndicators?.visible()
            if (position == slideCount - 1) {
                binding?.btnNext?.hide()
                binding?.btnGetStarted?.visible()
            } else {
                binding?.btnNext?.visible()
                binding?.btnGetStarted?.hide()
            }
        }
    }

    private fun chooseOnboardingLanguage(languageCode: String) {
        languageChosen = true
        // Triggers AppCompatDelegate to persist the locale and recreate the host
        // Activity; the recreated SplashMainFragment re-detects languageChosen
        // from AppCompatDelegate.getApplicationLocales() and unlocks slide 0.
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
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
                enterPasswordLauncher.launch(EnterPasswordActivity.newIntent(requireContext()))
            }
            else -> {
                navigateToDashboard()
            }
        }
    }

    private fun navigateToDashboard() {
        findNavController().navigate(
            R.id.dashboard_navigation_graph,
            null,
            navOptions {
                popUpTo(R.id.splash_navigation_graph) { inclusive = true }
                launchSingleTop = true
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
