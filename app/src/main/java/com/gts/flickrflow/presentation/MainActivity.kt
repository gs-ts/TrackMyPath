package com.gts.flickrflow.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gts.flickrflow.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PhotoStreamFragment.newInstance())
                .commitNow()
        }
    }
}
