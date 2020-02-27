package com.example.mateo.mateohervas_training.data.networking.models

import java.io.Serializable

data class RemoteData<T>(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<T>
) : Serializable