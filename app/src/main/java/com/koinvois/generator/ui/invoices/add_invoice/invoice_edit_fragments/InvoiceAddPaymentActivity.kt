package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.data_models.InvoicePaymentsModel
import com.koinvois.generator.databinding.FragmentInvoiceAddPaymentBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.enums.PaymentMethodEnum
import com.koinvois.generator.utilities.extensions.*
import com.koinvois.generator.utilities.manager.DialogManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class InvoiceAddPaymentActivity : BaseActivity<FragmentInvoiceAddPaymentBinding>() {

    private val viewModel: InvoiceMainViewModel by viewModels()

    override fun inflateBinding(): FragmentInvoiceAddPaymentBinding =
        FragmentInvoiceAddPaymentBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        val c: Date = Calendar.getInstance().time
        println("Current time => $c")

        val df = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val formattedDate: String = df.format(c)

        binding.editPaymentDate.text = formattedDate

        setUpToolbar()
        setCLickListeners()
        onBackPressedDispatcher.addCallback(this) { saveOnBack() }
    }

    private fun setCLickListeners() {
        binding.customToolbar.btnBack.setSafeOnClickListener {
            saveOnBack()
        }

        binding.editPaymentDate.setSafeOnClickListener {
            val datePicker = DialogManager.makeDatePickerDialog(this) { date ->
                binding.editPaymentDate.text = date
            }
            datePicker.show()
        }

        binding.editPaymentMethod.setSafeOnClickListener {
            showBottomSheetForPaymentMethod(this)
        }
    }

    private fun showBottomSheetForPaymentMethod(context: Context) {
        val bottomSheetForPaymentMethod = BottomSheetDialog(context)
        bottomSheetForPaymentMethod.setContentView(R.layout.bottom_sheet_payment_method)

        bottomSheetForPaymentMethod.findViewById<TextView>(R.id.txtCash)?.setSafeOnClickListener {
            binding.editPaymentMethod.text = PaymentMethodEnum.CASH.method
            bottomSheetForPaymentMethod.dismiss()
        }
        bottomSheetForPaymentMethod.findViewById<TextView>(R.id.txtCheque)?.setSafeOnClickListener {
            binding.editPaymentMethod.text = PaymentMethodEnum.CHEQUE.method
            bottomSheetForPaymentMethod.dismiss()
        }
        bottomSheetForPaymentMethod.findViewById<TextView>(R.id.txtBank)?.setSafeOnClickListener {
            binding.editPaymentMethod.text = PaymentMethodEnum.BANK.method
            bottomSheetForPaymentMethod.dismiss()
        }
        bottomSheetForPaymentMethod.findViewById<TextView>(R.id.txtCredit)?.setSafeOnClickListener {
            binding.editPaymentMethod.text = PaymentMethodEnum.CREDIT_CARD.method
            bottomSheetForPaymentMethod.dismiss()
        }
        bottomSheetForPaymentMethod.findViewById<TextView>(R.id.txtPaypal)?.setSafeOnClickListener {
            binding.editPaymentMethod.text = PaymentMethodEnum.PAYPAL.method
            bottomSheetForPaymentMethod.dismiss()
        }
        bottomSheetForPaymentMethod.findViewById<TextView>(R.id.txtOther)?.setSafeOnClickListener {
            binding.editPaymentMethod.text = PaymentMethodEnum.OTHER.method
            bottomSheetForPaymentMethod.dismiss()
        }

        bottomSheetForPaymentMethod.show()
    }

    private fun saveOnBack() {
        if (binding.editPaymentAmount.text?.isNotEmpty() == true) {
            val payment = InvoicePaymentsModel(
                binding.editPaymentAmount.getFloat(),
                binding.editPaymentMethod.getStringText(),
                binding.editPaymentDate.getStringText(),
                binding.editPaymentNotes.getString()
            )
            viewModel.addPayment(payment)
        } else {
            toastL("Please enter some amount!")
        }
        finish()
    }

    private fun setUpToolbar() {
        binding.customToolbar.txtToolbarTitle.text = getString(R.string.label_payment)
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, InvoiceAddPaymentActivity::class.java)
    }
}
