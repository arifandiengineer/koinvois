package com.koinvois.generator.ui.estimates.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.koinvois.generator.database.models.EstimatePhoto
import com.koinvois.generator.databinding.ItemInvoicePhotoBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.ui.estimates.add_estimate.AddEstimateMainFragmentDirections
import com.koinvois.generator.utilities.enums.DBEnum
import com.koinvois.generator.utilities.extensions.setSafeOnClickListener

class SelectedPhotosForEstimateAdapter(
    private val selectedPhotoList: ArrayList<EstimatePhoto>,
    private val navController: NavController,
    private val viewModel: EstimatesMainViewModel
) :
    RecyclerView.Adapter<SelectedPhotosForEstimateAdapter.SelectedPhotosForInvoiceViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedPhotosForInvoiceViewHolder {
        val binding =
            ItemInvoicePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedPhotosForInvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedPhotosForInvoiceViewHolder, position: Int) {
        holder.bind(selectedPhotoList[position])

        holder.binding.root.setSafeOnClickListener {
            viewModel.currentSelectedPhoto = selectedPhotoList[position]
            val action =
                AddEstimateMainFragmentDirections.actionFragmentEditEstimateMainToFragmentAddPhotoToEstimate(DBEnum.OLD.entryType)
            navController.navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return selectedPhotoList.size
    }

    class SelectedPhotosForInvoiceViewHolder(val binding: ItemInvoicePhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(singleInvoicePhoto: EstimatePhoto) {
            binding.txtAdditionalDetails.text = singleInvoicePhoto.estimatePhotoAdditionalDetails
            binding.txtPhotoDescription.text = singleInvoicePhoto.estimatePhotoDescription
        }
    }
}