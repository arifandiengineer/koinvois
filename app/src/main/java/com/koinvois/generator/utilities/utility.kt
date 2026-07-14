package com.koinvois.generator.utilities

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

object utility {

    fun encodeToBase64(image: Bitmap?): String? {
        val baos = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}