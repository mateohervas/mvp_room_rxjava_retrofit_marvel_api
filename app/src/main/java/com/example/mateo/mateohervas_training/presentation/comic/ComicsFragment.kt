package com.example.mateo.mateohervas_training.Comic

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
import com.example.mateo.mateohervas_training.data.networking.models.Comic
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.COMIC_KEY
import com.example.mateo.mateohervas_training.utils.TRANSITION_NAME
import com.example.mateo.mateohervas_training.data.networking.RxBus.RxBus
import com.example.mateo.mateohervas_training.data.networking.RxBus.RxEvent
import com.example.mateo.mateohervas_training.presentation.comic.Adapters.ComicListAdapter
import com.example.mateo.mateohervas_training.presentation.comic.ComicContract
import com.example.mateo.mateohervas_training.presentation.comic.Detail.ComicDetailActivity
import com.example.mateo.mateohervas_training.presentation.comic.ComicsPresenter
import com.example.mateo.mateohervas_training.presentation.RecyclerItemClickListener
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import kotlinx.android.synthetic.main.fragment_recyclerview.view.*


class ComicsFragment : Fragment(), RecyclerItemClickListener, ComicContract.View {

    private lateinit var recyclerView: RecyclerView
    private val comicsPresenter: ComicsPresenter = ComicsPresenter( this)
    var isScrolling = false
     var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_recyclerview, container, false)

        setupRecyclerView(view.event_recycler)
        setUpRxBusComicUpdates()

        return view
    }

    private val listAdapter: ComicListAdapter by lazy {

        ComicListAdapter(this)
    }

    override fun onRecyclerViewClick(image: ImageView, position: Int) {

        val transitionName = comicsPresenter.getComic(listAdapter,position)?.id.toString()

        val intent = Intent(requireActivity(), ComicDetailActivity::class.java)
        val bundle = createBundle(comicsPresenter.getComic(listAdapter,position))

        intent.putExtra(COMIC_KEY,bundle)
        intent.putExtra(TRANSITION_NAME,transitionName)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            image,
            ViewCompat.getTransitionName(image)!!)

        activity?.startActivityFromFragment(this,intent,543,options.toBundle())

    }

    override fun updateList(list : PagedList<Comic>) {
        if(!this.isDetached){
            listAdapter.submitList(list)
            event_prgLoading.visibility = View.GONE
        }
    }

    fun createBundle(comic : Comic?): Bundle{
        val bundle = Bundle()
        bundle.putSerializable(COMIC_KEY, comic)
        return bundle
    }

    fun setUpRxBusComicUpdates(){
        disposable = RxBus.listen(RxEvent.EventComicStateChanged::class.java).subscribe({
            listAdapter.onStateChanged(it.comic, it.comic.isFavorite)
        },{
                e-> Log.e("favorite listener","Comics Fragment Error:  $e")
        })

    }

    private fun setupRecyclerView(recycler: RecyclerView){

        val layoutManager = GridLayoutManager(context, 2)

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
                    comicsPresenter.listSuscriber()
                }
            }

        })

        comicsPresenter.getComics(activity)

    }

    override fun onDestroyView() {
        disposable?.dispose()
        comicsPresenter.onCleared()
        super.onDestroyView()
    }


    companion object {
        fun newInstance(): ComicsFragment {

            val fragment = ComicsFragment()
            val args = Bundle()

            return fragment
        }
    }
}
