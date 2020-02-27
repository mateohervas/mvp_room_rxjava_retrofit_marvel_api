package com.example.mateo.mateohervas_training.data.networking.mappers

import com.example.mateo.mateohervas_training.data.networking.models.Event
import com.example.mateo.mateohervas_training.data.networking.models.Thumbnail
import com.example.mateo.mateohervas_training.data.cache.Entities.EventEntity

class EventMapper{

    companion object {

        fun getEventfromEventEntity(eventEntity: EventEntity): Event {

            val url = eventEntity.thumbnail
            val splitter = "/standard_amazing"
            val path = url.split(splitter)

            val event = Event(
                eventEntity.id,
                eventEntity.title,
                eventEntity.description,
                Thumbnail(path[0], "jpg"),
                eventEntity.isFavorite
            )

            return event
        }
    }
}