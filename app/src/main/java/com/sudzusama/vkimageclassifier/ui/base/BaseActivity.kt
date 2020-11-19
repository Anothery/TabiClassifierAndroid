package com.sudzusama.vkimageclassifier.ui.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.sudzusama.vkimageclassifier.R

abstract class BaseActivity : AppCompatActivity() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val sfm = supportFragmentManager
        sfm.findFragmentById(R.id.navHostFragment)?.childFragmentManager?.let {
            for (fragment in it.fragments) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}