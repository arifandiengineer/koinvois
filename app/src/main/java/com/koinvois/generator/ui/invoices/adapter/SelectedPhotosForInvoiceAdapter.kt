package com.koinvois.generator.ui.invoices.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koinvois.generator.database.models.InvoicePhoto
import com.koinvois.generator.databinding.ItemInvoicePhotoListBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener

class SelectedPhotosForInvoiceAdapter(
    private val selectedPhotoList: ArrayList<InvoicePhoto>,
    private val viewModel: InvoiceMainViewModel,
    private val onPhotoClick: (InvoicePhoto) -> Unit
) :
    RecyclerView.Adapter<SelectedPhotosForInvoiceAdapter.SelectedPhotosForInvoiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedPhotosForInvoiceViewHolder {
        val binding = ItemInvoicePhotoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedPhotosForInvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedPhotosForInvoiceViewHolder, position: Int) {
        holder.bind(selectedPhotoList[position])

        holder.binding.root.setSafeOnClickListener {
            viewModel.currentSelectedPhoto = selectedPhotoList[position]
            onPhotoClick(selectedPhotoList[position])
        }

    }

    override fun getItemCount(): Int {
        return selectedPhotoList.size
    }

    class SelectedPhotosForInvoiceViewHolder(val binding: ItemInvoicePhotoListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(singleInvoicePhoto: InvoicePhoto) {
            binding.txtAdditionalDetails.text = singleInvoicePhoto.invoicePhotoAdditionalDetails
            binding.txtPhotoDescription.text = singleInvoicePhoto.invoicePhotoDescription
            singleInvoicePhoto.invoicePhoto?.let {
                binding.imgPhoto.setImageBitmap(it)
            }
        }
    }
}