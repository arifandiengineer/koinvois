package com.koinvois.generator.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.koinvois.generator.database.daos.*
import com.koinvois.generator.database.models.*

@Database(
    entities = [
        Client::class,
        Item::class,
        InvoiceItem::class,
        Invoice::class,
        InvoicePhoto::class,
        PersonalBusiness::class,
        EstimateItem::class,
        Estimate::class,
        EstimatePhoto::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun clientDao(): ClientDao
    abstract fun itemDao(): ItemDao
    abstract fun personalBusinessDao(): PersonalBusinessDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun invoicePhotoDao(): InvoicePhotoDao
    abstract fun invoiceItemDao(): InvoiceItemDao
    abstract fun estimateDao(): EstimateDao
    abstract fun estimatePhotoDao(): EstimatePhotoDao
    abstract fun estimateItemDao(): EstimateItemDao
}