package com.koinvois.generator.ui.estimates

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.koinvois.generator.data.mapper.toDomain
import com.koinvois.generator.data.mapper.toEntity
import com.koinvois.generator.data_models.Signature
import com.koinvois.generator.database.models.*
import com.koinvois.generator.utilities.enums.EstimateStatusEnum
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.domain.usecase.business.AddBusinessUseCase
import com.koinvois.generator.domain.usecase.business.GetBusinessUseCase
import com.koinvois.generator.domain.usecase.business.UpdateBusinessUseCase
import com.koinvois.generator.domain.usecase.client.GetAllClientsUseCase
import com.koinvois.generator.domain.usecase.estimate.*
import com.koinvois.generator.domain.usecase.item.GetAllItemsUseCase
import com.koinvois.generator.utilities.extensions.toStringFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.Date


@HiltViewModel
class EstimatesMainViewModel @Inject constructor(
    private val observeAllEstimatesUseCase: ObserveAllEstimatesUseCase,
    private val getEstimateByIdUseCase: GetEstimateByIdUseCase,
    private val addEstimateUseCase: AddEstimateUseCase,
    private val updateEstimateUseCase: UpdateEstimateUseCase,
    private val deleteEstimateUseCase: DeleteEstimateUseCase,
    private val getEstimateItemsByEstimateIdUseCase: GetEstimateItemsByEstimateIdUseCase,
    private val addEstimateItemUseCase: AddEstimateItemUseCase,
    private val updateEstimateItemUseCase: UpdateEstimateItemUseCase,
    private val deleteEstimateItemUseCase: DeleteEstimateItemUseCase,
    private val getEstimatePhotosByEstimateIdUseCase: GetEstimatePhotosByEstimateIdUseCase,
    private val addEstimatePhotoUseCase: AddEstimatePhotoUseCase,
    private val updateEstimatePhotoUseCase: UpdateEstimatePhotoUseCase,
    private val deleteEstimatePhotoUseCase: DeleteEstimatePhotoUseCase,
    private val getBusinessUseCase: GetBusinessUseCase,
    private val addBusinessUseCase: AddBusinessUseCase,
    private val updateBusinessUseCase: UpdateBusinessUseCase,
    private val getAllClientsUseCase: GetAllClientsUseCase,
    private val getAllItemsUseCase: GetAllItemsUseCase,
    private val appPreferences: AppPreferencesDataStore,
    private val draft: EstimateDraftState,
) : ViewModel() {

    val allEstimatesLive: LiveData<List<Estimate>> =
        observeAllEstimatesUseCase().map { list -> list.map { it.toEntity() } }.asLiveData()

    var estimatePrimaryId: Long?
        get() = draft.estimatePrimaryId
        set(value) { draft.estimatePrimaryId = value }
    var estimateNumber: Int?
        get() = draft.estimateNumber
        set(value) { draft.estimateNumber = value }
    var estimateDate: String?
        get() = draft.estimateDate
        set(value) { draft.estimateDate = value }
    var estimatePoNumber: String?
        get() = draft.estimatePoNumber
        set(value) { draft.estimatePoNumber = value }
    var businessUpdateModel: PersonalBusiness?
        get() = draft.businessUpdateModel
        set(value) { draft.businessUpdateModel = value }
    var selectedClient: Client?
        get() = draft.selectedClient
        set(value) { draft.selectedClient = value }
    var selectedItemsList: ArrayList<EstimateItem>?
        get() = draft.selectedItemsList
        set(value) { draft.selectedItemsList = value }
    var currentEstimateItem: EstimateItem?
        get() = draft.currentEstimateItem
        set(value) { draft.currentEstimateItem = value }
    var currentSelectedPhoto: EstimatePhoto?
        get() = draft.currentSelectedPhoto
        set(value) { draft.currentSelectedPhoto = value }
    var estimateSubTotal: Float?
        get() = draft.estimateSubTotal
        set(value) { draft.estimateSubTotal = value }
    var discountType: String?
        get() = draft.discountType
        set(value) { draft.discountType = value }
    var discountAmount: Float?
        get() = draft.discountAmount
        set(value) { draft.discountAmount = value }
    var discountTotalAmount: Float?
        get() = draft.discountTotalAmount
        set(value) { draft.discountTotalAmount = value }
    var taxType: String?
        get() = draft.taxType
        set(value) { draft.taxType = value }
    var taxLabel: String?
        get() = draft.taxLabel
        set(value) { draft.taxLabel = value }
    var taxRate: Float?
        get() = draft.taxRate
        set(value) { draft.taxRate = value }
    var taxInclusive: Boolean?
        get() = draft.taxInclusive
        set(value) { draft.taxInclusive = value }
    var estimateTotal: Float?
        get() = draft.estimateTotal
        set(value) { draft.estimateTotal = value }
    var photosForEstimate: ArrayList<EstimatePhoto>?
        get() = draft.photosForEstimate
        set(value) { draft.photosForEstimate = value }
    var signatureObj: Signature?
        get() = draft.signatureObj
        set(value) { draft.signatureObj = value }
    var estimateNotes: String?
        get() = draft.estimateNotes
        set(value) { draft.estimateNotes = value }
    var estimateStatus: String?
        get() = draft.estimateStatus
        set(value) { draft.estimateStatus = value }
    var allClients: ArrayList<Client>?
        get() = draft.allClients
        set(value) { draft.allClients = value }
    var allItems: ArrayList<Item>?
        get() = draft.allItems
        set(value) { draft.allItems = value }

    init {
        viewModelScope.launch(Dispatchers.Default) {
            allClients = getAllClientsUseCase().map { it.toEntity() } as ArrayList<Client>
            allItems = getAllItemsUseCase().map { it.toEntity() } as ArrayList<Item>
        }
    }

    suspend fun getBusiness() {
        businessUpdateModel = getBusinessUseCase(1).toEntity()
    }

    suspend fun updateBusiness(business: PersonalBusiness) {
        updateBusinessUseCase(business.toDomain())
    }

    suspend fun deleteEstimateItem(estimateItem: EstimateItem) {
        deleteEstimateItemUseCase(estimateItem.toDomain())
    }

    suspend fun insertEstimateItem(estimateItem: EstimateItem) {
        addEstimateItemUseCase(estimateItem.toDomain())
    }

    suspend fun updateEstimateItem(estimateItem: EstimateItem) {
        updateEstimateItemUseCase(estimateItem.toDomain())
    }

    suspend fun insertEstimatePhoto(estimatePhoto: EstimatePhoto) {
        addEstimatePhotoUseCase(estimatePhoto.toDomain())
    }

    suspend fun updateEstimatePhoto(estimatePhoto: EstimatePhoto) {
        updateEstimatePhotoUseCase(estimatePhoto.toDomain())
    }

    suspend fun deleteEstimatePhoto(estimatePhoto: EstimatePhoto) {
        deleteEstimatePhotoUseCase(estimatePhoto.toDomain())
    }

    suspend fun insertEstimate(estimate: Estimate) {
        addEstimateUseCase(estimate.toDomain())
    }

    suspend fun prepareNewEstimate() {
        clearViewModel()
        val tsLong = System.currentTimeMillis()
        val nextNumber = appPreferences.getEstimateNumber().first()
        
        estimatePrimaryId = tsLong.div(1000)
        estimateNumber = nextNumber
        estimateStatus = EstimateStatusEnum.OPEN.status
        estimateDate = Date().toStringFormat("dd/MM/yyyy")

        insertEstimate(
            Estimate(
                estimateId = (estimatePrimaryId?.toInt() ?: 0),
                estimateNumber = estimateNumber,
                estimateDate = estimateDate,
                estimateStatus = estimateStatus
            )
        )
    }

    suspend fun loadEstimateById(id: Int) {
        val estimate = getEstimateByIdUseCase(id)
        loadViewModelData(estimate.toEntity())
    }

    suspend fun updateEstimate(estimate: Estimate) {
        updateEstimateUseCase(estimate.toDomain())
    }

    suspend fun loadViewModelData(estimate: Estimate) {
        val fullEstimate = getEstimateByIdUseCase(estimate.estimateId).toEntity()
        estimatePrimaryId = fullEstimate.estimateId.toLong()
        estimateNumber = fullEstimate.estimateNumber
        estimateDate = fullEstimate.estimateDate
        estimateNotes = fullEstimate.estimateNotes
        estimatePoNumber = fullEstimate.estimatePoNumber
        estimateStatus = fullEstimate.estimateStatus
        estimateSubTotal = fullEstimate.estimateSubtotal
        estimateTotal = fullEstimate.estimateTotal
        businessUpdateModel = getBusinessUseCase(1).toEntity()
        selectedClient = fullEstimate.clientPK?.let {
            Client(
                it,
                fullEstimate.estimateClientName,
                fullEstimate.estimateClientEmail,
                fullEstimate.estimateClientMobile,
                fullEstimate.estimateClientPhone,
                fullEstimate.estimateClientFax,
                fullEstimate.estimateClientContact,
                fullEstimate.estimateClientAddress1,
                fullEstimate.estimateClientAddress2,
                fullEstimate.estimateClientAddress3
            )
        }
        selectedItemsList = getEstimateItemsByEstimateIdUseCase(fullEstimate.estimateId)
            .map { it.toEntity() } as? ArrayList<EstimateItem>
        photosForEstimate = getEstimatePhotosByEstimateIdUseCase(fullEstimate.estimateId)
            .map { it.toEntity() } as? ArrayList<EstimatePhoto>
        signatureObj =
            fullEstimate.estimateSignature?.let { Signature(it, fullEstimate.signatureDate ?: "") }

        discountType = fullEstimate.estimateDiscountType
        discountAmount = fullEstimate.estimateDiscountAmount
        // discountPercentage = invoice.invoiceDiscountPercentage
        taxType = fullEstimate.estimateTaxType
        taxLabel = fullEstimate.estimateTaxLabel
        currentEstimateItem = null
        taxRate = fullEstimate.estimateTaxRate
        taxInclusive = fullEstimate.estimateTaxInclusive

    }

    fun clearViewModel() {
        estimatePrimaryId = null
        estimateNumber = null
        estimateDate = null
        estimateNotes = null
        estimatePoNumber = null
        estimateStatus = null
        estimateSubTotal = null
        estimateTotal = null
        businessUpdateModel = null
        selectedClient = null
        selectedItemsList = null
        currentEstimateItem = null
        discountType = null
        discountAmount = null
        // discountPercentage = null
        discountTotalAmount = null
        taxType = null
        taxLabel = null
        taxRate = null
        taxInclusive = null
        photosForEstimate = null
        signatureObj = null
    }

    suspend fun deleteEstimate() {
        estimatePrimaryId?.let {
            deleteEstimateUseCase(it)
        }
    }
}