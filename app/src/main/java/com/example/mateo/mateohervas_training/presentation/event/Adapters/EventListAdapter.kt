package com.example.mateo.mateohervas_training.presentation.event.Adapters

import android.arch.paging.PagedListAdapter
import android.support.v4.view.ViewCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mateo.mateohervas_training.data.networking.models.Event
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.load
import com.example.mateo.mateohervas_training.presentation.RecyclerItemClickListener
import kotlinx.android.synthetic.main.event_item_recycler.view.*

class EventListAdapter(private val recycler: RecyclerItemClickListener): PagedListAdapter<Event,EventListAdapter.ViewHolder>(eventDiff){


    fun getItemAtPosition(position: Int): Event? {
        return getItem(position)
    }

    fun onStateChanged(event: Event, isFavorite: Boolean){

        var i = 0
        for(item in currentList!!){
            if(item.id == event.id){
                currentList?.get(i)?.isFavorite = isFavorite
                notifyItemChanged(i)
            }
            i++
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.event_item_recycler,viewGroup, false)

        return EventListAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val event : Event? = getItem(position)
        holder.eventImage.load("${event?.thumbnail?.path}/standard_amazing.${event?.thumbnail?.extension}")
        holder.eventName.text = event?.title
        holder.eventDescription.text = event?._details

        if(event!!.isFavorite){
            holder.star.visibility = View.VISIBLE
        }else{
            holder.star.visibility = View.GONE
        }

        val transitionName = event.id.toString()
        ViewCompat.setTransitionName(holder.eventImage,transitionName)

        holder.itemView.setOnClickListener{
            recycler.onRecyclerViewClick(holder.eventImage, position)

        }

    }


    class ViewHolder(item: View):RecyclerView.ViewHolder(item) {

        val eventImage = item.imgEvent
        val eventName = item.txtEventName
        val eventDescription = item.txtEventDescription
        val star = item.imgEventStar

    }

    companion object {

        val eventDiff = object: DiffUtil.ItemCallback<Event>(){

            override fun areItemsTheSame(existent: Event, new: Event): Boolean {

                return existent.id == new.id
            }

            override fun areContentsTheSame(existent: Event, new: Event): Boolean {

                return existent == new
            }
        }
    }


}