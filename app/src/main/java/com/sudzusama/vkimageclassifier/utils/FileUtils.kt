package com.sudzusama.vkimageclassifier.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import com.sudzusama.vkimageclassifier.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import javax.inject.Inject


class FileUtils @Inject constructor(private val context: Context) {
    private val ALBUM_NAME = context.resources.getString(R.string.app_name)

    fun contentFileToByteUtils(uri: Uri): ByteArray {
        return context.contentResolver.openInputStream(uri)?.readBytes()
            ?: throw Exception("Не удалось получить внешний файл")
    }

    fun getImageExtensionFromUrl(url: String): String? {
        val pattern = "[.](jpg|jpeg|gif|png|webp)".toRegex()
        return pattern.find(url)?.value?.replace(".", "")
    }

    fun saveBitmapToCache(bmp: Bitmap, extension: String): String? {
        try {
            val file = File(
                context.cacheDir,
                "shared_image_${System.currentTimeMillis()}.$extension"
            )
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.close()
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
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
        val imgList = mutableListOf<String>()

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )


        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} ASC"

        val query = context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                imgList += contentUri.toString()
            }
        }
        return imgList
    }

    fun saveImageToExternalStorage(bitmap: Bitmap, extension: String): Uri {
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            ALBUM_NAME
        )
        storageDir.mkdirs()
        val newFileName = "${UUID.randomUUID().toString().replace("-", "")}.$extension"
        val file = File(storageDir, newFileName)

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }

    fun checkFileExists(path: String) = File(path).exists()
}