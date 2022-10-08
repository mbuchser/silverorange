package com.silverorange.videoplayer

import com.silverorange.videoplayer.model.VideoData
import retrofit2.Call
import retrofit2.http.GET

interface VideoDataInterface {
        @GET("/videos")
        fun getAllVideos(): Call<ArrayList<VideoData>?>?

}
