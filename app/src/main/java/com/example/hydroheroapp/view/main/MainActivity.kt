package com.example.hydroheroapp.view.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hydroheroapp.R
import com.example.hydroheroapp.data.remote.repository.LoginPrefsRepo
import com.example.hydroheroapp.data.remote.repository.dataStore
import com.example.hydroheroapp.databinding.ActivityMainBinding
import com.example.hydroheroapp.view.ViewModelFactory
import com.example.hydroheroapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(
                this,
                LoginPrefsRepo.getInstance(dataStore)
            )
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_reminder,
                R.id.navigation_analyze,
                R.id.navigation_history
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_logout -> {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.log_out))
                    setMessage(getString(R.string.are_your_sure))
                    setPositiveButton(getString(R.string.continue_dialog)) { _, _ ->
                        viewModel.logout()
                        val intent = Intent(context, WelcomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    create()
                    show()
                }
                true
            }

            else -> false
        }
    }
}