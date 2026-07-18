package com.koinvois.generator.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koinvois.generator.database.daos.InvoiceDao
import com.koinvois.generator.database.models.Invoice
import com.koinvois.generator.utilities.enums.InvoiceStatusEnum
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InvoiceDaoTest {
    private lateinit var db: AppDataBase
    private lateinit var dao: InvoiceDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java).build()
        dao = db.invoiceDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndQueryInvoice() = runBlocking {
        val invoice = Invoice(
            invoiceId = 1,
            invoiceNumber = 101,
            invoiceDate = "2023-10-27",
            invoiceTerms = null,
            invoiceDueDate = null,
            invoicePoNumber = null,
            invoiceSubtotal = 1000f,
            invoiceDiscountType = null,
            invoiceDiscountAmount = null,
            discountTotalAmount = null,
            invoiceTaxType = null,
            invoiceTaxLabel = null,
            invoiceTaxRate = null,
            invoiceTaxInclusive = null,
            invoiceTotal = 1000f,
            invoicePaymentInstruction = null,
            invoiceSignature = null,
            signatureDate = null,
            invoiceNotes = null,
            invoiceStatus = InvoiceStatusEnum.UN_PAID.status,
            clientPK = 1,
            invoiceClientName = "John Doe",
            invoiceClientEmail = null,
            invoiceClientMobile = null,
            invoiceClientPhone = null,
            invoiceClientFax = null,
            invoiceClientContact = null,
            invoiceClientAddress1 = null,
            invoiceClientAddress2 = null,
            invoiceClientAddress3 = null
        )
        dao.insertInvoice(invoice)
        val loaded = dao.getInvoiceById(1)
        assertEquals(101, loaded.invoiceNumber)
        assertEquals("John Doe", loaded.invoiceClientName)
    }

    @Test
    fun getAllPaidInvoices() = runBlocking {
        val paidInvoice = Invoice(1, 1, "date", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PAID", null, null, null, null, null, null, null, null, null, null)
        val unpaidInvoice = Invoice(2, 2, "date", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "UNPAID", null, null, null, null, null, null, null, null, null, null)
        
        dao.insertInvoice(paidInvoice)
        dao.insertInvoice(unpaidInvoice)
        
        val paidOnes = dao.getAllPaidInvoices()
        assertEquals(1, paidOnes.size)
        assertEquals("PAID", paidOnes[0].invoiceStatus)
    }
}
