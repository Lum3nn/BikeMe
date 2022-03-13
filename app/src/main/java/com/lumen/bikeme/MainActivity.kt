package com.lumen.bikeme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        createNotificationChannel()
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

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelIdentification = getString(R.string.default_notification_channel_id)
            val name = "Default"
            val descriptionText = "BikeMe Alert"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelIdentification, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}