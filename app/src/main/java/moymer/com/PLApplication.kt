package moymer.com

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.support.multidex.MultiDex
import com.google.firebase.FirebaseApp

class PLApplication: Application() {

    init {
        instance = this
    }


    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this.baseContext)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {

        private var instance: PLApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }

        fun runOnMain(r: Runnable) {
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post(r)
        }

        fun runOnBG(r: Runnable) {
            AsyncTask.execute(r)
        }
    }
}