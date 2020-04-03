package com.example.ptworld.Activity

import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ptworld.R
import com.pedro.encoder.input.video.CameraHelper
import com.pedro.encoder.input.video.CameraOpenException
import com.pedro.rtplibrary.rtmp.RtmpCamera1
import kotlinx.android.synthetic.main.activity_go_broad_cast.*
import net.ossrs.rtmp.ConnectCheckerRtmp
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class GoBroadCast : AppCompatActivity(), ConnectCheckerRtmp, View.OnClickListener {

    lateinit var rtmpCamera1: RtmpCamera1
    var currentDateAndTime = ""

    private val folder = File(Environment.getExternalStorageDirectory().absolutePath
            + "/피지컬갤러리 동영상")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_go_broad_cast)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        rtmpCamera1 = RtmpCamera1(surfaceView, this)

        prepareOptions();
        b_start_stop.setOnClickListener(this)
        b_record.setOnClickListener(this)
        switch_camera.setOnClickListener(this)
    }

    private fun prepareOptions() {

    }

    override fun onAuthSuccessRtmp() {
        runOnUiThread { Toast.makeText(this@GoBroadCast, "Auth success", Toast.LENGTH_SHORT).show() }
    }

    override fun onNewBitrateRtmp(bitrate: Long) {
        runOnUiThread { tv_bitrate.setText("$bitrate bps") }

    }

    override fun onConnectionSuccessRtmp() {
        runOnUiThread { Toast.makeText(this@GoBroadCast, "Connection success", Toast.LENGTH_SHORT).show() }
    }

    override fun onConnectionFailedRtmp(reason: String) {
        runOnUiThread {
            Toast.makeText(this@GoBroadCast, "Connection failed. $reason", Toast.LENGTH_SHORT)
                    .show()
            rtmpCamera1.stopStream()
            b_start_stop.setText(resources.getString(R.string.start_button))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                    && rtmpCamera1.isRecording) {
                rtmpCamera1.stopRecord()
                b_record.setText(R.string.start_record)
                Toast.makeText(this@GoBroadCast,
                        "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath(),
                        Toast.LENGTH_SHORT).show()
                currentDateAndTime = ""
            }
        }

    }

    override fun onAuthErrorRtmp() {
        runOnUiThread { Toast.makeText(this@GoBroadCast, "Auth error", Toast.LENGTH_SHORT).show() }
    }

    override fun onDisconnectRtmp() {
        runOnUiThread {
            Toast.makeText(this@GoBroadCast, "Disconnected", Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                    && rtmpCamera1.isRecording) {
                rtmpCamera1.stopRecord()
                b_record.setText(R.string.start_record)
                Toast.makeText(this@GoBroadCast,
                        "file " + currentDateAndTime + ".mp4 saved in " + folder.absolutePath,
                        Toast.LENGTH_SHORT).show()
                currentDateAndTime = ""
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.b_start_stop ->
            {
                Log.d("TAG_R", "b_start_stop: ")
                if (!rtmpCamera1.isStreaming) {
                    b_start_stop.text = resources.getString(R.string.stop_button)

//                    rtmpCamera1.setAuthorization(
//                            getString(R.string.wowza_user),
//                            getString(R.string.wowza_user)
//                    )

                    rtmpCamera1.setAuthorization(
                            "squart300kg",
                            "squart1126"
                    )

                    if (rtmpCamera1.isRecording || prepareEncoders()) {
                        var rtmp_url = getString(R.string.rtmp_url) + rtmp_title.text
                        Log.i("rtmp_url : ", rtmp_url)
                        rtmpCamera1.startStream(rtmp_url)
                    } else {
                        Toast.makeText(this, "Error preparing stream, This device cant do it",
                                Toast.LENGTH_SHORT).show()
                        b_start_stop.text = resources.getString(R.string.start_button)
                    }
                } else {
                    b_start_stop.text = resources.getString(R.string.start_button)
                    rtmpCamera1.stopStream()
                }
            }
            R.id.b_record -> {
                Log.d("TAG_R", "b_start_stop: ")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (!rtmpCamera1.isRecording) {
                        try {
                            if (!folder.exists()) {
                                folder.mkdir()
                            }
                            val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                            currentDateAndTime = sdf.format(Date())
                            if (!rtmpCamera1.isStreaming) {
                                if (prepareEncoders()) {
                                    rtmpCamera1.startRecord(
                                            folder.absolutePath + "/" + currentDateAndTime + ".mp4")
                                    b_record.setText(R.string.stop_record)
                                    Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Error preparing stream, This device cant do it",
                                            Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                rtmpCamera1.startRecord(
                                        folder.absolutePath + "/" + currentDateAndTime + ".mp4")
                                b_record.setText(R.string.stop_record)
                                Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: IOException) {
                            rtmpCamera1.stopRecord()
                            b_record.setText(R.string.start_record)
                            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        rtmpCamera1.stopRecord()
                        b_record.setText(R.string.start_record)
                        Toast.makeText(this,
                                "file " + currentDateAndTime + ".mp4 saved in " + folder.absolutePath,
                                Toast.LENGTH_SHORT).show()
                        currentDateAndTime = ""
                    }
                } else {
                    Toast.makeText(this, "You need min JELLY_BEAN_MR2(API 18) for do it...",
                            Toast.LENGTH_SHORT).show()
                }
            }
            R.id.switch_camera -> try {
                rtmpCamera1.switchCamera()
            } catch (e: CameraOpenException) {
                Toast.makeText(this@GoBroadCast, e.message, Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }
    }
    private fun prepareEncoders(): Boolean {
//        val resolution : Camera.size = rtmpCamera1.resolutionsBack[spResolution.getSelectedItemPosition()]

        val resolution : Camera.Size = rtmpCamera1.resolutionsBack.get(0)
        val width = resolution.width
        val height = resolution.height
        Log.e("width : " , width.toString())
        Log.e("height : " , height.toString())

//        val width = 1080
//        val height = 1920
        return (rtmpCamera1.prepareVideo(
                width,
                height,
                Integer.parseInt(getString(R.string.FPS)),
                getString(R.string.video_bitrate).toInt() * 1024,
                false,
                CameraHelper.getCameraOrientation(this))
                && rtmpCamera1.prepareAudio(
                getString(R.string.audio_bitrate).toInt() * 1024,
                getString(R.string.sample_rate).toInt(),
                true,
                false,
                false)
                )
    }


}
