package com.example.mateo.mateohervas_training.presentation.character


import android.arch.paging.PagedList
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
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


class CharactersFragment : Fragment(), RecyclerItemClickListener, CharacterContract.View {


    private lateinit var recyclerView: RecyclerView
    private val charactersPresenter : CharactersPresenter =  CharactersPresenter(this)
     var disposable: Disposable? = null
    var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater.inflate(R.layout.fragment_recyclerview, container, false)

        setupRecyclerView(view.event_recycler)
        setUpRxBusCharacteUpdates()
        return view
    }

    private val listAdapter : CharacterListAdapter by lazy {
        CharacterListAdapter(this)
    }

    override fun onRecyclerViewClick(image: ImageView, position: Int) {

        val character  = listAdapter.getItemAtPosition(position)!!
        val transitionName = character.id.toString()

        val intent = Intent(requireActivity(), CharacterDetailActivity::class.java)

        val bundle = createBundle(character)

        intent.putExtra(CHARACTER_KEY,bundle)

        intent.putExtra(TRANSITION_NAME,transitionName)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            image,
            ViewCompat.getTransitionName(image)!!)

        activity?.startActivityFromFragment(this,intent,555,options.toBundle())
    }

    override fun updateList(list: PagedList<Character>) {
        if(!this.isDetached){
        listAdapter.submitList(list)
        event_prgLoading.visibility = View.GONE
        }
    }

    fun setUpRxBusCharacteUpdates(){
        disposable = RxBus.listen(RxEvent.EventCharacterStateChanged::class.java).subscribe({
            listAdapter.onStateChanged(it.character, it.character.isFavorite)
        },{
                e-> Log.e("favorite listener","CharactersFragment Error:  $e")
        })

    }


    fun createBundle(character: Character?): Bundle {
        val bundle = Bundle()
        bundle.putSerializable(CHARACTER_KEY, character)
        return bundle
    }

    private fun setupRecyclerView(recycler: RecyclerView){


        val layoutManager = GridLayoutManager(context, 3)

        recyclerView = recycler
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = listAdapter

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    event_prgBottom.visibility = View.VISIBLE
                    isScrolling = true

                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(isScrolling){

                    isScrolling = false
                    event_prgBottom.visibility = View.GONE
                    charactersPresenter.listSuscriber()
                }
            }

        })
        charactersPresenter.getCharacters(activity)
    }

    override fun onDestroyView() {
        charactersPresenter.onCleared()
        disposable?.dispose()
        super.onDestroyView()
    }

    companion object {

        fun newInstance(): CharactersFragment {

            val fragment = CharactersFragment()
            val args = Bundle()

            return fragment
        }

    }

}
