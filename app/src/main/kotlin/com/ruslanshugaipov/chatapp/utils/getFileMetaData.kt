package com.ruslanshugaipov.chatapp.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.chat2desk.chat2desk_sdk.AttachedFile

fun getFileMetaData(context: Context, uri: Uri): AttachmentMeta {
    val contentResolver = context.contentResolver

    val mimeType: String? = contentResolver.getType(uri)
    val cursor: Cursor? = contentResolver.query(
        uri, null, null, null, null, null
    )

    var originalName: String? = ""
    var fileSize: Int? = 0

    cursor?.use {
        if (it.moveToFirst()) {
            try {
                originalName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                fileSize = it.getInt(it.getColumnIndexOrThrow(OpenableColumns.SIZE))
            } catch (e: Exception) {
                Log.e("Attachment", "Can't get file meta data", e)
            }
        }
    }

    return AttachmentMeta(uri, originalName ?: "", fileSize ?: 0, mimeType ?: "*/*")
}

data class AttachmentMeta(
    val contentUri: Uri,
    val originalName: String = "",
    val fileSize: Int = 0,
    val mimeType: String,
)

fun AttachmentMeta.toC2DAttachment(context: Context): AttachedFile = AttachedFile.fromUri(
    context,
    uri = this@toC2DAttachment.contentUri,
    originalName = this@toC2DAttachment.originalName,
    mimeType = this@toC2DAttachment.mimeType,
    fileSize = this@toC2DAttachment.fileSize
)
