package com.example.mateo.mateohervas_training.presentation.event.Favorite

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
import com.example.mateo.mateohervas_training.data.networking.models.Event
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.EVENT_KEY
import com.example.mateo.mateohervas_training.utils.TRANSITION_NAME
import com.example.mateo.mateohervas_training.data.networking.RxBus.RxBus
import com.example.mateo.mateohervas_training.data.networking.RxBus.RxEvent
import com.example.mateo.mateohervas_training.presentation.event.Adapters.EventListAdapter
import com.example.mateo.mateohervas_training.presentation.event.Detail.EventDetailActivity
import com.example.mateo.mateohervas_training.presentation.RecyclerItemClickListener
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import kotlinx.android.synthetic.main.fragment_recyclerview.view.*


class FavoriteEventFragment : Fragment(), RecyclerItemClickListener, FavoriteEventContract.View{

    private lateinit var recyclerView: RecyclerView
    private val favoriteEventPresenter = FavoriteEventPresenter(this)
    var disposable : Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_recyclerview, container, false)

        setupRecyclerView(view.event_recycler)
        setUpRxBusEventUpdates()

        return view
    }

    private val listAdapter : EventListAdapter by lazy {
        EventListAdapter(this)
    }
    override fun onRecyclerViewClick(image: ImageView, position: Int) {

        val transitionName = favoriteEventPresenter.getEvent(listAdapter, position).id.toString()

        val intent = Intent(requireActivity(), EventDetailActivity::class.java)
        val bundle = createBundle(favoriteEventPresenter.getEvent(listAdapter,position))

        intent.putExtra(EVENT_KEY,bundle)
        intent.putExtra(TRANSITION_NAME,transitionName)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            image,
            ViewCompat.getTransitionName(image)!!)

        activity?.startActivityFromFragment(this,intent,541,options.toBundle())


    }

    fun setUpRxBusEventUpdates(){

        disposable = RxBus.listen(RxEvent.EventAddEvent::class.java).subscribe{

            listAdapter.currentList!!.dataSource.invalidate()
            favoriteEventPresenter.listSubscriber(context)
        }

    }

    fun createBundle(event: Event): Bundle{

        event.isFavorite = true
        val bundle = Bundle()
        bundle.putSerializable(EVENT_KEY, event)

        return bundle
    }

    override fun updateList(list: PagedList<Event>) {

        if(!this.isDetached){

            listAdapter.submitList(list)
            event_prgLoading.visibility = View.GONE

        }
    }

    fun setupRecyclerView(recycler : RecyclerView){
        val layoutManager = GridLayoutManager(context, 1)

        recyclerView = recycler
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = listAdapter

        favoriteEventPresenter.listSubscriber(context)
        listAdapter.notifyDataSetChanged()

    }

    companion object {

        fun newInstance(): FavoriteEventFragment{

            val fragment = FavoriteEventFragment()
            val args = Bundle()

            return fragment
        }
    }

    override fun onDestroyView() {
        disposable?.dispose()
        super.onDestroyView()
    }



}