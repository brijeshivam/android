package com.oyelabs.marvel.universe

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class DataFetch(private val cntxt: Context, private val rAdapter: RAdapter) {
    private var offSet = 0
    private val apiKey = "d173010f849caf0b923576d0b6eefb78"
    private val hash = "8be0cb8198ba49a46bdaed80ecc007be"

    var isLoading = false

    fun fetch() {
        val url = "https://gateway.marvel.com/v1/public/characters?offset=$offSet" +
                "&apikey=$apiKey&ts=1&hash=$hash"

        if (isLoading) return // if fetch is running already do nothing
        isLoading = true
        val dataList = ArrayList<CharactersData>()
        val queue = Volley.newRequestQueue(cntxt)
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val data = response.getJSONObject("data")
                val results = data.getJSONArray("results")
                for (i in 0 until results.length()) {
                    val objects = results.getJSONObject(i)
                    val name = objects.getString("name")
                    val thumbnail =
                        objects.getJSONObject("thumbnail")
                            .getString("path") + "/portrait_medium.jpg"
                    val link = objects.getJSONArray("urls").getJSONObject(0).getString("url")
                    val chrData = CharactersData(name, thumbnail, link)
                    dataList.add(chrData)
                }
                rAdapter.updateData(dataList)
                offSet += 20
                isLoading = false
            },
            {
                Toast.makeText(cntxt, "API limit reached or network error", Toast.LENGTH_SHORT)
                    .show()
            })
        queue.add(jsonRequest)

    }

    fun fetchSearch(query: String) {
        val searchName = query.replace(" ", "%20")
        val url = "https://gateway.marvel.com/v1/public/characters?name=$searchName" +
                "&apikey=$apiKey&ts=1&hash=$hash"

        val queue = Volley.newRequestQueue(cntxt)
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val data = response.getJSONObject("data")
                val results = data.getJSONArray("results")
                if (results.length() > 0) {
                    val objects = results.getJSONObject(0)
                    val name = objects.getString("name")
                    val thumbnail =
                        objects.getJSONObject("thumbnail")
                            .getString("path") + "/portrait_medium.jpg"
                    val link = objects.getJSONArray("urls").getJSONObject(0).getString("url")
                    val chrData = CharactersData(name, thumbnail, link)
                    rAdapter.search(chrData)
                } else {
                    Toast.makeText(cntxt, "Not Found", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            {
                Toast.makeText(cntxt, "Error", Toast.LENGTH_SHORT)
                    .show()
            })
        queue.add(jsonRequest)
    }


}