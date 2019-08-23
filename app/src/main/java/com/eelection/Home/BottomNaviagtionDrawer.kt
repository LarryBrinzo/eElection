package com.eelection.Home

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.widget.LinearLayout
import com.eelection.Home.Fragments.CandidatesFragment
import com.eelection.Home.Fragments.HomeFragment
import com.eelection.Home.Fragments.MyActivityFragment
import com.eelection.Home.Fragments.ProfileFragment
import com.eelection.R

class BottomNaviagtionDrawer : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

    internal val fragment1: Fragment = HomeFragment()
    internal val fragment2: Fragment = MyActivityFragment()
    internal val fragment3: Fragment = CandidatesFragment()
    internal var fragment4: Fragment = ProfileFragment()
    internal val fm = supportFragmentManager
    lateinit var navigation: BottomNavigationView
    internal var active = fragment1
    lateinit var elect: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_navigation)

        navigation = findViewById(R.id.navigationView)
        elect=findViewById(R.id.elect)
        navigation.setOnNavigationItemSelectedListener(this)

        elect.setOnClickListener {
            val intent = Intent(applicationContext, ElectionActivity::class.java)
            startActivity(intent)
        }

       fm.beginTransaction().add(R.id.fragment_container, fragment4, "4").hide(fragment4).commit()
        fm.beginTransaction().add(R.id.fragment_container, fragment3, "3").hide(fragment3).commit()
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "2").hide(fragment2).commit()
        fm.beginTransaction().add(R.id.fragment_container, fragment1, "1").commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.navigation_home -> {
                fm.beginTransaction().hide(active).show(fragment1).commit()
                active = fragment1
                return true
            }

            R.id.navigation_myactivity -> {
                fm.beginTransaction().hide(active).show(fragment2).commit()
                active = fragment2
                return true
            }

            R.id.navigation_candidates -> {
                fm.beginTransaction().hide(active).show(fragment3).commit()
                active = fragment3
                return true
            }


            R.id.navigation_profile -> {
                fm.beginTransaction().hide(active).show(fragment4).commit()
                active = fragment4
                return true
            }
        }

        return false
    }


    override fun onBackPressed() {

        if (navigation.selectedItemId == R.id.navigation_home) {
            super.onBackPressed()
            finish()
        } else {
            navigation.selectedItemId = R.id.navigation_home
        }
    }
}
