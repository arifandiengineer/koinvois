package com.koinvois.generator.ui.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.MainActivity
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentMoreBinding
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
            txtTitle.text = "Clients"
            txtSubtitle.text = "Manage your clients"
            imgIcon.setImageResource(R.drawable.client_icon)
            iconContainer.setCardBackgroundColor(requireContext().getColor(R.color.color_stat_green_bg))
            imgIcon.setColorFilter(requireContext().getColor(R.color.color_success))
        }
        binding.menuItems.apply {
            txtTitle.text = "Items"
            txtSubtitle.text = "Manage your items"
            imgIcon.setImageResource(R.drawable.items_icon)
            iconContainer.setCardBackgroundColor(requireContext().getColor(R.color.color_stat_green_bg))
            imgIcon.setColorFilter(requireContext().getColor(R.color.color_success))
        }

        binding.menuSettings.apply {
            txtTitle.text = "Settings"
            txtSubtitle.text = "App preferences and customization"
            imgIcon.setImageResource(R.drawable.icon_setting)
            iconContainer.setCardBackgroundColor(requireContext().getColor(R.color.color_neutral_surface_light))
            //imgIcon.setColorFilter(requireContext().getColor(R.color.color_text_secondary))
        }


    }

    private fun setClickListeners() {
        binding.menuClients.root.setSafeOnClickListener {
            findNavController().navigate(R.id.client_navigation_graph)
        }
        binding.menuItems.root.setSafeOnClickListener {
            findNavController().navigate(R.id.item_navigation_graph)
        }
        binding.menuSettings.root.setSafeOnClickListener {
            findNavController().navigate(R.id.setting_navigation_graph)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}