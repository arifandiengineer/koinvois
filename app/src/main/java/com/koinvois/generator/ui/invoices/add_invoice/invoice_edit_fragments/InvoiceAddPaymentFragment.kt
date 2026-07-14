package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.data_models.InvoicePaymentsModel
import com.koinvois.generator.databinding.FragmentInvoiceAddPaymentBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.enums.PaymentMethodEnum
import com.koinvois.generator.utilities.extensions.*
import com.koinvois.generator.utilities.manager.DialogManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class InvoiceAddPaymentFragment : Fragment() {

    private var binding: FragmentInvoiceAddPaymentBinding? = null
    private val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInvoiceAddPaymentBinding.inflate(inflater, container, false)

        val c: Date = Calendar.getInstance().time
        println("Current time => $c")

        val df = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val formattedDate: String = df.format(c)

        binding?.editPaymentDate?.text = formattedDate

        setUpToolbar()
        setCLickListeners()
        backPressed()

        return binding?.root
    }

    private fun setCLickListeners() {
        binding?.customToolbar?.btnBack?.setSafeOnClickListener {
            saveOnBack()
        }

        binding?.editPaymentDate?.setSafeOnClickListener {
            val datePicker = activity?.let { it1 ->
                DialogManager.makeDatePickerDialog(it1) { date ->
                    binding?.editPaymentDate?.text = date
                }
            }
            datePicker?.show()
        }

        binding?.editPaymentMethod?.setSafeOnClickListener {
            activity?.let { it1 -> showBottomSheetForPaymentMethod(it1) }
        }
    }

    private fun showBottomSheetForPaymentMethod(context: Context) {
        val bottomSheetForPaymentMethod = BottomSheetDialog(context)
        bottomSheetForPaymentMethod.setContentView(R.layout.bottom_sheet_payment_method)

        val cashMethod = bottomSheetForPaymentMethod.findViewById<TextView>(R.id.txtCash)?.setSafeOnClickListener {
            binding?.editPaymentMethod?.text = PaymentMethodEnum.CASH.method
            bottomSheetForPaymentMethod.dismiss()
        }
        val chequeMethod = bottomSheetForPaymentMethod.findViewById<TextView>(R.id.txtCheque)?.setSafeOnClickListener {
            binding?.editPaymentMethod?.text = PaymentMethodEnum.CHEQUE.method
            bottomSheetForPaymentMethod.dismiss()
        }
        val bankMethod = bottomSheetForPaymentMethod.findViewById<TextView>(R.id.txtBank)?.setSafeOnClickListener {
            binding?.editPaymentMethod?.text = PaymentMethodEnum.BANK.method
            bottomSheetForPaymentMethod.dismiss()
        }
        val creditMethod = bottomSheetForPaymentMethod.findViewById<TextView>(R.id.txtCredit)?.setSafeOnClickListener {
            binding?.editPaymentMethod?.text = PaymentMethodEnum.CREDIT_CARD.method
            bottomSheetForPaymentMethod.dismiss()
        }
        val payPalMethod = bottomSheetForPaymentMethod.findViewById<TextView>(R.id.txtPaypal)?.setSafeOnClickListener {
            binding?.editPaymentMethod?.text = PaymentMethodEnum.PAYPAL.method
            bottomSheetForPaymentMethod.dismiss()
        }
        val otherMethod = bottomSheetForPaymentMethod.findViewById<TextView>(R.id.txtOther)?.setSafeOnClickListener {
            binding?.editPaymentMethod?.text = PaymentMethodEnum.OTHER.method
            bottomSheetForPaymentMethod.dismiss()
        }

        bottomSheetForPaymentMethod.show()

    }

    private fun backPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            saveOnBack()
        }
    }

    private fun saveOnBack() {
        if (binding?.editPaymentAmount?.text?.isNotEmpty() == true) {
            val payment = InvoicePaymentsModel(
                binding?.editPaymentAmount?.getFloat(),
                binding?.editPaymentMethod?.getStringText(),
                binding?.editPaymentDate?.getStringText(),
                binding?.editPaymentNotes?.getString()
            )
            viewModel.addPayment(payment)
        } else {
            activity?.toastL("Please enter some amount!")
        }
        findNavController().navigateUp()
    }

    private fun setUpToolbar() {
        binding?.let {
            with(it) {
                customToolbar.txtToolbarTitle.text = getString(R.string.label_payment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}