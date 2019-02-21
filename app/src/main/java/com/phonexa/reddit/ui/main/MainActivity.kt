package com.phonexa.reddit.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.phonexa.reddit.R
import com.phonexa.reddit.data.NetworkState
import com.phonexa.reddit.data.model.Post
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var postsViewModel: PostsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        postsViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)

        initRecyclerView()
        observeInternetStatus()
    }

    private fun initRecyclerView() {
        val adapter = PostsRecyclerViewAdapter {
            try {
                val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resources.getColor(R.color.colorAccent, null)
                } else {
                    resources.getColor(R.color.colorAccent)
                }

                val customTabsIntent = CustomTabsIntent.Builder().setToolbarColor(color).build()
                customTabsIntent.launchUrl(this, Uri.parse("https://www.reddit.com" + it.postLink))
            } catch (t: Throwable) {
                Snackbar.make(recyclerView, R.string.open_link_error, Snackbar.LENGTH_LONG).show()
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        postsViewModel.posts.observe(this, Observer<PagedList<Post>> { adapter.submitList(it) })
    }

    private fun observeInternetStatus() {
        swipeRefreshLayout.setOnRefreshListener { postsViewModel.refresh() }

        postsViewModel.getNetworkState().observe(this, Observer<NetworkState> {
            when (it) {
                NetworkState.LOADING -> swipeRefreshLayout.isRefreshing = true
                NetworkState.LOADED -> swipeRefreshLayout.isRefreshing = false
                else -> showError()
            }
        })
    }

    private fun showError() {
        swipeRefreshLayout.isRefreshing = false
        Snackbar.make(recyclerView, R.string.loading_data_error, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.btn_retry) {
                postsViewModel.refresh()
            }.show()
    }
}
