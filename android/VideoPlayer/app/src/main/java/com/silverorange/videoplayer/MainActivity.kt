package com.silverorange.videoplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toUri
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.silverorange.videoplayer.model.VideoData
import io.noties.markwon.Markwon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var videoList: ArrayList<VideoData>
    val TAG = "Video player"
    val FIRST_VIDEO_POSITION_ZERO = 0
    val FADEBUTTON_ALPHAZERODOTTWO = 0.2f
    val SHOWBUTTON_ALPHAONEDOTZERO = 1.0f
    private var isPlaying = false
    private lateinit var currentVideoPlaying : VideoData
    private var currentPosition = 0
    private var totalVideos = 0
    private lateinit var videoView : VideoView
    private lateinit var fabPlay : FloatingActionButton
    private lateinit var fabPrev : FloatingActionButton
    private lateinit var fabNext : FloatingActionButton
    private lateinit var textView : TextView
    private lateinit var tvTitle : TextView
    private lateinit var tvAuthor : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getVideoListFromBackend()
        videoView = findViewById(R.id.videoView)
        videoView.setOnCompletionListener {
            showHideVideoControlButtons(true)
        }
        videoView.setOnErrorListener(MediaPlayer.OnErrorListener {
                mediaPlayer, i, i2 -> Log.e(TAG, "Mediaplayer error: " + i)
            Toast.makeText(this, "Mediaplayer error: " + + i, Toast.LENGTH_LONG).show()
            true
        })
        videoView.setOnPreparedListener { Log.i(TAG, "Videoview prepared") }
        fabPlay = findViewById(R.id.fabPlay)
        fabPlay.setOnClickListener {
            if (isPlaying){
                videoView.pause()
                isPlaying = false
                showHideVideoControlButtons(true)
            } else {
                playVideoSetVideoDescriptionAndHidePlayButtons()
                isPlaying = true
            }
        }
        fabNext = findViewById(R.id.fabNext)
        fabNext.setOnClickListener { view ->
            setNextVideoAndHideActionButtons()
        }
        fabPrev = findViewById(R.id.fabPrevious)
        fabPrev.setOnClickListener {
            setPreviousVideoAndHideActionButtons()
        }
        textView = findViewById(R.id.etVideoDescr)
        tvTitle = findViewById(R.id.tvTitle)
        tvAuthor = findViewById(R.id.tvAuthor)
    }

    fun setFirstVideoAndHidePreviousButton(){
        showHideActionButton(fabPrev, false)
        if (!videoList.isNullOrEmpty()) {
            currentVideoPlaying = videoList[FIRST_VIDEO_POSITION_ZERO]
            currentPosition = FIRST_VIDEO_POSITION_ZERO
        }
    }

    fun showHideActionButton(fab : FloatingActionButton, show: Boolean){
        if (show){
            fab.alpha = SHOWBUTTON_ALPHAONEDOTZERO
            fab.isEnabled = true
        } else {
            fab.alpha = FADEBUTTON_ALPHAZERODOTTWO
            fab.isEnabled = false
        }
    }

    fun setNextVideoAndHideActionButtons(){
        if (!videoList.isNullOrEmpty() && currentPosition < videoList.size-1) {
            currentVideoPlaying = videoList[currentPosition + 1]
            currentPosition += 1
            showHideActionButton(fabPrev, true)
        } else {
            Toast.makeText(this, "Last video in the list" , Toast.LENGTH_LONG).show()
            currentVideoPlaying = videoList[videoList.size-1]
            showHideActionButton(fabNext, false)
        }
        initVideoPrepareToPlay()
    }

    fun setPreviousVideoAndHideActionButtons(){
        if (!videoList.isNullOrEmpty() && currentPosition > 0) {
            currentVideoPlaying = videoList[currentPosition - 1]
            currentPosition -= 1
            showHideActionButton(fabNext, true)
        } else {
            Toast.makeText(this, "First video in the list" , Toast.LENGTH_LONG)
                .show()
            currentVideoPlaying = videoList[FIRST_VIDEO_POSITION_ZERO]
            showHideActionButton(fabPrev, false)
        }
        initVideoPrepareToPlay()
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
                    totalVideos = videoList.size
                    setFirstVideoAndHidePreviousButton()
                    sortVideosByDateDescending()
                    initVideoPrepareToPlay()
                } else {
                    Toast.makeText(this@MainActivity, "Failed to get the video list: " + response.message(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ArrayList<VideoData>?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to get the data" + t.message, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun sortVideosByDateDescending(){
        if (videoList.isNullOrEmpty()){
            Log.e(TAG, "Video list null or empty, sort not possible")
        } else {
            videoList.sortByDescending { it.publishedAt }
        }
    }

    fun initVideoPrepareToPlay(){
        if (!videoList.isEmpty()) {
            videoView.setVideoURI(currentVideoPlaying.fullURL.toUri())
            videoView.setVideoPath(currentVideoPlaying.fullURL)
            tvTitle.setText(currentVideoPlaying.title)
            tvAuthor.setText(currentVideoPlaying.author.name)
            setTextViewMarkdown(textView, currentVideoPlaying)
        } else {
            Toast.makeText(this@MainActivity, "No video to play right now, try again later", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun playVideoSetVideoDescriptionAndHidePlayButtons(){
        showHideVideoControlButtons(false)
        setTextViewMarkdown(textView, currentVideoPlaying)
        videoView.start()
    }

    fun setTextViewMarkdown(tv : TextView, videodata: VideoData){
        val markwon = Markwon.create(this)
        markwon.setMarkdown(tv, videodata.description)
    }

    fun showHideVideoControlButtons(show: Boolean){
        if (show){
            fabPlay.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.play))
            fabPlay.alpha = SHOWBUTTON_ALPHAONEDOTZERO
            fabPrev.alpha = SHOWBUTTON_ALPHAONEDOTZERO
            fabNext.alpha = SHOWBUTTON_ALPHAONEDOTZERO
        }else {
            fabPlay.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.pause))
            fabPlay.alpha = FADEBUTTON_ALPHAZERODOTTWO
            fabPrev.alpha = FADEBUTTON_ALPHAZERODOTTWO
            fabNext.alpha = FADEBUTTON_ALPHAZERODOTTWO
        }
    }
}