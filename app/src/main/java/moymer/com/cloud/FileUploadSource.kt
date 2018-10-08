package moymer.com.cloud

import java.io.File

interface FileUploadSource {

    fun uploadFile(key: String, filePath: String, contentType: FileContentType, deleteOnFinish: Boolean?, callback: SCloudStorageCallback<String>)
    fun uploadDir(keyBase: String, dir: File, callback: SCloudStorageCallback<String>)
    fun uploadDir(keyBase: String, dir: File, fileContentTypeToUpload: FileContentType?, callback: SCloudStorageCallback<String>)
    fun getUrlBase(): String
}