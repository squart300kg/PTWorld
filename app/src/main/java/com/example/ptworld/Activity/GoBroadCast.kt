package com.example.ptworld.Activity

import android.app.ProgressDialog
import android.hardware.Camera
import android.os.*
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ptworld.Adapter.AdapterMain
import com.example.ptworld.DTO.ItemObject
import com.example.ptworld.R
import com.pedro.encoder.input.video.CameraHelper
import com.pedro.encoder.input.video.CameraOpenException
import com.pedro.rtplibrary.rtmp.RtmpCamera1
import kotlinx.android.synthetic.main.activity_go_broad_cast.*
import net.ossrs.rtmp.ConnectCheckerRtmp
import org.json.JSONArray
import org.json.JSONException
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

class GoBroadCast : AppCompatActivity(), ConnectCheckerRtmp, View.OnClickListener {

    lateinit var rtmpCamera1: RtmpCamera1
    var currentDateAndTime = ""
    private val handler = Handler()
    private val uiHandler = Handler()
    lateinit var liveTimer : Thread
    var isCounting = false
    var stream_key = 0

    private val folder = File(Environment.getExternalStorageDirectory().absolutePath
            + "/피지컬갤러리 동영상")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_go_broad_cast)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        rtmpCamera1 = RtmpCamera1(surfaceView, this)

        b_start_stop.setOnClickListener(this)
        b_record.setOnClickListener(this)
        switch_camera.setOnClickListener(this)
    }


    override fun onAuthSuccessRtmp() {
        runOnUiThread { Toast.makeText(this@GoBroadCast, "Auth success", Toast.LENGTH_SHORT).show() }
    }

    override fun onNewBitrateRtmp(bitrate: Long) {
        runOnUiThread { tv_bitrate.setText("$bitrate bps") }

    }

    override fun onConnectionSuccessRtmp() {
        runOnUiThread { Toast.makeText(this@GoBroadCast, "Connection success", Toast.LENGTH_SHORT).show() }
        startLiveCounting()

    }

    private fun startLiveCounting() {
        isCounting = true
        runOnUiThread{
            rtmp_title.visibility = View.GONE
            live_timer.visibility = View.VISIBLE
            live_timer.text = getString(R.string.publishing_label, 0L.format(), 0L.format())
        }

        ThreadStreamingStartStop().execute(
                getString(R.string.server_url) + "streamingStartStop.php",
                "start",
                rtmp_title.text.toString(),
                getString(R.string.rtmp_url),
                stream_key.toString())

        val startedAt = System.currentTimeMillis()
        var updatedAt = System.currentTimeMillis()
        liveTimer = Thread {
            while (isCounting) {
                if (System.currentTimeMillis() - updatedAt > 1000) {
                    updatedAt = System.currentTimeMillis()
                    handler.post {
                        val diff = System.currentTimeMillis() - startedAt
                        val second = diff / 1000 % 60
                        val min = diff / 1000 / 60
                        live_timer.text = getString(R.string.publishing_label, min.format(), second.format())
                    }
                }
            }
        }
        liveTimer?.start()
    }
    private fun stopCounting() {
        isCounting = false
        runOnUiThread{
            live_timer.text = ""
            live_timer.visibility = View.GONE
            rtmp_title.visibility = View.VISIBLE
        }
        liveTimer?.interrupt()
        ThreadStreamingStartStop().execute(getString(R.string.server_url) + "streamingStartStop.php",
                "stop",
                rtmp_title.text.toString(),
                getString(R.string.rtmp_url),
                stream_key.toString())

    }
    private fun Long.format(): String {
        return String.format("%02d", this)
    }

    override fun onConnectionFailedRtmp(reason: String) {
        runOnUiThread {
            Toast.makeText(this@GoBroadCast, "Connection failed. $reason", Toast.LENGTH_SHORT)
                    .show()
            stopCounting()
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
            stopCounting()
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
                        stream_key = Random().nextInt(10000)
                        var rtmp_url = getString(R.string.rtmp_url) + stream_key
                        Log.i("rtmp_url : ", rtmp_url)

                        rtmpCamera1.startStream(rtmp_url)
                    } else {
                        Toast.makeText(this, "Error preparing stream, This device cant do it",
                                Toast.LENGTH_SHORT).show()
                        b_start_stop.text = resources.getString(R.string.start_button)
                    }
                } else {
                    b_start_stop.text = resources.getString(R.string.start_button)
                    live_timer.visibility = View.GONE
                    rtmp_title.visibility = View.VISIBLE
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

    private class ThreadStreamingStartStop : AsyncTask<String?, Void?, String>() {

        override fun onPreExecute() { }

         override fun doInBackground(vararg params: String?): String {
            val serverURL = params[0]
            val type = params[1]
            val rtmp_title = params[2]
             val rtmp_url = params[3]
             val stream_key = params[4]

            var postParameters = ""

            if(type.equals("start")){
                Log.i("스트리밍 방송 create", " create")

                val streaming_url = rtmp_url + stream_key
                Log.i("streaming_url" , streaming_url)
                postParameters = "type=$type&rtmp_title=$rtmp_title&streaming_url=$streaming_url"
            } else {
                Log.i("스트리밍 방송 delete", " delete")
                postParameters = "type=$type&rtmp_title=$rtmp_title"
            }

            Log.i("url : ", serverURL)
            Log.i("parameter : ",postParameters)

            return try {
                val url = URL(serverURL)
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.readTimeout = 5000
                httpURLConnection.connectTimeout = 5000
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.connect()
                val outputStream = httpURLConnection.outputStream
                outputStream.write(postParameters.toByteArray(charset("UTF-8")))
                outputStream.flush()
                outputStream.close()
                val responseStatusCode = httpURLConnection.responseCode
                Log.d("ThreadStreamingStartStop", "POST response code - $responseStatusCode")
                val inputStream: InputStream
                inputStream = if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    httpURLConnection.inputStream
                } else {
                    httpURLConnection.errorStream
                }
                val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
                val bufferedReader = BufferedReader(inputStreamReader)
                val sb = StringBuilder()
                var line: String? = null
                while (bufferedReader.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                bufferedReader.close()
                sb.toString()
            } catch (e:Exception ) {

                Log.d("ThreadStreamingStartStop", "InsertData: Error ", e);

                return "Error: " + e.message
            }
        }
        override fun onPostExecute(result: String) { //DB로부터 데이터를 JSON형태로 받아온다.
            Log.i("streaming_start_stop_result", result)
//            try {
//                val jsonArray = JSONArray(result)
//                val total_json = jsonArray.length()
//                Log.i("JSON갯수_pain_ankle", total_json.toString() + "")
//                for (i in 1..total_json) {
//                    val jsonObject = jsonArray.getJSONObject(i - 1)
//                    val itemObject = ItemObject(jsonObject.getString("subject"), jsonObject.getString("contents_url"), jsonObject.getString("thumbnail_url"))
//
//                }
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
            //            progressDialog.dismiss();
        }
    }




}
