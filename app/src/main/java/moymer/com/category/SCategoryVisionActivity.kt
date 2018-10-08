package moymer.com.category

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import moymer.com.photolearning.R
import moymer.com.utils.ActivityUtils

class SCategoryVisionActivity: AppCompatActivity() {

    private var injectedFragment: SCategoryVisionFragment = SCategoryVisionFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var categoryVisionFragment: SCategoryVisionFragment? = supportFragmentManager.findFragmentById(R.id.fragment_container) as SCategoryVisionFragment?

        if (categoryVisionFragment == null) {
            categoryVisionFragment = injectedFragment
            ActivityUtils.addFragmentToActivity(supportFragmentManager, categoryVisionFragment, R.id.fragment_container)
        }
    }
}
