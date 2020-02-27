package com.example.mateo.mateohervas_training.presentation.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.example.mateo.mateohervas_training.presentation.character.CharacterViewPagerFragment
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.ConnectionDetector
import com.example.mateo.mateohervas_training.presentation.comic.ComicsViewPagerFragment
import com.example.mateo.mateohervas_training.presentation.event.EventsViewPagerFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    lateinit var fragmentCharacterViewPager : CharacterViewPagerFragment
    lateinit var comicsFragment: ComicsViewPagerFragment
    lateinit var eventsFragment: EventsViewPagerFragment
    val cd : ConnectionDetector = ConnectionDetector()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, mainToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        fragmentCharacterViewPager = CharacterViewPagerFragment.newInstance()
        comicsFragment = ComicsViewPagerFragment.newInstance()
        eventsFragment = EventsViewPagerFragment.newInstance()

        FragmentInfo(getString(R.string.character_fragment_title), fragmentCharacterViewPager)


    }

    override fun onBackPressed() {

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)

        } else {

            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.app_name)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(getString(R.string.exit_question))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes)){ dialog, which -> super.onBackPressed() }
                .setNegativeButton(getString(R.string.no)){ dialog, which -> }

            val alert : AlertDialog = builder.create()
            alert.show()

        }
    }

    fun FragmentInfo( title : String, frg : Fragment){

        supportActionBar?.setTitle(title)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frmContainer, frg)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        if(cd.isConnectingToInternet(this)) {
            imgNoNet.visibility = View.GONE
        }
        else{
            imgNoNet.visibility = View.VISIBLE
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.nav_characters -> { FragmentInfo(getString(R.string.character_fragment_title), fragmentCharacterViewPager)  }
            R.id.nav_comics -> { FragmentInfo(getString(R.string.comics),comicsFragment) }
            R.id.nav_events -> { FragmentInfo(getString(R.string.events),eventsFragment)  }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}
