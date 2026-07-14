package com.koinvois.generator.ui.invoices.add_invoice.invoice_edit_fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentSignatureBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.data_models.Signature
import com.koinvois.generator.utilities.extensions.*
import com.koinvois.generator.utilities.signature.SignatureView

class SignatureFragment : Fragment(), SignatureView.OnSignedListener {

    private var binding: FragmentSignatureBinding? = null
    private val viewModel: InvoiceMainViewModel by hiltNavGraphViewModels(R.id.invoice_navigation_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignatureBinding.inflate(inflater, container, false)

        setClickListeners()
        setUpToolbar()
        viewModel.signatureObj?.signatureBitmap?.let {
            binding?.signatureView?.setSignatureBitmap(it)
        }

        return binding?.root
    }

    private fun setClickListeners() {
        binding?.btnClear?.setSafeOnClickListener { binding?.signatureView?.clear() }

        binding?.btnOk?.setOnClickListener {
            val bitmap: Bitmap? = binding?.signatureView?.getSignatureBitmap()
            val date = getCurrentDateTime()
            val dateInString = date.toStringFormat("yyyy/MM/dd")
            viewModel.signatureObj = bitmap?.let { it1 -> Signature(it1, dateInString) }
            findNavController().navigateUp()
        }

        binding?.btnCancel?.setSafeOnClickListener {
            findNavController().navigateUp()
        }

        binding?.customToolbar?.btnBack?.setSafeOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onStartSigning() {
        Log.e("sign", "started")
    }

    override fun onSigned() {
        Log.e("sign", "signed")
        binding?.btnOk?.isEnabled = true
        binding?.btnClear?.isEnabled = true
    }

    override fun onClear() {
        Log.e("sign", "cleared")

        binding?.btnClear?.isEnabled = false
        binding?.btnOk?.isEnabled = false
    }

    private fun setUpToolbar() {
        binding?.customToolbar?.btnBack?.visible()
        binding?.customToolbar?.txtToolbarTitle?.text = getString(R.string.title_signature)
    }
}