package moymer.com.data

import moymer.com.PLApplication

/**
 * Created by gabriellins @ moymer
 * on 20/07/18.
 */
abstract class SCallback<T> constructor(private val returnOnMain: Boolean = true) {

    abstract fun onSuccess(result: T)
    abstract fun onFailure(errorDescription: String)

    fun onSuccessThread(result: T) {
        if (returnOnMain) {
            PLApplication.runOnMain(Runnable { onSuccess(result) })
        } else {
            PLApplication.runOnBG(Runnable { onSuccess(result) })
        }
    }

    fun onFailureThread(errorDescription: String?) {
        val error =  errorDescription?.let { it } ?: ""

        if (returnOnMain) {
            PLApplication.runOnMain(Runnable { onFailure(error) })
        } else {
            PLApplication.runOnBG(Runnable { onFailure(error) })
        }
    }
}