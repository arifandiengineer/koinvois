package com.koinvois.generator.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


class Converters {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) return null
        
        var maxWidth = 800
        var maxHeight = 800
        
        var currentBitmap = if (bitmap.width > maxWidth || bitmap.height > maxHeight) {
            val scale = Math.min(maxWidth.toFloat() / bitmap.width, maxHeight.toFloat() / bitmap.height)
            Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale).toInt(), (bitmap.height * scale).toInt(), true)
        } else {
            bitmap
        }

        val outputStream = ByteArrayOutputStream()
        var quality = 70
        currentBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        var byteArray = outputStream.toByteArray()

        // CursorWindow limit is typically 2MB. We aim for < 1MB to be safe for a single row.
        val maxBlobSize = 1024 * 1024 // 1MB
        
        while (byteArray.size > maxBlobSize && quality > 10) {
            quality -= 10
            outputStream.reset()
            currentBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            byteArray = outputStream.toByteArray()
        }
        
        // If still too large, downscale further
        while (byteArray.size > maxBlobSize && (maxWidth > 200 || maxHeight > 200)) {
            maxWidth -= 200
            maxHeight -= 200
            val scale = Math.min(maxWidth.toFloat() / currentBitmap.width, maxHeight.toFloat() / currentBitmap.height)
            val nextBitmap = Bitmap.createScaledBitmap(currentBitmap, (currentBitmap.width * scale).toInt(), (currentBitmap.height * scale).toInt(), true)
            
            // Avoid recycling the original bitmap passed to the function
            if (currentBitmap != bitmap) {
                currentBitmap.recycle()
            }
            currentBitmap = nextBitmap
            
            quality = 60
            outputStream.reset()
            currentBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            byteArray = outputStream.toByteArray()
        }

        return byteArray
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        return try {
            byteArray?.size?.let { BitmapFactory.decodeByteArray(byteArray, 0, it) }
        } catch (e: Exception) {
            null
        }
    }

}