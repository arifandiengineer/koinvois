package com.koinvois.generator.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.koinvois.generator.database.models.Estimate
import kotlinx.coroutines.flow.Flow

@Dao
interface EstimateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstimate(estimate: Estimate)

    @Query("SELECT estimateId, estimate_number, estimate_date, estimate_po_number, estimate_subtotal, estimate_discount_type, estimate_discount_amount, estimate_discount_total_amount, estimate_tax_type, estimate_tax_label, estimate_tax_rate, estimate_tax_inclusive, estimate_total, NULL as estimate_signature, signature_date, estimate_notes, estimate_status, client_pk, client_name, client_email, client_mobile, client_phone, client_fax, client_contact, client_address1, client_address2, client_address3 FROM estimate")
    suspend fun getAllEstimates(): List<Estimate>

    @Query("SELECT estimateId, estimate_number, estimate_date, estimate_po_number, estimate_subtotal, estimate_discount_type, estimate_discount_amount, estimate_discount_total_amount, estimate_tax_type, estimate_tax_label, estimate_tax_rate, estimate_tax_inclusive, estimate_total, NULL as estimate_signature, signature_date, estimate_notes, estimate_status, client_pk, client_name, client_email, client_mobile, client_phone, client_fax, client_contact, client_address1, client_address2, client_address3 FROM estimate")
    fun observeAllEstimates(): Flow<List<Estimate>>

    @Query("SELECT estimateId, estimate_number, estimate_date, estimate_po_number, estimate_subtotal, estimate_discount_type, estimate_discount_amount, estimate_discount_total_amount, estimate_tax_type, estimate_tax_label, estimate_tax_rate, estimate_tax_inclusive, estimate_total, NULL as estimate_signature, signature_date, estimate_notes, estimate_status, client_pk, client_name, client_email, client_mobile, client_phone, client_fax, client_contact, client_address1, client_address2, client_address3 FROM estimate")
    fun getAllEstimatesLive(): LiveData<List<Estimate>>

    @Query("SELECT * FROM estimate WHERE estimateId = :estimateId")
    suspend fun getEstimateById(estimateId: Int): Estimate

    @Update
    suspend fun updateEstimate(vararg Estimate: Estimate)

    @Delete
    suspend fun deleteEstimate(vararg estimate: Estimate)

    @Query("Delete from estimate where estimateId = :id")
    suspend fun deleteEstimateWithID(vararg id: Long)
}