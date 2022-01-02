package com.oyelabs.marvel.universe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, ItemClicked {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RAdapter
    private lateinit var scrollView: ScrollView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var dataFetch: DataFetch
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv)
        layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        adapter = RAdapter(this)
        recyclerView.adapter = adapter
        dataFetch = DataFetch(this.applicationContext, adapter)
        dataFetch.fetch()  //fetching data initially

        //Infinite Scrolling
        scrollView = findViewById(R.id.scrollView)
        scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > 0) {
                dataFetch.fetch() //fetching data on reaching end of the list
            }
        }


    }

    //Top search icon
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val search = menu?.findItem(R.id.app_bar_search)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        dataFetch.fetchSearch(query!!)      //Bring the searched item to 0 position
        layoutManager.scrollToPosition(0)
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        return true
    }

    override fun onItemClick(link: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(link))
    }
}