package com.koinvois.generator.ui.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.MainActivity
import com.koinvois.generator.R
import com.koinvois.generator.core.common.state.bind
import com.koinvois.generator.domain.model.Item
import com.koinvois.generator.databinding.ItemMainFragmentBinding
import com.koinvois.generator.ui.item.adapter.AllItemsAdapter
import com.koinvois.generator.utilities.constants.Constants
import com.koinvois.generator.utilities.extensions.hide
import com.koinvois.generator.utilities.extensions.inVisible
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener
import com.koinvois.generator.utilities.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItemMainFragment : Fragment() {

    private var binding: ItemMainFragmentBinding? = null
    private val viewModel: ItemMainViewModel by hiltNavGraphViewModels(R.id.item_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemMainFragmentBinding.inflate(inflater, container, false)

        setUpToolbar()
        setClickListeners()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main)
        {
            val allItems = viewModel.getAllItems()
            if (allItems.isEmpty()) {
                binding?.rvAllItems?.hide()
                binding?.emptyState?.root?.visible()
                binding?.emptyState?.bind(title = getString(R.string.no_items_empty_state))
            } else {
                binding?.rvAllItems?.visible()
                setUpRecyclerView(allItems)
                binding?.emptyState?.root?.hide()
            }
        }

        return binding?.root
    }

    private fun setClickListeners() {
        binding?.btnAddItem?.setSafeOnClickListener {
            val action =
                ItemMainFragmentDirections.actionMainItemFragmentToAddItemFragment(Constants.NEW_ITEM)
            findNavController().navigate(action)
        }
    }

    private fun setUpToolbar() {
        val customToolbar = (activity as MainActivity).binding?.customToolbar
        customToolbar?.imgRightAction?.visible()
        customToolbar?.btnBack?.inVisible()
        customToolbar?.imgSecondaryAction?.inVisible()
        customToolbar?.txtToolbarTitle?.text = getString(R.string.title_items)
    }

    private fun setUpRecyclerView(allItems: ArrayList<Item>) {
        val adapter = AllItemsAdapter(findNavController(), viewModel)
        binding?.rvAllItems?.adapter = adapter
        adapter.submitList(allItems)
    }
}