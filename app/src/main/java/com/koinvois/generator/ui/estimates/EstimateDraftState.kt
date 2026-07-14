package com.koinvois.generator.ui.estimates

import com.koinvois.generator.data_models.Signature
import com.koinvois.generator.database.models.Client
import com.koinvois.generator.database.models.Item
import com.koinvois.generator.database.models.EstimateItem
import com.koinvois.generator.database.models.EstimatePhoto
import com.koinvois.generator.database.models.PersonalBusiness

/**
 * Process-lifetime holder for the in-progress estimate draft, shared across the
 * estimate edit Activities (each screen has its own EstimatesMainViewModel instance
 * via `by viewModels()`, but they all inject this same @Singleton so the draft
 * stays consistent across Activity boundaries the way hiltNavGraphViewModels
 * used to keep it consistent across Fragment destinations in one nav graph).
 */
@javax.inject.Singleton
class EstimateDraftState @javax.inject.Inject constructor() {

    var estimatePrimaryId: Long? = null
    var estimateNumber: Int? = null
    var estimateDate: String? = null
    var estimatePoNumber: String? = null
    var businessUpdateModel: PersonalBusiness? = null
    var selectedClient: Client? = null
    var selectedItemsList: ArrayList<EstimateItem>? = arrayListOf()
    var currentEstimateItem: EstimateItem? = null
    var currentSelectedPhoto: EstimatePhoto? = null
    var estimateSubTotal: Float? = null
    var discountType: String? = null
    var discountAmount: Float? = null
    var discountTotalAmount: Float? = null
    var taxType: String? = null
    var taxLabel: String? = null
    var taxRate: Float? = null
    var taxInclusive: Boolean? = null
    var estimateTotal: Float? = null
    var photosForEstimate: ArrayList<EstimatePhoto>? = arrayListOf()
    var signatureObj: Signature? = null
    var estimateNotes: String? = null
    var estimateStatus: String? = null
    var allClients: ArrayList<Client>? = null
    var allItems: ArrayList<Item>? = null
}
