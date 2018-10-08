package moymer.com.photolearning


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import moymer.com.utils.ActivityUtils
import android.content.Intent
import android.view.Menu
import android.view.MenuItem


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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_cut -> launchCategoriesActivity()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun launchCategoriesActivity() {
        val intent = Intent(this, PLMainActivity::class.java)
        startActivity(intent)
    }
}
