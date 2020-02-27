package com.example.mateo.mateohervas_training.presentation.comic.Favorite

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
import com.example.mateo.mateohervas_training.data.networking.models.Comic
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.COMIC_KEY
import com.example.mateo.mateohervas_training.utils.TRANSITION_NAME
import com.example.mateo.mateohervas_training.data.networking.RxBus.RxBus
import com.example.mateo.mateohervas_training.data.networking.RxBus.RxEvent
import com.example.mateo.mateohervas_training.presentation.comic.Adapters.ComicListAdapter
import com.example.mateo.mateohervas_training.presentation.comic.Detail.ComicDetailActivity
import com.example.mateo.mateohervas_training.presentation.RecyclerItemClickListener
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import kotlinx.android.synthetic.main.fragment_recyclerview.view.*

class FavoriteComicFragment : Fragment(), RecyclerItemClickListener, FavoriteComicContract.View{

    private lateinit var recyclerView: RecyclerView

    private val favoriteComicFragment = FavoriteComicPresenter(this)

     var disposable :Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_recyclerview, container, false)

        setupRecyclerView(view.event_recycler)

        setUpRxBusComicUpdates()

        return view
    }

    private val listAdapter : ComicListAdapter by lazy {
       ComicListAdapter(this)
    }

    override fun onRecyclerViewClick(image: ImageView, position: Int) {

        val transitionName = favoriteComicFragment.getComic(listAdapter, position).id.toString()

        val intent = Intent(requireActivity(), ComicDetailActivity::class.java)

        val bundle = createBundle(favoriteComicFragment.getComic(listAdapter,position))

        intent.putExtra(COMIC_KEY,bundle)
        intent.putExtra(TRANSITION_NAME,transitionName)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            image,
            ViewCompat.getTransitionName(image)!!)

        activity?.startActivityFromFragment(this,intent,542,options.toBundle())

    }

    fun setUpRxBusComicUpdates(){

        disposable = RxBus.listen(RxEvent.EventAddComic::class.java).subscribe{

            listAdapter.currentList!!.dataSource.invalidate()
            favoriteComicFragment.listSubscriber(context)
        }

    }

    fun createBundle(comic: Comic): Bundle{

        comic.isFavorite = true
        val bundle = Bundle()
        bundle.putSerializable(COMIC_KEY, comic)
        return bundle

    }

    override fun updateList(list: PagedList<Comic>) {

        if(!this.isDetached){

            listAdapter.submitList(list)
           event_prgLoading.visibility = View.GONE
        }
    }


    fun setupRecyclerView(recycler : RecyclerView){
        val layoutManager = GridLayoutManager(context, 2)

        recyclerView = recycler
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = listAdapter

        favoriteComicFragment.listSubscriber(context)

        listAdapter.notifyDataSetChanged()


    }

    override fun onDestroyView() {
        disposable?.dispose()
        super.onDestroyView()
    }


    companion object {

        fun newInstance(): FavoriteComicFragment{

            val fragment = FavoriteComicFragment()
            val args = Bundle()

            return fragment
        }
    }



}