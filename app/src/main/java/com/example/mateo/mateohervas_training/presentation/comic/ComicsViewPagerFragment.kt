package com.example.mateo.mateohervas_training.presentation.comic

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mateo.mateohervas_training.Comic.ComicsFragment
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.ALL
import com.example.mateo.mateohervas_training.utils.FAVORITE
import com.example.mateo.mateohervas_training.presentation.main.ViewPagerAdapter
import com.example.mateo.mateohervas_training.presentation.comic.Favorite.FavoriteComicFragment
import kotlinx.android.synthetic.main.fragment_view_pager.*

class ComicsViewPagerFragment : Fragment(){

    lateinit var adapter : ViewPagerAdapter
    lateinit var comicsFragment: ComicsFragment
    lateinit var favoriteComicsFragment: FavoriteComicFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_view_pager,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = ViewPagerAdapter(childFragmentManager)
        comicsFragment = ComicsFragment.newInstance()
        favoriteComicsFragment = FavoriteComicFragment.newInstance()

        adapter.addFragment(comicsFragment, ALL)
        adapter.addFragment(favoriteComicsFragment, FAVORITE)

        viewPager_mng.adapter = adapter

        tab_layout.setupWithViewPager(viewPager_mng)

        adapter.notifyDataSetChanged()

    }



    companion object {

         fun newInstance(): ComicsViewPagerFragment{

             val fragment = ComicsViewPagerFragment()
             val args = Bundle()

             return fragment
         }
    }
}