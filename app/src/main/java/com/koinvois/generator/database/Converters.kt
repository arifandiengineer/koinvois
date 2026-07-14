package com.koinvois.generator.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


class Converters {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray {
        val resized = bitmap?.let { Bitmap.createScaledBitmap(it, (bitmap.width.times(0.8)).toInt(), (bitmap.height.times(0.8)).toInt(), true) }
        val outputStream = ByteArrayOutputStream()
        resized?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        return byteArray?.size?.let { BitmapFactory.decodeByteArray(byteArray, 0, it) }
    }

}