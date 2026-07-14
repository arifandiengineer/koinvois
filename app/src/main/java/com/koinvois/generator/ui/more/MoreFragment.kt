package com.koinvois.generator.ui.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentMoreBinding
import com.koinvois.generator.ui.client.ClientMainActivity
import com.koinvois.generator.ui.item.ItemMainActivity
import com.koinvois.generator.ui.setting.SettingActivity
import com.koinvois.generator.utilities.extensions.inVisible
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)

        setUpMenuItems()
        setClickListeners()

        return binding.root
    }



    private fun setUpMenuItems() {
        // Business Section
        binding.menuClients.apply {
            txtTitle.text = getString(R.string.title_clients)
            txtSubtitle.text = getString(R.string.more_menu_client_subtitle)
            imgIcon.setImageResource(R.drawable.client_icon)
            iconContainer.setCardBackgroundColor(requireContext().getColor(R.color.color_stat_green_bg))
            imgIcon.setColorFilter(requireContext().getColor(R.color.color_success))
        }
        binding.menuItems.apply {
            txtTitle.text = getString(R.string.title_items)
            txtSubtitle.text = getString(R.string.more_menu_item_subtitle)
            imgIcon.setImageResource(R.drawable.items_icon)
            iconContainer.setCardBackgroundColor(requireContext().getColor(R.color.color_stat_green_bg))
            imgIcon.setColorFilter(requireContext().getColor(R.color.color_success))
        }

        binding.menuSettings.apply {
            txtTitle.text = getString(R.string.title_setting)
            txtSubtitle.text = getString(R.string.more_menu_settings_subtitle)
            imgIcon.setImageResource(R.drawable.icon_setting)
            iconContainer.setCardBackgroundColor(requireContext().getColor(R.color.color_neutral_surface_light))
            //imgIcon.setColorFilter(requireContext().getColor(R.color.color_text_secondary))
        }
    }

    private fun setClickListeners() {
        binding.menuClients.root.setSafeOnClickListener {
            startActivity(ClientMainActivity.newIntent(requireContext()))
        }
        binding.menuItems.root.setSafeOnClickListener {
            startActivity(ItemMainActivity.newIntent(requireContext()))
        }
        binding.menuSettings.root.setSafeOnClickListener {
            startActivity(SettingActivity.newIntent(requireContext()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}