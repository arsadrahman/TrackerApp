package com.arsa.trackerapp.view

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.arsa.trackerapp.R
import com.arsa.trackerapp.databinding.ActivityMainBinding
import com.arsa.trackerapp.databinding.ActivityNavigationExampleBinding

class ActivityNavigationExample : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_example)


    }

}