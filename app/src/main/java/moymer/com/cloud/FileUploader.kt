package moymer.com.cloud

import moymer.com.cloud.google.CloudStorage
import java.io.File


object FileUploader : FileUploadSource {

    private var storageSource: FileUploadSource = CloudStorage()

    enum class CloudSource {
        AMAZON, GOOGLE
    }

    private val source = CloudSource.GOOGLE


    override fun uploadFile(key: String, filePath: String, contentType: FileContentType, deleteOnFinish: Boolean?, callback: SCloudStorageCallback<String>) {
        storageSource.uploadFile(key, filePath, contentType, deleteOnFinish, callback)
    }

    override fun uploadDir(keyBase: String, dir: File, callback: SCloudStorageCallback<String>) {
        storageSource.uploadDir(keyBase, dir, callback)
    }

    override fun uploadDir(keyBase: String, dir: File, fileContentTypeToUpload: FileContentType?, callback: SCloudStorageCallback<String>) {
        storageSource.uploadDir(keyBase, dir, fileContentTypeToUpload, callback)
    }

    override fun getUrlBase(): String {
        return storageSource.getUrlBase()
    }
}