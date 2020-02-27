package com.example.mateo.mateohervas_training.presentation.character.Favorite

import android.arch.paging.PagedList
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.CHARACTER_KEY
import com.example.mateo.mateohervas_training.utils.TRANSITION_NAME
import com.example.mateo.mateohervas_training.presentation.character.Adapters.CharacterListAdapter
import com.example.mateo.mateohervas_training.presentation.character.Detail.CharacterDetailActivity
import com.example.mateo.mateohervas_training.data.networking.RxBus.RxBus
import com.example.mateo.mateohervas_training.data.networking.RxBus.RxEvent
import com.example.mateo.mateohervas_training.presentation.RecyclerItemClickListener
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import kotlinx.android.synthetic.main.fragment_recyclerview.view.*


class FavoriteCharacterFragment() : Fragment(),
    RecyclerItemClickListener, FavoriteCharacterContract.View{


    private lateinit var recyclerView: RecyclerView
    private val favoriteCharacterPresenter = FavoriteCharacterPresenter(this)
     var disposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_recyclerview, container, false)

        setupRecyclerView(view.event_recycler)

        setUpRxBusCharacteUpdates()

        return view
    }

    private val listAdapter: CharacterListAdapter by lazy {
        CharacterListAdapter(this)
    }

    override fun onRecyclerViewClick(image: ImageView, position: Int) {

        val transitionName = favoriteCharacterPresenter.getCharacter(listAdapter,position).id.toString()

        val intent = Intent(requireActivity(),CharacterDetailActivity::class.java)
        val bundle = createBundle(favoriteCharacterPresenter.getCharacter(listAdapter,position))

        intent.putExtra(CHARACTER_KEY,bundle)
        intent.putExtra(TRANSITION_NAME,transitionName)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            image,
            ViewCompat.getTransitionName(image)!!)

        activity?.startActivityFromFragment(this,intent,541,options.toBundle())

    }

    override fun updateList(list: PagedList<Character>) {
        if(!this.isDetached){

            listAdapter.submitList(list)
            event_prgLoading.visibility = View.GONE

        }
    }

    fun setUpRxBusCharacteUpdates(){

        disposable = RxBus.listen(RxEvent.EventAddCharacter::class.java).subscribe{

            listAdapter.currentList!!.dataSource.invalidate()
            favoriteCharacterPresenter.listSubscriber(context)
        }

    }

    fun createBundle(character: Character): Bundle{

        character.isFavorite = true
        val bundle = Bundle()
        bundle.putSerializable(CHARACTER_KEY, character)
        return bundle
    }


    fun setupRecyclerView(recycler : RecyclerView){
        val layoutManager = GridLayoutManager(context, 3)

        recyclerView = recycler
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = listAdapter

        favoriteCharacterPresenter.listSubscriber(context)
        listAdapter.notifyDataSetChanged()

    }

    override fun onDestroyView() {
        disposable?.dispose()
        super.onDestroyView()
    }

    companion object {

        fun newInstance(): FavoriteCharacterFragment{

            val fragment = FavoriteCharacterFragment()
            val args = Bundle()

            return fragment
        }
    }



}