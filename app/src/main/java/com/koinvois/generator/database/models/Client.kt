package com.koinvois.generator.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Client(
    @PrimaryKey(autoGenerate = true) val clientId: Int,
    @ColumnInfo(name = "client_name") val clientName: String?,
    @ColumnInfo(name = "client_email") val clientEmail: String?,
    @ColumnInfo(name = "client_mobile") val clientMobile: Int?,
    @ColumnInfo(name = "client_phone") val clientPhone: Int?,
    @ColumnInfo(name = "client_fax") val clientFax: Int?,
    @ColumnInfo(name = "client_contact") val clientContact: String?,
    @ColumnInfo(name = "client_address1") val clientAddress1: String?,
    @ColumnInfo(name = "client_address2") val clientAddress2: String?,
    @ColumnInfo(name = "client_address3") val clientAddress3: String?,
)
