package com.example.mateo.mateohervas_training.presentation.event

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mateo.mateohervas_training.Event.EventsFragment
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.ALL
import com.example.mateo.mateohervas_training.utils.FAVORITE
import com.example.mateo.mateohervas_training.presentation.main.ViewPagerAdapter
import com.example.mateo.mateohervas_training.presentation.event.Favorite.FavoriteEventFragment
import kotlinx.android.synthetic.main.fragment_view_pager.*

class EventsViewPagerFragment: Fragment(){

    lateinit var adapter: ViewPagerAdapter
    lateinit var eventsFragment : EventsFragment
    lateinit var favoriteEventsFragment: FavoriteEventFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_view_pager,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = ViewPagerAdapter(childFragmentManager)
        eventsFragment = EventsFragment.newInstance()
        favoriteEventsFragment = FavoriteEventFragment.newInstance()

        adapter.addFragment(eventsFragment, ALL)
        adapter.addFragment(favoriteEventsFragment, FAVORITE)

        viewPager_mng.adapter = adapter

        tab_layout.setupWithViewPager(viewPager_mng)
    }

    companion object {

        fun newInstance(): EventsViewPagerFragment{

            val fragment = EventsViewPagerFragment()
            val args = Bundle()

            return fragment
        }
    }


}