package com.example.ve441_lifesaver_draft

import android.util.Log
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.reflect.full.declaredMemberProperties
import okhttp3.*
import androidx.databinding.ObservableArrayList
import java.io.IOException


class AEDs (var id: String? = null,
            var description: String? = null,
            var location: JSONObject? = null)


object AEDStore{
    val aeds = ObservableArrayList<AEDs>()
    private val nFields = AEDs::class.declaredMemberProperties.size

    private val client = OkHttpClient()
    private const val serverUrl = "https://10.0.0.102/"

    fun getAEDs() {
        val request = Request.Builder()
            .url(serverUrl+"getAEDs/")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("getAEDs", "Failed GET request")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val aedReceived = try { JSONObject(response.body?.string() ?: "")
                        .getJSONArray("AEDs") } catch (e: JSONException) { JSONArray() }

                    aeds.clear()
                    for (i in 0 until aedReceived.length()) {
                        val aedEntry = aedReceived[i] as JSONArray
                        if (aedEntry.length() == nFields) {
                            aeds.add(AEDs(id = aedEntry[0].toString(),
                                description = aedEntry[1].toString(),
                                location = JSONObject(aedEntry[2].toString()),
                            ))
                        } else {
                            Log.e("getAEDs", "Received unexpected number of fields "
                                    + aedEntry.length().toString()
                                    + " instead of " + nFields.toString())
                        }
                    }
                }
            }
        })
    }
}