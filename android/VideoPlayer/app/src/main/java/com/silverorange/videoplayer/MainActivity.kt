package com.silverorange.videoplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.core.net.toUri
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.silverorange.videoplayer.model.VideoData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var videoList: ArrayList<VideoData>
    val TAG : String = "Video player"
    lateinit var videoView : VideoView
    lateinit var fabPlay : FloatingActionButton
    lateinit var fabPrev : FloatingActionButton
    lateinit var fabNext : FloatingActionButton
    lateinit var textView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getVideoListFromBackend()
        videoView = findViewById(R.id.videoView)
        videoView.setOnCompletionListener {
            Log.i(TAG,"Videoview on complete")
            showHideVideoControlButtons(true)
        }
        videoView.setOnErrorListener(MediaPlayer.OnErrorListener {
                mediaPlayer, i, i2 -> Log.e(TAG, "Mediaplayer error: " + i)
            true
        })
        videoView.setOnPreparedListener { Log.i(TAG, "Videoview prepared") }
        fabPlay = findViewById(R.id.fabPlay)
        fabPlay.setOnClickListener { getFirstVideo()?.let { it1 ->
            playVideoSetVideoDescriptionAndHidePlayButtons(
                it1
            )
        } }
        fabNext = findViewById(R.id.fabNext)
        fabPrev = findViewById(R.id.fabPrevious)
        textView = findViewById(R.id.etVideoDescr)
    }

    fun getFirstVideo() : VideoData? {
        if (!videoList.isNullOrEmpty()) {
            return videoList[0]
        } else {
            return null
        }
    }

    fun getVideoListFromBackend() {
        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(VideoDataInterface::class.java)
        val call: Call<ArrayList<VideoData>?>? = apiInterface.getAllVideos()
        call!!.enqueue(object : Callback<ArrayList<VideoData>?> {
            override fun onResponse(
                call: Call<ArrayList<VideoData>?>,
                response: Response<ArrayList<VideoData>?>
            ) {
                if (response.isSuccessful) {
                    videoList = response.body()!!
                    sortVideosByDateDescending()
                    initVideoPrepareToPlay()
                } else {
                    Log.e(TAG, "on response failed with: " + response.message())
                    Toast.makeText(this@MainActivity, "Failed to get the video list: " + response.message(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ArrayList<VideoData>?>, t: Throwable) {
                Log.e(TAG, "failure getting video list" + t.message)
                Toast.makeText(this@MainActivity, "Failed to get the data" + t.message, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun sortVideosByDateDescending(){
        if (videoList.isNullOrEmpty()){
            Log.e(TAG, "Video list null or empty, sort impossible")
        } else {
            videoList.sortByDescending { it.publishedAt }
        }
    }


    fun initVideoPrepareToPlay(){
        if (!videoList.isEmpty()) {
            val videoToPlay = videoList[0]
            videoView.setVideoURI(videoToPlay.fullURL.toUri())
            videoView.setVideoPath(videoToPlay.fullURL)
            textView.setText(videoToPlay.description)
        } else {
            Toast.makeText(this@MainActivity, "No video to play right now, try again later", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun playVideoSetVideoDescriptionAndHidePlayButtons(video2Play: VideoData){
        showHideVideoControlButtons(false)
        videoView.start()
    }

    fun showHideVideoControlButtons(show: Boolean){
        if (show){
            fabPlay.show()
            fabPrev.show()
            fabNext.show()
        }else {
            fabPlay.hide()
            fabPrev.hide()
            fabNext.hide()
        }
    }
}