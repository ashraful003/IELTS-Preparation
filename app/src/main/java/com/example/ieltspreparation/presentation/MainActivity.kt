package com.example.ieltspreparation.presentation

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.ieltspreparation.R
import com.example.ieltspreparation.databinding.ActivityMainBinding
import com.example.ieltspreparation.presentation.util.IPActivityUtil
import com.example.ieltspreparation.presentation.util.SharePreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IPActivityUtil.ActivityListener {
    @Inject
    lateinit var sharedPrefs: SharePreferenceUtil
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }
        val authedUser: Boolean = try {
            !sharedPrefs.getAuthToken().isNullOrEmpty()
        } catch (e: Exception) {
            false
        }
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment
        with(navHostFragment) {
            val inflater = findNavController().navInflater
            if (authedUser) {
                findNavController().graph = (inflater.inflate(R.navigation.dashboard_navigation))
            } else {
                findNavController().graph = (inflater.inflate(R.navigation.login_navigation))
            }
        }
        navController = navHostFragment.navController
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> navHostFragment.findNavController().navigate(R.id.homeFragment)
                R.id.profile -> navHostFragment.findNavController().navigate(R.id.profileFragment)
            }
            true
        }
    }

    override fun hideBottomNavigation(hide: Boolean) {
        if (hide) {
            binding.bottomNavigationView.visibility = View.GONE
        } else {
            binding.bottomNavigationView.visibility = View.VISIBLE
        }
    }

    override fun setFullScreenLoading(short: Boolean) {
        if (short) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            binding.fullscreenLoading.visibility = View.VISIBLE
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            binding.fullscreenLoading.visibility = View.GONE
        }
    }

    override fun closeKeyboard() {
        TODO("Not yet implemented")
    }

    companion object {
        fun getLaunchIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}