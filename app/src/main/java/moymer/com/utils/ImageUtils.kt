package moymer.com.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ImageUtils {

    companion object {

        fun resizeAndCropUserImage(selectedImageUri: Uri, context: Context, path: String, pathToSaveFile: String?): String? {

            if (path.isEmpty()) {
                return selectedImageUri.toString()
            }

            val resizedImage = ImageUtils.cropToSquareAndResize(path, 600)
            val orientedBitmap = ImageUtils.rotateImage(resizedImage, path)
            val file = bitmapToFile(context, orientedBitmap, pathToSaveFile)

            val oldFile = File(path)
            if (oldFile.exists()) {
                oldFile.delete()
            }

            return file?.path
        }

        fun cropToSquareAndResize(src: String, heightSize: Int): Bitmap {

            val bitmap = setReducedImageSize(src, heightSize, heightSize)
            val width = bitmap.width
            val height = bitmap.height

            val size = Math.min(Math.min(width, height), heightSize)
            val x = (width - size) / 2
            val y = (height - size) / 2

            val cropImg: Bitmap
            cropImg = try {
                Bitmap.createBitmap(bitmap, x, y, size, size)
            } catch (e: OutOfMemoryError) {
                bitmap
            }

            return Bitmap.createScaledBitmap(cropImg, heightSize, heightSize, false)
        }

        fun setReducedImageSize(path: String, width: Int, height: Int): Bitmap {
            val bmOptions = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, bmOptions)
            val imageWidth = bmOptions.outWidth
            val imageHeight = bmOptions.outHeight

            bmOptions.inSampleSize = Math.min(imageWidth / width, imageHeight / height)
            bmOptions.inJustDecodeBounds = false

            return BitmapFactory.decodeFile(path, bmOptions)
        }

        fun rotateImage(bitmap: Bitmap, imagePath: String): Bitmap {
            var ei: ExifInterface? = null
            try {
                ei = ExifInterface(imagePath)
            } catch (e: IOException) {
                e.printStackTrace()
                return bitmap
            }

            val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED)

            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                    matrix.setRotate(180f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    matrix.setRotate(90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    matrix.setRotate(-90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
                ExifInterface.ORIENTATION_NORMAL, ExifInterface.ORIENTATION_UNDEFINED -> return bitmap
                else -> return bitmap
            }

            val oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            return oriented
        }

        fun bitmapToFile(context: Context, bitmap: Bitmap, pathToSaveFile: String?): File? {
            pathToSaveFile?.let {

                val currentTimeMillis = System.currentTimeMillis()
                
                val tmpDirPath = FileUtils.getDirectoryWithPath("/$pathToSaveFile/", context)
                val tmpDir = File(tmpDirPath)

                val split = pathToSaveFile.split("(?<=[/])".toRegex())
                val categoryLabel = split[1]
                val file = File(tmpDir, "$categoryLabel$currentTimeMillis.jpeg")
                try {
                    val os = BufferedOutputStream(FileOutputStream(file))
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    os.close()

                } catch (e: Exception) {
                    Log.e("Your Error Message", e.message)
                }
                return file
            }

            return null
        }

    }
}