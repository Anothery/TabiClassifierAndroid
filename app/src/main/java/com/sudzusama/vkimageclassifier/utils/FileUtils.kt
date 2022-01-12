package com.sudzusama.vkimageclassifier.utils

import android.content.Context
import android.provider.MediaStore
import java.util.*
import javax.inject.Inject


class FileUtils @Inject constructor(private val context: Context) {
    fun findMediaFiles(): List<String?> {
        val fileList: MutableList<String?> = ArrayList()
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
                fileList.add(arrPath[i])
            }
            cursor.close()
        }
        return fileList
    }
}