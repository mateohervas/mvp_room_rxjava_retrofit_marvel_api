package com.example.mateo.mateohervas_training.data.networking.RxBus

import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.data.networking.models.Comic
import com.example.mateo.mateohervas_training.data.networking.models.Event

class RxEvent{

    data class EventCharacterStateChanged(val character: Character)
    data class EventAddCharacter(val character: Character)


    data class EventComicStateChanged(val comic: Comic)
    data class EventAddComic(val comic: Comic)

    data class EventEventStateChanged(val event: Event)
    data class EventAddEvent(val event : Event)

}