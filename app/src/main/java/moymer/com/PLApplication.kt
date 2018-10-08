package moymer.com

import android.app.Application
import android.content.Context

class PLApplication: Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: PLApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}