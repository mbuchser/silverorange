package com.silverorange.videoplayer.model

import com.google.gson.annotations.SerializedName


data class VideoData (
    @SerializedName("id")
    var id: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("hlsUrl")
    var hlsUrl: String,
    @SerializedName("fullUrl")
    var fullUrl: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("publishedAt")
    var publishedAt: String,
    @SerializedName("author")
    var author: Author)
{
}