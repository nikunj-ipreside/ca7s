package com.music.ca7s.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import androidx.core.content.FileProvider
import com.music.ca7s.model.DownloadPlaylistModel
import java.io.*
import java.util.*


internal fun saveImage(context: Context, image: Bitmap, imagePath: String) {
    val savedImagePath: String
    val imageFile = File(imagePath)
    savedImagePath = imageFile.absolutePath
    try {
        val fOut = FileOutputStream(imageFile)
        image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
        fOut.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    galleryAddPic(context, savedImagePath)

}

private fun galleryAddPic(context: Context, imagePath: String) {
    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    val f = File(imagePath)
    Log.e("f : ", f.path)
    val contentUri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", f)
    mediaScanIntent.data = contentUri
    context.sendBroadcast(mediaScanIntent)
}

fun getSortedListForOffline(list: ArrayList<DownloadPlaylistModel>): List<DownloadPlaylistModel> {
    val sortedList = list.sortedWith(compareByDescending { it.createdAt })
    val newList = ArrayList<DownloadPlaylistModel>()
    for (i in 0 until sortedList.size) {
        newList.add(sortedList.get(i))
    }
    return newList

}


fun encode(mContext: Context, imageUri: String): String {
    val f = File(imageUri)
    Log.e("f : ", f.path)
    val contentUri = FileProvider.getUriForFile(mContext, mContext.applicationContext.packageName + ".provider", f)
    val input = mContext.getContentResolver().openInputStream(contentUri)
    val image = BitmapFactory.decodeStream(input, null, null)
    //encode image to base64 string
    val baos = ByteArrayOutputStream()
    image!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    var imageBytes = baos.toByteArray()
    val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)
    return imageString
}

private var APPDIRECTORY = "/Myuploads"
fun getFilePathFromUri(mContext: Context, uri: Uri): String {
    val fileName = getFileName(uri)
    var songDirectory = File(Environment.getExternalStorageDirectory().toString() + APPDIRECTORY)

    if (!songDirectory.exists()) {
        songDirectory.mkdirs()
    }

    if (!TextUtils.isEmpty(fileName)) {
//        val extension: String = uri.path!!.substring(uri.path!!.lastIndexOf("."))
        val copyFile = File(songDirectory.toString() + File.separator + fileName + ".mp3")
        Log.e("copyFIle : ", "" + copyFile.path)
        copy(mContext, uri, copyFile);
        return copyFile.absolutePath
    }

    return ""

}


fun getFileName(uri: Uri): String {
    if (uri == null) return null!!
    var fileName: String = ""

    var path: String = uri.path!!
    val cut: Int = path.lastIndexOf('/')

    if (cut != -1) {
        fileName = path.substring(cut + 1)
    }

    Log.e("file name : ", fileName + "")

    return fileName
}

fun copy(context: Context, srcUri: Uri?, dstFile: File?) {
    try {
        val inputStream = context.contentResolver.openInputStream(srcUri!!) ?: return
        val outputStream: OutputStream = FileOutputStream(dstFile)
        copystream(inputStream, outputStream)
        inputStream.close()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

private const val BUFFER_SIZE = 1024 * 2
@Throws(java.lang.Exception::class, IOException::class)
fun copystream(input: InputStream?, output: OutputStream?): Int {
    val buffer = ByteArray(BUFFER_SIZE)
    val `in` = BufferedInputStream(input, BUFFER_SIZE)
    val out = BufferedOutputStream(output, BUFFER_SIZE)
    var count = 0
    var n = 0
    try {
        while (`in`.read(buffer, 0, BUFFER_SIZE).also({ n = it }) != -1) {
            out.write(buffer, 0, n)
            count += n
        }
        out.flush()
    } finally {
        try {
            out.close()
        } catch (e: IOException) {
            Log.e(e.message, java.lang.String.valueOf(e))
        }
        try {
            `in`.close()
        } catch (e: IOException) {
            Log.e(e.message, java.lang.String.valueOf(e))
        }
    }
    return count
}

