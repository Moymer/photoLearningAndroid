package moymer.com.data

import com.moymer.spoken.SApplication

/**
 * Created by gabriellins @ moymer
 * on 20/07/18.
 */
abstract class SCallback<T> constructor(private val returnOnMain: Boolean = true) {

    abstract fun onSuccess(result: T)
    abstract fun onFailure(errorDescription: String)

    fun onSuccessThread(result: T) {
        if (returnOnMain) {
            SApplication.runOnMain(Runnable { onSuccess(result) })
        } else {
            SApplication.runOnBG(Runnable { onSuccess(result) })
        }
    }

    fun onFailureThread(errorDescription: String?) {
        val error =  errorDescription?.let { it } ?: ""

        if (returnOnMain) {
            SApplication.runOnMain(Runnable { onFailure(error) })
        } else {
            SApplication.runOnBG(Runnable { onFailure(error) })
        }
    }
}