package com.silverorange.videoplayer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.silverorange.videoplayer.model.ResponseListVideos

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getVideoList()
        Log.i("TAG", "getting videos...")
    }

    fun getVideoList() {
        var retrofit = RetrofitClient.getInstance()
        var apiInterface = retrofit.create(VideoDataInterface::class.java)
        lifecycleScope.launchWhenCreated {
            try {
                val response = apiInterface.getAllVideos()
                if (response.isSuccessful()) {
                    Log.i("video import", "Successful import: "+ response.body())
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        response.errorBody().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }catch (Ex:Exception){
                Log.e("Error-BAD-BAD-BAD",Ex.localizedMessage)
            }
        }

    }
}