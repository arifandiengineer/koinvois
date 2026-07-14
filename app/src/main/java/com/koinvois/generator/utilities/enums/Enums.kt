package com.koinvois.generator.utilities.enums

enum class ItemDiscountTypeEnum(val discountTypeSmall: String, val discountTypeCapital: String) {
    PERCENTAGE("percentage", "Percentage"),
    FLAT_AMOUNT("flat amount", "Flat Amount"),
    NO_DISCOUNT("no discount", "No Discount")
}

enum class ItemTaxTypeEnum(val taxTypeSmall: String, val taxTypeCapital: String) {
    NONE("none", "None"),
    ON_THE_TOTAL("on the total", "On The Total"),
    DEDUCTED("deducted", "Deducted"),
    PER_ITEM("per item", "Per Item")
}

enum class DBEnum(val entryType: String) {
    NEW("new"),
    OLD("old")
}

enum class DateTypeEnum(val dateType: String) {
    INVOICE_DATE("invoiceDate"),
    INVOICE_DUE_DATE("invoiceDueDate"),
    PAYMENT_DATE("paymentDate")
}

enum class PaymentMethodEnum(val method: String) {
    CASH("Cash"),
    CHEQUE("Cheque"),
    BANK("Bank"),
    CREDIT_CARD("Credit Card"),
    PAYPAL("PayPal"),
    OTHER("Other")
}

enum class InvoiceStatusEnum(val status: String) {
    PAID("paid"),
    UN_PAID("unpaid")
}

enum class EstimateStatusEnum(val status: String) {
    OPEN("open"),
    CLOSED("closed")
}

enum class InvoiceTermsEnums(val stringValue: String) {
    NONE("None"),
    DUE_ON_RECEIPT("Due on receipt"),
    NEXT_DAY("Next Day"),
    TWO_DAYS("2 Days"),
    ONE_WEEK("1 Week"),
    CUSTOM("Custom")
}

enum class PinTypeEnum(val type: String) {
    NEW("new"),
    RECOVERY("recovery")
}
