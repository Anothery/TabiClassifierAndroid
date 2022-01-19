package com.sudzusama.vkimageclassifier.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.util.*
import javax.inject.Inject
import android.os.Environment
import android.provider.OpenableColumns


class FileUtils @Inject constructor(private val context: Context) {
    fun contentFileToByteUtils(uri: Uri): ByteArray {
        return context.contentResolver.openInputStream(uri)?.readBytes()
            ?: throw Exception("Не удалось получить внешний файл")
    }

    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            cursor.use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    result =
                        cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }

    fun findMediaFiles(): List<String> {
        val fileList: MutableList<String> = ArrayList()
        val columns = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)
        val orderBy = MediaStore.Images.Media._ID

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            columns,
            null,
            null,
            orderBy
        )
        if (cursor != null) {
            val count = cursor.count
            val arrPath = arrayOfNulls<String>(count)
            for (i in 0 until count) {
                cursor.moveToPosition(i)
                val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                arrPath[i] = cursor.getString(dataColumnIndex)
                arrPath.getOrNull(i)?.let { fileList.add(it) }
            }
            cursor.close()
        }
        return fileList
    }

    fun checkFileExists(path: String) = File(path).exists()
}