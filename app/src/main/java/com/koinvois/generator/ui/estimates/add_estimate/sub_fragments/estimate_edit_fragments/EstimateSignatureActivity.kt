package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments.estimate_edit_fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.koinvois.generator.R
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.databinding.FragmentSignatureBinding
import com.koinvois.generator.data_models.Signature
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.extensions.*
import com.koinvois.generator.utilities.signature.SignatureView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EstimateSignatureActivity : BaseActivity<FragmentSignatureBinding>(), SignatureView.OnSignedListener {

    private val viewModel: EstimatesMainViewModel by viewModels()

    override fun inflateBinding(): FragmentSignatureBinding =
        FragmentSignatureBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        setClickListeners()
        setUpToolbar()
        viewModel.signatureObj?.signatureBitmap?.let {
            binding.signatureView.setSignatureBitmap(it)
        }
    }

    private fun setClickListeners() {
        binding.btnClear.setSafeOnClickListener { binding.signatureView.clear() }

        binding.btnOk.setOnClickListener {
            val bitmap: Bitmap? = binding.signatureView.getSignatureBitmap()
            val date = getCurrentDateTime()
            val dateInString = date.toStringFormat("yyyy/MM/dd")
            viewModel.signatureObj = bitmap?.let { it1 -> Signature(it1, dateInString) }
            finish()
        }

        binding.btnCancel.setSafeOnClickListener {
            finish()
        }

        binding.customToolbar.btnBack.setSafeOnClickListener {
            finish()
        }
    }

    override fun onStartSigning() {
        android.util.Log.e("sign", "started")
    }

    override fun onSigned() {
        android.util.Log.e("sign", "signed")
        binding.btnOk.isEnabled = true
        binding.btnClear.isEnabled = true
    }

    override fun onClear() {
        android.util.Log.e("sign", "cleared")

        binding.btnClear.isEnabled = false
        binding.btnOk.isEnabled = false
    }

    private fun setUpToolbar() {
        binding.customToolbar.btnBack.visible()
        binding.customToolbar.txtToolbarTitle.text = getString(R.string.title_signature)
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, EstimateSignatureActivity::class.java)
    }
}
