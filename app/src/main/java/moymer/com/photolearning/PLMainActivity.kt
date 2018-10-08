package moymer.com.photolearning


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import moymer.com.utils.ActivityUtils

class PLMainActivity : AppCompatActivity() {

    private val plMainFragment = PLMainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mainFragment: PLMainFragment? = supportFragmentManager.findFragmentById(R.id.fragment_container) as PLMainFragment?

        if (mainFragment == null) {
            mainFragment = plMainFragment
            ActivityUtils.addFragmentToActivity(supportFragmentManager, mainFragment, R.id.fragment_container)
        }

        mainFragment.arguments = intent?.extras
    }
}
