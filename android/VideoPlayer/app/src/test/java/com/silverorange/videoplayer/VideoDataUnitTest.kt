package com.silverorange.videoplayer

import com.silverorange.videoplayer.model.Author
import com.silverorange.videoplayer.model.VideoData
import org.junit.Test

import org.junit.Assert.*

class VideoDataUnitTest {

    var videoData = VideoData(
        "2f1fe9c0-bdbf-4104-bee2-3c0ec514436f",
        "About EM:RAP",
        hlsURL = "",
        fullURL = "",
        description = "",
        publishedAt = "",
        author = Author("", "Mel Herbert")
    )

    var videoDataWrong = VideoData(
        "123",
        "No title",
        hlsURL = "http",
        fullURL = "http",
        description = "",
        publishedAt = "",
        author = Author("", "Mel")
    )

    @Test
    fun videodata_correct() {
        assertEquals(videoData.title, "About EM:RAP")
        assertTrue(videoData.title.isNotEmpty())
        assertTrue(videoData.id.length > 20)
        assertEquals(videoData.id, "2f1fe9c0-bdbf-4104-bee2-3c0ec514436f")

    }

    @Test
    fun video_ext_data_correct(){
        assertTrue(videoData.hlsURL.startsWith("https"))
        //assertTrue(videoData.fullURL.startsWith("https://"))
        //assertTrue(videoData.description.isNotEmpty())
        //assertEquals(videoData.author, "Mel Herbert" )
    }

    @Test
    fun videodata_wrong(){
        assertFalse(videoDataWrong.title.isEmpty())
        assertNotEquals(videoData.title, "")
        assertNotEquals(videoData.author, "")
        assertFalse(videoData.title.isEmpty())
        assertFalse(videoData.id.isEmpty())
        assertNotEquals(videoData.id, "2f1fe9c0-bdbf-4104-bee2")
        assertFalse(videoData.hlsURL.startsWith("stream"))
        assertFalse(videoData.fullURL.startsWith("//"))
        assertFalse(videoData.description.isNotEmpty())

    }
}