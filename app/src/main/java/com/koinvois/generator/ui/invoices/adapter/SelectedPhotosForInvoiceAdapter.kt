package com.koinvois.generator.ui.invoices.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.koinvois.generator.database.models.InvoicePhoto
import com.koinvois.generator.databinding.ItemInvoicePhotoBinding
import com.koinvois.generator.ui.invoices.InvoiceMainViewModel
import com.koinvois.generator.ui.invoices.add_invoice.AddInvoiceMainFragmentDirections
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener

class SelectedPhotosForInvoiceAdapter(
    private val selectedPhotoList: ArrayList<InvoicePhoto>,
    private val navController: NavController,
    private val viewModel: InvoiceMainViewModel
) :
    RecyclerView.Adapter<SelectedPhotosForInvoiceAdapter.SelectedPhotosForInvoiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedPhotosForInvoiceViewHolder {
        val binding = ItemInvoicePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedPhotosForInvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedPhotosForInvoiceViewHolder, position: Int) {
        holder.bind(selectedPhotoList[position])

        holder.binding.root.setSafeOnClickListener {
            viewModel.currentSelectedPhoto = selectedPhotoList[position]
            val action = AddInvoiceMainFragmentDirections.actionFragmentInvoiceEditToFragmentAddPhoto(DBEnum.OLD.entryType)
            navController.navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return selectedPhotoList.size
    }

    class SelectedPhotosForInvoiceViewHolder(val binding: ItemInvoicePhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(singleInvoicePhoto: InvoicePhoto) {
            binding.txtAdditionalDetails.text = singleInvoicePhoto.invoicePhotoAdditionalDetails
            binding.txtPhotoDescription.text = singleInvoicePhoto.invoicePhotoDescription
        }
    }
}