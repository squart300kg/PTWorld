package com.example.ptworld.Activity

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ptworld.Adapter.AdapterBroadCast
import com.example.ptworld.DTO.BroadCast
import com.example.ptworld.DTO.ItemObject
import com.example.ptworld.R
import kotlinx.android.synthetic.main.activity_broad_cast_list.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.NullPointerException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class BroadCastListActivity : AppCompatActivity() {

    var list : ArrayList<BroadCast> = ArrayList()
    lateinit var adapter: AdapterBroadCast
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broad_cast_list)

        BroadCast_Thread().execute(getString(R.string.server_url) + "selectAllBroadCast.php")

    }
    inner class BroadCast_Thread() : AsyncTask<String, Void, String>() {
//        var items : List<Item> = items
        lateinit var progressDialog: ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(this@BroadCastListActivity)
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog.setMessage("방송을 불러오고 있습니다...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }


        override fun doInBackground(vararg params: String?): String? {
            val serverURL = params[0]

            var postParameters = ""

            Log.i("url : ", serverURL)

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
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            lateinit var broadCast: BroadCast
            try {
                val jsonArray = JSONArray(result)
                val total_json = jsonArray.length()
                Log.i("broad_cast갯수 : ", total_json.toString() + "")
                for (i in 1..total_json) {
                    val jsonObject = jsonArray.getJSONObject(i - 1)
//                    val itemObject = ItemObject(jsonObject.getString("subject"), jsonObject.getString("contents_url"), jsonObject.getString("thumbnail_url"))
                    Log.i("broad_cast_list_result : ", jsonObject.getString("title"))
                    Log.i("broad_cast_list_result : ", jsonObject.getString("streaming_url"))
                    Log.i("broad_cast_list_result : ", jsonObject.getString("thumbnail_url"))
                    broadCast = BroadCast(
                            jsonObject.getString("title"),
                            jsonObject.getString("streaming_url"),
                            jsonObject.getString("thumbnail_url")
                    )
                    list.add(broadCast)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val mainAdapter = AdapterBroadCast(list, this@BroadCastListActivity)
            mainAdapter.notifyDataSetChanged()
            boardCastRecyclerView.adapter = mainAdapter

            val layoutManager = LinearLayoutManager(applicationContext)
            boardCastRecyclerView.layoutManager = layoutManager
            boardCastRecyclerView.setHasFixedSize(true)

            progressDialog.dismiss()
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }



    }




}
