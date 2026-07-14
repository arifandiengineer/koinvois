package com.koinvois.generator.ui.invoices

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.koinvois.generator.data.mapper.toDomain
import com.koinvois.generator.data.mapper.toDomainItems
import com.koinvois.generator.data.mapper.toDomainPhotos
import com.koinvois.generator.data.mapper.toEntity
import com.koinvois.generator.data_models.InvoicePaymentsModel
import com.koinvois.generator.data_models.Signature
import com.koinvois.generator.database.models.*
import com.koinvois.generator.domain.calculation.DiscountCalculator
import com.koinvois.generator.domain.calculation.TaxCalculator
import com.koinvois.generator.domain.usecase.business.AddBusinessUseCase
import com.koinvois.generator.domain.usecase.business.GetBusinessUseCase
import com.koinvois.generator.domain.usecase.business.UpdateBusinessUseCase
import com.koinvois.generator.domain.usecase.client.GetAllClientsUseCase
import com.koinvois.generator.domain.usecase.invoice.AddInvoiceItemUseCase
import com.koinvois.generator.domain.usecase.invoice.AddInvoicePhotoUseCase
import com.koinvois.generator.domain.usecase.invoice.AddInvoiceUseCase
import com.koinvois.generator.domain.usecase.invoice.DeleteInvoiceItemUseCase
import com.koinvois.generator.domain.usecase.invoice.DeleteInvoicePhotoUseCase
import com.koinvois.generator.domain.usecase.invoice.DeleteInvoiceUseCase
import com.koinvois.generator.domain.usecase.invoice.GetInvoiceByIdUseCase
import com.koinvois.generator.domain.usecase.invoice.GetInvoiceItemsByInvoiceIdUseCase
import com.koinvois.generator.domain.usecase.invoice.GetInvoicePhotosByInvoiceIdUseCase
import com.koinvois.generator.domain.usecase.invoice.ObserveAllInvoicesUseCase
import com.koinvois.generator.domain.usecase.invoice.UpdateInvoiceItemUseCase
import com.koinvois.generator.domain.usecase.invoice.UpdateInvoicePhotoUseCase
import com.koinvois.generator.domain.usecase.invoice.UpdateInvoiceUseCase
import com.koinvois.generator.domain.usecase.item.GetAllItemsUseCase
import com.koinvois.generator.core.data.preferences.AppPreferencesDataStore
import com.koinvois.generator.utilities.enums.InvoiceStatusEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceMainViewModel @Inject constructor(
    private val observeAllInvoicesUseCase: ObserveAllInvoicesUseCase,
    private val getInvoiceByIdUseCase: GetInvoiceByIdUseCase,
    private val addInvoiceUseCase: AddInvoiceUseCase,
    private val updateInvoiceUseCase: UpdateInvoiceUseCase,
    private val deleteInvoiceUseCase: DeleteInvoiceUseCase,
    private val getInvoiceItemsByInvoiceIdUseCase: GetInvoiceItemsByInvoiceIdUseCase,
    private val addInvoiceItemUseCase: AddInvoiceItemUseCase,
    private val updateInvoiceItemUseCase: UpdateInvoiceItemUseCase,
    private val deleteInvoiceItemUseCase: DeleteInvoiceItemUseCase,
    private val getInvoicePhotosByInvoiceIdUseCase: GetInvoicePhotosByInvoiceIdUseCase,
    private val addInvoicePhotoUseCase: AddInvoicePhotoUseCase,
    private val updateInvoicePhotoUseCase: UpdateInvoicePhotoUseCase,
    private val deleteInvoicePhotoUseCase: DeleteInvoicePhotoUseCase,
    private val getBusinessUseCase: GetBusinessUseCase,
    private val addBusinessUseCase: AddBusinessUseCase,
    private val updateBusinessUseCase: UpdateBusinessUseCase,
    private val getAllClientsUseCase: GetAllClientsUseCase,
    private val getAllItemsUseCase: GetAllItemsUseCase,
    private val appPreferences: AppPreferencesDataStore,
    private val draft: InvoiceDraftState,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val allInvoicesLive: LiveData<List<Invoice>> =
        combine(observeAllInvoicesUseCase(), _searchQuery) { list, query ->
            val entityList = list.map { it.toEntity() }
            if (query.isEmpty()) entityList
            else entityList.filter {
                it.invoiceClientName?.contains(query, ignoreCase = true) == true ||
                        it.invoiceId.toString().contains(query)
            }
        }.asLiveData()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    var invoicePrimaryId: Long?
        get() = draft.invoicePrimaryId
        set(value) { draft.invoicePrimaryId = value }
    var invoiceNumber: Int?
        get() = draft.invoiceNumber
        set(value) { draft.invoiceNumber = value }
    var invoiceDate: String?
        get() = draft.invoiceDate
        set(value) { draft.invoiceDate = value }
    var invoiceTerms: String?
        get() = draft.invoiceTerms
        set(value) { draft.invoiceTerms = value }
    var invoiceDueDate: String?
        get() = draft.invoiceDueDate
        set(value) { draft.invoiceDueDate = value }
    var invoicePoNumber: String?
        get() = draft.invoicePoNumber
        set(value) { draft.invoicePoNumber = value }
    var businessUpdateModel: PersonalBusiness?
        get() = draft.businessUpdateModel
        set(value) { draft.businessUpdateModel = value }
    var selectedClient: Client?
        get() = draft.selectedClient
        set(value) { draft.selectedClient = value }
    var selectedItemsList: ArrayList<InvoiceItem>?
        get() = draft.selectedItemsList
        set(value) { draft.selectedItemsList = value }
    var currentInvoiceItem: InvoiceItem?
        get() = draft.currentInvoiceItem
        set(value) { draft.currentInvoiceItem = value }
    var currentSelectedPhoto: InvoicePhoto?
        get() = draft.currentSelectedPhoto
        set(value) { draft.currentSelectedPhoto = value }
    var invoiceSubTotal: Float?
        get() = draft.invoiceSubTotal
        set(value) { draft.invoiceSubTotal = value }

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

    var invoiceTotal: Float
        get() = draft.invoiceTotal
        set(value) { draft.invoiceTotal = value }

    var taxAmount: Float
        get() = draft.taxAmount
        set(value) { draft.taxAmount = value }

    val invoicePaymentsState: StateFlow<List<InvoicePaymentsModel>> = draft.invoicePaymentsFlow.asStateFlow()

    val totalPaidAmount: StateFlow<Float> = draft.invoicePaymentsFlow.map { list ->
        list.sumOf { (it.amount ?: 0f).toDouble() }.toFloat()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    val balanceDue: StateFlow<Float> = combine(draft.invoicePaymentsFlow, draft.invoiceTotalFlow) { payments, total ->
        val totalPaid = payments.sumOf { (it.amount ?: 0f).toDouble() }.toFloat()
        total - totalPaid
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    fun addPayment(payment: InvoicePaymentsModel) {
        draft.invoicePaymentsFlow.update { currentList ->
            currentList + payment
        }
    }

    var photosForInvoice: ArrayList<InvoicePhoto>?
        get() = draft.photosForInvoice
        set(value) { draft.photosForInvoice = value }
    var invoicePaymentInstructions: String?
        get() = draft.invoicePaymentInstructions
        set(value) { draft.invoicePaymentInstructions = value }
    var signatureObj: Signature?
        get() = draft.signatureObj
        set(value) { draft.signatureObj = value }
    var invoiceNotes: String?
        get() = draft.invoiceNotes
        set(value) { draft.invoiceNotes = value }
    var invoiceStatus: String?
        get() = draft.invoiceStatus
        set(value) { draft.invoiceStatus = value }

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

    suspend fun getAllClients(): ArrayList<Client> {
        return getAllClientsUseCase().map { it.toEntity() } as ArrayList<Client>
    }

    suspend fun addBusiness(business: PersonalBusiness) {
        addBusinessUseCase(business.toDomain())
    }

    suspend fun updateBusiness(business: PersonalBusiness) {
        updateBusinessUseCase(business.toDomain())
    }

    suspend fun getBusiness() {
        businessUpdateModel = getBusinessUseCase(1).toEntity()
    }

    suspend fun insertInvoice(invoice: Invoice) {
        addInvoiceUseCase(invoice.toDomain())
    }

    suspend fun updateInvoice(invoice: Invoice) {
        updateInvoiceUseCase(invoice.toDomain())
    }

    suspend fun insertInvoiceItem(invoiceItem: InvoiceItem) {
        addInvoiceItemUseCase(invoiceItem.toDomain())
    }

    suspend fun updateInvoiceItem(invoiceItem: InvoiceItem) {
        updateInvoiceItemUseCase(invoiceItem.toDomain())
    }

    suspend fun deleteInvoiceItem(invoice: InvoiceItem) {
        deleteInvoiceItemUseCase(invoice.toDomain())
    }

    suspend fun insertInvoicePhoto(invoicePhoto: InvoicePhoto) {
        addInvoicePhotoUseCase(invoicePhoto.toDomain())
    }

    suspend fun updateInvoicePhoto(invoicePhoto: InvoicePhoto) {
        updateInvoicePhotoUseCase(invoicePhoto.toDomain())
    }

    suspend fun deleteInvoicePhoto(invoicePhoto: InvoicePhoto) {
        deleteInvoicePhotoUseCase(invoicePhoto.toDomain())
    }

    fun clearViewModel() {
//        invoicePrimaryId = null
        invoiceNumber = null
        invoiceDate = null
        invoiceDueDate = null
        invoiceNotes = null
        invoicePaymentInstructions = null
        invoicePoNumber = null
        invoiceStatus = null
        invoiceSubTotal = null
        invoiceTerms = null
        invoiceTotal = 0f
        businessUpdateModel = null
        selectedClient = null
        selectedItemsList = null
        currentInvoiceItem = null
        discountType = null
        discountAmount = null
        // discountPercentage = null
        discountTotalAmount = null
        taxType = null
        taxLabel = null
        taxRate = null
        taxInclusive = null
        photosForInvoice = null
        signatureObj = null
        draft.invoicePaymentsFlow.value = emptyList()
    }

    suspend fun loadViewModelData(invoice: Invoice) {
        val fullInvoice = getInvoiceByIdUseCase(invoice.invoiceId)
        invoicePrimaryId = fullInvoice.invoiceId.toLong()
        invoiceNumber = fullInvoice.invoiceNumber
        invoiceDate = fullInvoice.invoiceDate
        invoiceDueDate = fullInvoice.invoiceDueDate
        invoiceNotes = fullInvoice.invoiceNotes
        invoicePaymentInstructions = fullInvoice.invoicePaymentInstruction
        invoicePoNumber = fullInvoice.invoicePoNumber
        invoiceStatus = fullInvoice.invoiceStatus
        invoiceSubTotal = fullInvoice.invoiceSubtotal
        invoiceTerms = fullInvoice.invoiceTerms
        invoiceTotal = fullInvoice.invoiceTotal ?: 0f
        businessUpdateModel = getBusinessUseCase(1).toEntity()
        selectedClient = fullInvoice.clientPK?.let {
            Client(
                it,
                fullInvoice.invoiceClientName,
                fullInvoice.invoiceClientEmail,
                fullInvoice.invoiceClientMobile,
                fullInvoice.invoiceClientPhone,
                fullInvoice.invoiceClientFax,
                fullInvoice.invoiceClientContact,
                fullInvoice.invoiceClientAddress1,
                fullInvoice.invoiceClientAddress2,
                fullInvoice.invoiceClientAddress3
            )
        }
        selectedItemsList = getInvoiceItemsByInvoiceIdUseCase(fullInvoice.invoiceId)
            .map { it.toEntity() } as? ArrayList<InvoiceItem>
        photosForInvoice = getInvoicePhotosByInvoiceIdUseCase(fullInvoice.invoiceId)
            .map { it.toEntity() } as? ArrayList<InvoicePhoto>
        signatureObj = fullInvoice.invoiceSignature?.let { Signature(it, fullInvoice.signatureDate ?: "") }

        discountType = fullInvoice.invoiceDiscountType
        discountAmount = fullInvoice.invoiceDiscountAmount
        // discountPercentage = invoice.invoiceDiscountPercentage
        taxType = fullInvoice.invoiceTaxType
        taxLabel = fullInvoice.invoiceTaxLabel
        currentInvoiceItem = null
        taxRate = fullInvoice.invoiceTaxRate
        taxInclusive = fullInvoice.invoiceTaxInclusive

        // TODO: Load payments from database if available
        draft.invoicePaymentsFlow.value = emptyList()

    }

    fun recalculateInvoiceSubTotal() {
        invoiceSubTotal = selectedItemsList?.sumOf { (it.itemTotal ?: 0f).toDouble() }?.toFloat() ?: 0f
    }

    fun recalculateInvoiceTotal() {
        discountTotalAmount = DiscountCalculator.calculateDiscountTotal(
            subTotal = invoiceSubTotal,
            discountType = discountType,
            discountAmount = discountAmount
        )
        val subTotalAfterDiscount = (invoiceSubTotal ?: 0f) - (discountTotalAmount ?: 0f)
        val taxableItems = selectedItemsList
            ?.filter { it.invoiceItemTaxable == true }
            ?.map { it.itemTaxRate to it.itemTotal }
            ?: emptyList()
        val taxResult = TaxCalculator.calculateTax(
            taxType = taxType,
            taxRate = taxRate,
            taxInclusive = taxInclusive,
            subTotalAfterDiscount = subTotalAfterDiscount,
            taxableItems = taxableItems
        )
        taxAmount = taxResult.taxAmount
        invoiceTotal = subTotalAfterDiscount + taxResult.totalAdjustment
    }

    suspend fun loadInvoiceById(invoiceId: Int) {
        val invoice = getInvoiceByIdUseCase(invoiceId)
        loadViewModelData(invoice.toEntity())
    }

    suspend fun prepareNewInvoice() {
        val tsLong = System.currentTimeMillis()
        val invoiceNumberFromPref = appPreferences.getInvoiceNumber().first()

        invoicePrimaryId = tsLong.div(1000)
        invoiceNumber = invoiceNumberFromPref
        invoiceStatus = InvoiceStatusEnum.UN_PAID.status
        invoiceTotal = 0f

        insertInvoice(
            Invoice(
                invoiceId = (tsLong.div(1000)).toInt(),
                invoiceNumber = invoiceNumberFromPref,
                invoiceDate = null,
                invoiceTerms = null,
                invoiceDueDate = null,
                invoicePoNumber = null,
                invoiceSubtotal = null,
                invoiceDiscountType = null,
                invoiceDiscountAmount = null,
                discountTotalAmount = null,
                invoiceTaxType = null,
                invoiceTaxLabel = null,
                invoiceTaxRate = null,
                invoiceTaxInclusive = null,
                invoiceTotal = null,
                invoicePaymentInstruction = null,
                invoiceSignature = null,
                signatureDate = null,
                invoiceNotes = null,
                invoiceStatus = InvoiceStatusEnum.UN_PAID.status,
                clientPK = null,
                invoiceClientName = null,
                invoiceClientEmail = null,
                invoiceClientMobile = null,
                invoiceClientPhone = null,
                invoiceClientFax = null,
                invoiceClientContact = null,
                invoiceClientAddress1 = null,
                invoiceClientAddress2 = null,
                invoiceClientAddress3 = null
            )
        )
    }

    suspend fun deleteInvoice() {
        invoicePrimaryId?.let {
            deleteInvoiceUseCase(it)
        }
    }

    fun deleteInvoiceById(invoiceId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            deleteInvoiceUseCase(invoiceId.toLong())
        }
    }

}
