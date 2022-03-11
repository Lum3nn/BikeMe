package com.lumen.bikeme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.lumen.bikeme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController
        get() {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

            navHostFragment.navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
                if (nd.id == R.id.tripsListFragment || nd.id == R.id.mapsFragment2) {
                    binding.bottomNavigation.visibility = View.VISIBLE
                } else {
                    binding.bottomNavigation.visibility = View.GONE
                }
            }

            return navHostFragment.navController
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupNavigation() {

        binding.bottomNavigation.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mapsFragment2,
                R.id.tripsListFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}