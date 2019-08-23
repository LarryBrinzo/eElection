package com.eelection.Home

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.widget.LinearLayout
import com.eelection.Home.Fragments.ElectionFragment
import com.eelection.Home.Fragments.ResultFragment
import com.eelection.R


class ElectionActivity : AppCompatActivity(), ElectionFragment.OnFragmentInteractionListener,ResultFragment.OnFragmentInteractionListener{

    lateinit var back: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elect)

        val viewPager: ViewPager = findViewById(R.id.pager)
        val myPagerAdapter: MyPagerAdapter = MyPagerAdapter(getSupportFragmentManager())
        viewPager.setAdapter(myPagerAdapter)
        val tabLayout: TabLayout = findViewById(R.id.tablayout)
        tabLayout.setupWithViewPager(viewPager)

        back = findViewById(R.id.backbt)

        back.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
