package com.koinvois.generator.ui.estimates.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koinvois.generator.database.models.EstimatePhoto
import com.koinvois.generator.databinding.ItemInvoicePhotoListBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener

class SelectedPhotosForEstimateAdapter(
    private val selectedPhotoList: ArrayList<EstimatePhoto>,
    private val viewModel: EstimatesMainViewModel,
    private val onPhotoClick: (EstimatePhoto) -> Unit
) :
    RecyclerView.Adapter<SelectedPhotosForEstimateAdapter.SelectedPhotosForInvoiceViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedPhotosForInvoiceViewHolder {
        val binding =
            ItemInvoicePhotoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    class SelectedPhotosForInvoiceViewHolder(val binding: ItemInvoicePhotoListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(singleInvoicePhoto: EstimatePhoto) {
            binding.txtAdditionalDetails.text = singleInvoicePhoto.estimatePhotoAdditionalDetails
            binding.txtPhotoDescription.text = singleInvoicePhoto.estimatePhotoDescription
        }
    }
}