package moymer.com.learnedwords

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import moymer.com.photolearning.R
import moymer.com.utils.ActivityUtils

class SLearnedWordsVisionActivity: AppCompatActivity() {

    var injectedFragment: SLearnedWordsVisionFragment = SLearnedWordsVisionFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var learnedWordsFragment: SLearnedWordsVisionFragment? = supportFragmentManager.findFragmentById(R.id.fragment_container) as SLearnedWordsVisionFragment?

        if (learnedWordsFragment == null) {
            learnedWordsFragment = injectedFragment
            ActivityUtils.addFragmentToActivity(supportFragmentManager, learnedWordsFragment, R.id.fragment_container)
        }

        injectedFragment.arguments = intent?.extras

    }
}
