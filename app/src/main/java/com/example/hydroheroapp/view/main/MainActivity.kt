package com.example.hydroheroapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.hydroheroapp.R
import com.example.hydroheroapp.databinding.ActivityMainBinding
import com.example.hydroheroapp.view.main.analyze.AnalyzeFragment
import com.example.hydroheroapp.view.main.home.HomeFragment
import com.example.hydroheroapp.view.main.reminder.ReminderFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.bottom_reminder -> {
                    replaceFragment(ReminderFragment())
                    true
                }

                R.id.bottom_analyze -> {
                    replaceFragment(AnalyzeFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment())
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }


}

