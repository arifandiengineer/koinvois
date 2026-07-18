package com.koinvois.generator.ui.estimates

import com.koinvois.generator.data_models.Signature
import com.koinvois.generator.database.models.Client
import com.koinvois.generator.database.models.Item
import com.koinvois.generator.database.models.EstimateItem
import com.koinvois.generator.database.models.EstimatePhoto
import com.koinvois.generator.database.models.PersonalBusiness

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
    var taxAmount: Float = 0f
    var photosForEstimate: ArrayList<EstimatePhoto>? = arrayListOf()
    var signatureObj: Signature? = null
    var estimateNotes: String? = null
    var estimateStatus: String? = null
    var allClients: ArrayList<Client>? = null
    var allItems: ArrayList<Item>? = null
}
