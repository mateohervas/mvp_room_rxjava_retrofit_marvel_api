package com.example.mateo.mateohervas_training.presentation.character



import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.ALL
import com.example.mateo.mateohervas_training.utils.FAVORITE
import com.example.mateo.mateohervas_training.presentation.main.ViewPagerAdapter
import com.example.mateo.mateohervas_training.presentation.character.Favorite.FavoriteCharacterFragment
import kotlinx.android.synthetic.main.fragment_view_pager.*


class CharacterViewPagerFragment : Fragment() {



    lateinit var adapter : ViewPagerAdapter
    lateinit var charactersFragment: CharactersFragment
    lateinit var favoriteCharacterFragment: FavoriteCharacterFragment



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



       return inflater.inflate(R.layout.fragment_view_pager, container, false)


    }


   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       adapter = ViewPagerAdapter(childFragmentManager)
       charactersFragment = CharactersFragment.newInstance()
       favoriteCharacterFragment = FavoriteCharacterFragment.newInstance()

       adapter!!.addFragment(charactersFragment, ALL)
       adapter!!.addFragment(favoriteCharacterFragment, FAVORITE)


       viewPager_mng.adapter = adapter

       tab_layout.setupWithViewPager(viewPager_mng)


    }


    companion object {

       fun newInstance(): CharacterViewPagerFragment {


           val fragment = CharacterViewPagerFragment()
           val args = Bundle()

           return fragment
       }

    }

}
