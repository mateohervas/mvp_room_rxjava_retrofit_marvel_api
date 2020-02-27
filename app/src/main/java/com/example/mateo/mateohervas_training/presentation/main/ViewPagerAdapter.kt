package com.example.mateo.mateohervas_training.presentation.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager){


    var fragmentList : ArrayList<Fragment> = ArrayList()
    var fragmentTitle : ArrayList<String> = ArrayList()

    fun addFragment( frg  :Fragment, title : String ){

        fragmentList.add(frg)
        fragmentTitle.add(title)
    }
    override fun getItem(position: Int): Fragment {

        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return fragmentTitle[position]
    }

}