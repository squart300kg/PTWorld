package com.example.ptworld.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ptworld.R
import com.pedro.vlc.VlcListener
import com.pedro.vlc.VlcVideoLibrary
import kotlinx.android.synthetic.main.activity_seeing_broad_cast.*
import org.videolan.libvlc.MediaPlayer


class SeeingBroadCastActivity : AppCompatActivity(), VlcListener {

    private lateinit var vlcVideoLibrary: VlcVideoLibrary
    private lateinit var streaming_url : String
    private lateinit var title : String

    private val options = arrayOf(":fullscreen")
//    private val options : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_seeing_broad_cast)
        vlcVideoLibrary = VlcVideoLibrary(this@SeeingBroadCastActivity, this, surfaceView)
        vlcVideoLibrary.setOptions(options.toMutableList())

        var intent = getIntent()
        streaming_url = intent.getStringExtra("streaming_url")
        title = intent.getStringExtra("title")

        Log.i("SeeingBroadCastActivity : ", streaming_url)
        Log.i("SeeingBroadCastActivity : ", title)

    }

    override fun onResume() {
        super.onResume()
        vlcVideoLibrary.play(streaming_url)
    }

    override fun onPause() {
        super.onPause()
        vlcVideoLibrary.stop()
    }
    override fun onComplete() {
        Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show()
    }
    override fun onError() {
        Toast.makeText(this, "Error, make sure your endpoint is correct", Toast.LENGTH_SHORT).show()
        vlcVideoLibrary.stop()
//        bStartStop.text = "stop"
    }
    fun onBuffering(event: MediaPlayer.Event?) {}

    fun onClick(view: View?) {
        if (!vlcVideoLibrary.isPlaying) {
//            vlcVideoLibrary.play(etEndpoint.text.toString())
//            bStartStop.text = getString(R.string.stop_player)
        } else {
//            vlcVideoLibrary.stop()
//            bStartStop.text = getString(R.string.start_player)
        }
    }
}
