package com.example.mateo.mateohervas_training.utils

import com.example.mateo.mateohervas_training.data.networking.models.RemoteData
import java.io.Serializable

data class Response<T> (
    val code: Int,
    val etag: String,
    val data: RemoteData<T>
) : Serializable