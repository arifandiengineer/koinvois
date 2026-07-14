package com.koinvois.generator.ui.item.add_item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.koinvois.generator.R
import com.koinvois.generator.core.common.dialog.BaseDialog
import com.koinvois.generator.domain.model.Item
import com.koinvois.generator.databinding.FragmentAddItemBinding
import com.koinvois.generator.ui.item.ItemMainViewModel
import com.koinvois.generator.utilities.constants.Constants
import com.koinvois.generator.utilities.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private var binding: FragmentAddItemBinding? = null
    private val args: AddItemFragmentArgs by navArgs()
    private val viewModel: ItemMainViewModel by hiltNavGraphViewModels(R.id.item_navigation_graph)
    private var item: Item? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddItemBinding.inflate(inflater, container, false)

        setUpToolbar()
        if (args.itemType == Constants.EXISTING_ITEM) {
            binding?.let {
                with(it) {
                    with(viewModel.itemUpdateModel.value) {
                        this?.let {
                            editItemDescription.setText(itemName)
                            editItemUnitCost.setText(itemUnitCost.toString())
                            editAdditionalItemDetails.setText(itemDetails)
                            switchItemTaxable.isChecked = itemTaxable ?: false
                        }
                    }
                }
            }
        }

        backPressed()
        return binding?.root
    }

    private fun setUpToolbar() {
        binding?.customToolbar?.btnBack?.visible()
        binding?.customToolbar?.txtToolbarTitle?.text = if (args.itemType == Constants.EXISTING_ITEM) 
            getString(R.string.label_edit_item) else getString(R.string.label_add_item)

        if (args.itemType == Constants.EXISTING_ITEM) {
            binding?.customToolbar?.imgRightAction?.visible()
            binding?.customToolbar?.imgRightAction?.setImageResource(R.drawable.icon_three_dot)
            binding?.customToolbar?.imgRightAction?.setSafeOnClickListener {
                val popup = PopupMenu(requireContext(), it)
                popup.menuInflater.inflate(R.menu.popup_menu_client, popup.menu)
                popup.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        when (item.title) {
                            getString(R.string.delete_menu_item) -> {
                                context?.let { ctx ->
                                    BaseDialog.confirm(
                                        context = ctx,
                                        title = getString(R.string.delete_confirm_title),
                                        message = getString(R.string.delete_item_confirm_message),
                                        positiveText = getString(R.string.delete_confirm_positive),
                                        negativeText = getString(R.string.delete_confirm_negative),
                                        onConfirm = {
                                            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                                viewModel.deleteItem()
                                                findNavController().navigateUp()
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        return true
                    }
                })
                popup.show()
            }
        }

        binding?.customToolbar?.btnBack?.setOnClickListener {
            saveOnBack()
        }
    }

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            saveOnBack()
        }
    }

    private fun saveOnBack() {
        if (binding?.editItemDescription?.text.toString().isNotEmpty()) {
            when (args.itemType) {
                Constants.NEW_ITEM -> {
                    binding?.let {
                        with(it) {
                            item = Item(
                                0,
                                editItemDescription.getString(),
                                editItemUnitCost.getFloat(),
                                switchItemTaxable.isChecked,
                                editAdditionalItemDetails.getString(),
                            )
                        }
                    }
                    item?.let { it1 ->
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            viewModel.addItem(it1)
                        }
                    }
                }
                Constants.EXISTING_ITEM -> {
                    binding?.let {
                        with(it) {
                            item = viewModel.itemUpdateModel.value?.itemId?.let { it1 ->
                                Item(
                                    it1,
                                    editItemDescription.getString(),
                                    editItemUnitCost.getFloat(),
                                    switchItemTaxable.isChecked,
                                    editAdditionalItemDetails.getString(),
                                )
                            }
                        }
                    }
                    item?.let { it1 ->
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            viewModel.updateItem(it1)
                        }
                    }
                }
            }
            findNavController().navigateUp()
        } else {
            findNavController().navigateUp()
        }
    }

}