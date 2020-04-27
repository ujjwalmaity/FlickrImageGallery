package dev.ujjwal.flickrimagegallery.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.ujjwal.flickrimagegallery.R
import dev.ujjwal.flickrimagegallery.view.fragment.HomeFragment
import dev.ujjwal.flickrimagegallery.view.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, HomeFragment()).commit()

        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, HomeFragment()).commit()
                }
                R.id.nav_profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ProfileFragment()).commit()
                }
                else -> println("Give a proper input")
            }
            true
        }
    }
}
