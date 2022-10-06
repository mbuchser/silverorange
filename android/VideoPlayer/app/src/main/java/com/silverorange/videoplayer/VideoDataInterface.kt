package com.silverorange.videoplayer

import com.silverorange.videoplayer.model.ResponseListVideos
import retrofit2.Response
import retrofit2.http.GET

interface VideoDataInterface {
        @GET("/videos")
        suspend fun getAllVideos(): Response<ResponseListVideos>
}
