package moymer.com.learnedwords

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import moymer.com.photolearning.R
import moymer.com.utils.ActivityUtils

class PLLearnedWordsVisionActivity: AppCompatActivity() {

    var injectedFragment: PLLearnedWordsVisionFragment = PLLearnedWordsVisionFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var learnedWordsFragment: PLLearnedWordsVisionFragment? = supportFragmentManager.findFragmentById(R.id.fragment_container) as PLLearnedWordsVisionFragment?

        if (learnedWordsFragment == null) {
            learnedWordsFragment = injectedFragment
            ActivityUtils.addFragmentToActivity(supportFragmentManager, learnedWordsFragment, R.id.fragment_container)
        }

        injectedFragment.arguments = intent?.extras

    }
}
