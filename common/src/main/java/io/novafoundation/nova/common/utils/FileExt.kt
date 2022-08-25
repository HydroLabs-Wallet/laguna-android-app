@file:Suppress("BlockingMethodInNonBlockingContext")

package io.novafoundation.nova.common.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * @param quality - integer between 1 and 100
 */
suspend fun File.write(bitmap: Bitmap, quality: Int = 100) {
    withContext(Dispatchers.IO) {
        val outputStream = FileOutputStream(this@write)

        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputStream.close()
    }
}
fun Context.newImageFile(fileName: String, extension: Bitmap.CompressFormat): File {
    val dirPath = this.getExternalFilesDir(null)!!.absolutePath
    val ext = extension.name.toLowerCase(Locale.ROOT)
    val dir = File(dirPath)
    if (!dir.exists() || !dir.isDirectory) {
        dir.mkdir()
    }

    return File(dir, "$fileName.$ext")
}

fun Context.getUriForFile(file: File): Uri? {
    return FileProvider.getUriForFile(
        this,
        "${packageName}.provider",
        file
    )
}

fun Context.storeBitmap(bitmap: Bitmap, uri: Uri) {
    contentResolver.openOutputStream(uri)?.run {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, this)
        close()
    }
}

fun Context.sendFileIntent(
    type: String,
    subject: String,
    uri: Uri,
    title: String?,
    text: String = ""
) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = type
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.putExtra(Intent.EXTRA_TEXT, text)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.resolveActivity(packageManager)?.run {
        startActivity(Intent.createChooser(intent, title))
    }
}
