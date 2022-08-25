package io.novafoundation.nova.feature_assets.presentation.asset_transactions.details.transfer

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class SaveTransactionTransferToDownloadsUseCase @Inject constructor(
    private val context: Context
) {

    operator fun invoke(data: String, hash: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            saveFileToExternalStorage(data, "${hash}.csv")
        } else {
            saveFileUsingMediaStore(context, data, "${hash}.csv")
        }
    }


    private fun saveFileToExternalStorage(data: String, fileName: String) {
        val target = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            fileName
        )
        target.writeText(data)

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveFileUsingMediaStore(context: Context, data: String, fileName: String) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            resolver.openOutputStream(uri).use { output ->
                output?.let { os ->
                    val fos = os as FileOutputStream
                    fos.write(data.toByteArray())
                }
            }
        }
    }
}
