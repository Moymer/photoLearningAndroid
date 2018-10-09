package moymer.com.cloud

import java.io.File

interface FileUploadSource {

    fun uploadFile(key: String, filePath: String, contentType: FileContentType, deleteOnFinish: Boolean?, callback: PLCloudStorageCallback<String>)
    fun uploadDir(keyBase: String, dir: File, callback: PLCloudStorageCallback<String>)
    fun uploadDir(keyBase: String, dir: File, fileContentTypeToUpload: FileContentType?, callback: PLCloudStorageCallback<String>)
    fun getUrlBase(): String
}