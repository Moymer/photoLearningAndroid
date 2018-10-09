package moymer.com.cloud

import moymer.com.PLApplication

abstract class SCloudStorageCallback<String>(returnOnMain: Boolean?) {

    var returnOnMain = true

    init {
        if (returnOnMain != null) this.returnOnMain = returnOnMain
    }

    abstract fun uploadedFile(result: String, bytes: Long)
    abstract fun uploadedDir(success: Boolean)

    fun onUploadedFileThread(result: String?, bytes: Long) {
        if (returnOnMain) {
            PLApplication.runOnMain(Runnable { result?.let { uploadedFile(it, bytes) } })
        } else {
            PLApplication.runOnBG(Runnable { result?.let { uploadedFile(it, bytes) } })
        }
    }

    fun onUploadedDirThread(success: Boolean) {
        if (returnOnMain) {
            PLApplication.runOnMain(Runnable { uploadedDir(success) })
        } else {
            PLApplication.runOnBG(Runnable { uploadedDir(success) })
        }
    }
}