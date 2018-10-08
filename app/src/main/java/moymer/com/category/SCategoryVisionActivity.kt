package com.moymer.spoken.usercases.main.vision.category

import android.os.Bundle
import com.moymer.spoken.R
import com.moymer.spoken.SDaggerActivity
import com.moymer.spoken.utils.ActivityUtils
import javax.inject.Inject

class SCategoryVisionActivity @Inject constructor(): SDaggerActivity() {

    @Inject
    lateinit var injectedFragment: SCategoryVisionFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scontact)

        var categoryVisionFragment: SCategoryVisionFragment? = supportFragmentManager.findFragmentById(R.id.fragment_container) as SCategoryVisionFragment?

        if (categoryVisionFragment == null) {
            categoryVisionFragment = injectedFragment
            ActivityUtils.addFragmentToActivity(supportFragmentManager, categoryVisionFragment, R.id.fragment_container)
        }

        injectedFragment.arguments = intent?.extras

        overridePendingTransition(R.anim.slide_in_bottom_top, R.anim.hold)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.hold, R.anim.slide_out_top_bottom)

    }
}
