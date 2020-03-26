package com.gts.trackmypath.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.gts.trackmypath.R

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
