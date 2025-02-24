package com.example.ieltspreparation.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.ieltspreparation.R

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var navHostFragment = supportFragmentManager.findFragmentById(R.id.loginFragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
    }
}