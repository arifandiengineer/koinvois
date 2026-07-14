package com.koinvois.generator.ui.invoices.add_invoice.sub_fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentEditInvoiceBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.invoices.adapter.SelectedInvoiceItemsAdapter
import com.koinvois.generator.ui.invoices.adapter.SelectedPhotosForInvoiceAdapter
import com.koinvois.generator.ui.invoices.add_invoice.AddInvoiceMainFragment
import com.koinvois.generator.ui.invoices.add_invoice.AddInvoiceMainFragmentDirections
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.enums.InvoiceStatusEnum
import com.koinvois.generator.utilities.extensions.*
import kotlinx.coroutines.launch
import java.util.Locale

class EditInvoiceFragment : Fragment() {

    private var binding: FragmentEditInvoiceBinding? = null
    private val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditInvoiceBinding.inflate(inflater, container, false)
        setUpToolbar()
        setClickListeners()
        observePaymentSummary()

        return binding?.root
    }

    private fun observePaymentSummary() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.totalPaidAmount.collect { total ->
                        binding?.txtPaymentValue?.text = String.format(Locale.getDefault(), "$%.2f", total)
                    }
                }
                launch {
                    viewModel.balanceDue.collect { balance ->
                        binding?.txtTotalBalanceAmount?.text = String.format(Locale.getDefault(), "$%.2f", balance)
                    }
                }
            }
        }
    }

    private fun setUpToolbar() {
        binding?.customToolbar?.apply {
            btnBack.visible()
            txtToolbarTitle.text = getString(R.string.label_edit_invoice)
            
            // Three-dot menu for Share/Delete
            imgSecondaryAction.visible()
            imgSecondaryAction.setImageResource(R.drawable.icon_three_dot)
            imgSecondaryAction.setSafeOnClickListener {
                (parentFragment as? AddInvoiceMainFragment)?.showPopupMenu(it)
            }

            // Forward button for Tab navigation
            imgRightAction.visible()
            imgRightAction.setImageResource(R.drawable.btn_forward)
            imgRightAction.setColorFilter(requireContext().getColor(R.color.yellow_tab_indicator))
            imgRightAction.setSafeOnClickListener {
                (parentFragment as? AddInvoiceMainFragment)?.binding?.viewPager?.currentItem = 1
            }

            btnBack.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    private fun setClickListeners() {

        binding?.txtTaxPrice?.setSafeOnClickListener {
            val action = AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentTax()
            findNavController().navigate(action)
        }

        binding?.txtSignature?.setSafeOnClickListener {
            val action =
                AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentSignature()
            findNavController().navigate(action)
        }

        binding?.txtAddPhoto?.setSafeOnClickListener {
            val action =
                AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentAddPhoto(DBEnum.NEW.entryType)
            findNavController().navigate(action)
        }

        binding?.btnInvoiceDate?.setSafeOnClickListener {
            val action =
                AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentInvoiceInformation()
            findNavController().navigate(action)
        }

        binding?.btnInvoiceNumber?.setSafeOnClickListener {
            val action =
                AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentInvoiceInformation()
            findNavController().navigate(action)
        }

        binding?.btnDueOnReceipt?.setSafeOnClickListener {
            val action =
                AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentInvoiceInformation()
            findNavController().navigate(action)
        }

        binding?.btnBusinessInfo?.setSafeOnClickListener {
            val action =
                AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentBusinessDetail()
            findNavController().navigate(action)
        }

        binding?.secondCard?.setSafeOnClickListener {

            viewModel.selectedClient?.let {
                val action =
                    AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentClientDetail()
                findNavController().navigate(action)
            } ?: run {
                when (viewModel.allClients?.isNotEmpty()) {
                    true -> {
                        val action =
                            AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentClientList()
                        findNavController().navigate(action)
                    }
                    false -> {
                        val action =
                            AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentClientDetail()
                        findNavController().navigate(action)
                    }
                    null -> {
                        binding?.root?.showErrorSnackbar(getString(R.string.error_try_again))
                    }
                }
            }
        }

        binding?.txtAddItem?.setSafeOnClickListener {
            val action =
                AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentItemDetailForInvoice(
                    DBEnum.NEW.entryType
                )
            findNavController().navigate(action)
        }

        binding?.txtDiscountPrice?.setSafeOnClickListener {
            val action =
                AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentDiscount()
            findNavController().navigate(action)
        }

        binding?.btnMarkPaid?.setSafeOnClickListener {
            when (viewModel.invoiceStatus) {
                InvoiceStatusEnum.PAID.status -> {
                    viewModel.invoiceStatus = InvoiceStatusEnum.UN_PAID.status
                    binding?.btnMarkPaid?.setText("Mark Paid")
                }
                InvoiceStatusEnum.UN_PAID.status -> {
                    viewModel.invoiceStatus = InvoiceStatusEnum.PAID.status
                    binding?.btnMarkPaid?.setText("Mark Unpaid")

                }
                else -> {
                    viewModel.invoiceStatus = InvoiceStatusEnum.PAID.status
                    binding?.btnMarkPaid?.setText("Mark Unpaid")
                }
            }
        }

        binding?.txtPaymentPrice?.setSafeOnClickListener {
            val action =
                AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentPayments()
            findNavController().navigate(action)
        }

        binding?.txtNotes?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val notes = binding?.txtNotes?.text.toString()
                viewModel.invoiceNotes = notes
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding?.txtPaymentInstruction?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val instructions = binding?.txtPaymentInstruction?.text.toString()
                viewModel.invoicePaymentInstructions = instructions
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

    override fun onResume() {
        super.onResume()

        viewModel.selectedClient.let {
            binding?.txtClientName?.text = it?.clientName
        }

        viewModel.signatureObj?.let {
            // binding?.txtSignature?.text = "Signed on ${it?.signatureDate}"
        }
        viewModel.recalculateInvoiceSubTotal()
        viewModel.recalculateInvoiceTotal()

        binding?.txtSubtotal?.text = String.format(Locale.getDefault(), "$%.2f", viewModel.invoiceSubTotal ?: 0f)
        binding?.txtDiscountValue?.text = String.format(Locale.getDefault(), "-$%.2f", viewModel.discountTotalAmount ?: 0f)
        binding?.txtTaxLabel?.text = if (viewModel.taxRate != null) {
            "Tax (${viewModel.taxRate?.let { rate -> if (rate == rate.toInt().toFloat()) rate.toInt().toString() else rate.toString() }}%)"
        } else {
            "Tax"
        }
        binding?.txtTaxValue?.text = String.format(Locale.getDefault(), "$%.2f", viewModel.taxAmount)
        binding?.txtTotalAmount?.text = String.format(Locale.getDefault(), "$%.2f", viewModel.invoiceTotal)

        viewModel.invoiceNumber?.let {
            binding?.txtInvoiceNumber?.text = "INV-$it"
        } ?: run {
            binding?.txtInvoiceNumber?.text = null
        }

        viewModel.invoiceDate?.let {
            binding?.txtInvoiceDate?.text = it
        }

        viewModel.selectedItemsList?.let {
            binding?.rvInvoiceItems?.adapter =
                SelectedInvoiceItemsAdapter(it, findNavController(), viewModel)
        }

        viewModel.photosForInvoice?.let {
            binding?.rvPhotos?.adapter =
                SelectedPhotosForInvoiceAdapter(it, findNavController(), viewModel)
        }

        viewModel.invoicePaymentInstructions?.let {
            binding?.txtPaymentInstruction?.setText(it)
        }

        viewModel.invoiceNotes?.let {
            binding?.txtNotes?.setText(it)
        }

        viewModel.invoiceStatus?.let {
            when (it) {
                InvoiceStatusEnum.UN_PAID.status -> {
                    binding?.btnMarkPaid?.setText("Mark Paid")
                }
                InvoiceStatusEnum.PAID.status -> {
                    binding?.btnMarkPaid?.setText("Mark Unpaid")
                }
                else -> {
                    binding?.btnMarkPaid?.setText("Mark Paid")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}