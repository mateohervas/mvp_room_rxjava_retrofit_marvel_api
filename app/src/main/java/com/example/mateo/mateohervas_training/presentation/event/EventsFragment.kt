package com.example.mateo.mateohervas_training.Event

import android.arch.paging.PagedList
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import com.example.mateo.mateohervas_training.data.networking.models.Event
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.EVENT_KEY
import com.example.mateo.mateohervas_training.utils.TRANSITION_NAME
import com.example.mateo.mateohervas_training.data.networking.RxBus.RxBus
import com.example.mateo.mateohervas_training.data.networking.RxBus.RxEvent
import com.example.mateo.mateohervas_training.presentation.event.Adapters.EventListAdapter
import com.example.mateo.mateohervas_training.presentation.event.Detail.EventDetailActivity
import com.example.mateo.mateohervas_training.presentation.event.EventsContract
import com.example.mateo.mateohervas_training.presentation.event.EventsPresenter
import com.example.mateo.mateohervas_training.presentation.RecyclerItemClickListener
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import kotlinx.android.synthetic.main.fragment_recyclerview.view.*


class EventsFragment : Fragment(), EventsContract.View,
    RecyclerItemClickListener {


    private lateinit var recyclerView: RecyclerView
    private val eventsPresenter: EventsPresenter = EventsPresenter(this)
    private var isScrolling = false
    var disposable : Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.events_recyclerview, container, false)

        setupRecyclerView(view.event_recycler)
        setUpRxBusEventUpdates()

        return view
    }

    private val listAdapter: EventListAdapter by lazy {

        EventListAdapter(this)
    }
    override fun onRecyclerViewClick(image: ImageView, position: Int) {

        val transitionName = eventsPresenter.getEvent(listAdapter,position)?.id.toString()

        val intent = Intent(requireActivity(), EventDetailActivity::class.java)
        val bundle = createBundle(eventsPresenter.getEvent(listAdapter,position))

        intent.putExtra(EVENT_KEY,bundle)
        intent.putExtra(TRANSITION_NAME,transitionName)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            image,
            ViewCompat.getTransitionName(image)!!)

        activity?.startActivityFromFragment(this,intent,345,options.toBundle())


    }

    override fun updateList(list: PagedList<Event>) {

        if(!this.isDetached){
            listAdapter.submitList(list)
            event_prgLoading.visibility = View.GONE
        }
    }

    fun setUpRxBusEventUpdates(){
        disposable = RxBus.listen(RxEvent.EventEventStateChanged::class.java).subscribe({
            listAdapter.onStateChanged(it.event, it.event.isFavorite)
        },{
                e-> Log.e("favorite listener","Events Fragment Error:  $e")
        })

    }

    fun createBundle(event : Event?): Bundle{
        val bundle = Bundle()
        bundle.putSerializable(EVENT_KEY, event)
        return bundle
    }

    private fun setupRecyclerView(recycler: RecyclerView){

        val layoutManager = LinearLayoutManager(context)

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
                    eventsPresenter.listSuscriber()
                }
            }

        })

        eventsPresenter.getEvents(activity)

    }

    override fun onDestroy() {
        super.onDestroy()
        eventsPresenter.onCleared()
        disposable?.dispose()
    }

    companion object {
        fun newInstance() : EventsFragment {

            val fragment = EventsFragment()
            val args = Bundle()

            return fragment
        }
    }
}
