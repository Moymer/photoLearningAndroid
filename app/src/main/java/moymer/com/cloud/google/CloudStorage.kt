package moymer.com.cloud.google

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import moymer.com.cloud.FileContentType
import moymer.com.cloud.FileUploadSource
import moymer.com.cloud.SCloudStorageCallback
import moymer.com.utils.Constants
import java.io.File
import java.util.*

class CloudStorage : FileUploadSource {

    private var TAG = "UPLOAD_RESPONSE_PROCESS"

    private var storage: FirebaseStorage = FirebaseStorage.getInstance(Constants.sInterval)

    private val uploadTimeout: Long = 20000
    private val operationTimeout: Long = 10000
    private val downloadTimeout: Long = 10000
    private val FIREBASE_META_RULE_KEY = "demodev"
    private val FIREBASE_META_RULE_PSWD = "342flwejfo2_1432423lkdsrkotihy32"

    init {
        storage.maxUploadRetryTimeMillis = uploadTimeout
        storage.maxOperationRetryTimeMillis = operationTimeout
        storage.maxDownloadRetryTimeMillis = downloadTimeout
    }

    override fun getUrlBase(): String {
        return Constants.sIntervalPublicAddress
    }

    override fun uploadFile(key: String, filePath: String, contentType: FileContentType, deleteOnFinish: Boolean?, callback: SCloudStorageCallback<String>) {
        uploadFile(key, filePath, contentType, callback, deleteOnFinish)
    }

    override fun uploadDir(keyBase: String, dir: File, callback: SCloudStorageCallback<String>) {
        uploadDir(keyBase, dir, null, callback)
    }

    override fun uploadDir(keyBase: String, dir: File, fileContentTypeToUpload: FileContentType?, callback: SCloudStorageCallback<String>) {
        val uploadTasks = ArrayList<UploadTask>()
        val fileCallback = object : SCloudStorageCallback<String>(false) {

            var totalUploaded: Int = 0
            var totalBytesUploaded: Long = 0L
            var hasRespondedError = false

            override fun uploadedFile(result: String, bytes: Long) {
                synchronized(this) {
                    totalUploaded++
                    totalBytesUploaded += bytes
//                    EventBus.getDefault().post(SEvents.UploadProgressEvent(totalBytesUploaded, false))
                    if (totalUploaded == uploadTasks.size && !hasRespondedError) {
                        //finished all
                        callback.onUploadedDirThread(true)
                    }
                }
            }
        }

        //execute tasks
        val totalBytes = uploadDirCloudStorageRec(keyBase, dir, fileContentTypeToUpload, uploadTasks, fileCallback)
//        EventBus.getDefault().post(SEvents.UploadProgressEvent(totalBytes, true))
        if (uploadTasks.isEmpty()) {
            callback.onUploadedDirThread(true)
        }
    }

    private fun uploadDirCloudStorageRec(keyBase: String, dir: File, fileContentTypeToUpload: FileContentType?, uploadTasks: MutableList<UploadTask>, fileCallback: SCloudStorageCallback<String>): Long {

        var totalBytes: Long = 0
        val fileList = dir.listFiles()

        if (fileList != null) {

            for (fileToUpload in fileList) {
                if (fileToUpload.isDirectory) {
                    totalBytes += uploadDirCloudStorageRec(keyBase, fileToUpload, fileContentTypeToUpload, uploadTasks, fileCallback)
                } else {
                    val parts = fileToUpload.absolutePath.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val filename = parts[parts.size - 1]
                    val extension = filename.substring(filename.lastIndexOf("."))
                    val contentType = FileContentType.fromFileExtesion(extension)

                    if (fileContentTypeToUpload == null || contentType === fileContentTypeToUpload) {
                        totalBytes += fileToUpload.length()

                        val substringAfter = fileToUpload.absolutePath.substringAfter("files/")
                        val task = uploadFile (substringAfter, fileToUpload.absolutePath, contentType, fileCallback, true)
                        uploadTasks.add(task)
                    }
                }
            }
        }

        return totalBytes
    }

    private fun uploadFile(key: String, filePath: String, contentType: FileContentType, listener: SCloudStorageCallback<String>, deleteOnFinish: Boolean?): UploadTask {

        val file = File(filePath)
        val fileUri = Uri.fromFile(file)
        val sRef = storage.getReference(key)

        val metadata = StorageMetadata.Builder()
                .setContentType(contentType.toContentType())
                .setCustomMetadata("Content-Legth", file.length().toString())
                .setCustomMetadata(FIREBASE_META_RULE_KEY, FIREBASE_META_RULE_PSWD)
                .build()

        val uploadTask = sRef.putFile(fileUri, metadata)


        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener { _ -> listener.onUploadedFileThread("", 0L) }.addOnSuccessListener { _ ->
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            //Uri downloadUrl = taskSnapshot.getDownloadUrl();

            val url = Constants.sIntervalPublicAddress +
                    "/" +
                    key

            val bytes = file.length()
            if (deleteOnFinish!!) {
                file.delete()
            }

            listener.onUploadedFileThread(url, bytes)
        }

        uploadTask.addOnPausedListener {
            Log.d(TAG, "paused")
        }

        return uploadTask
    }
}