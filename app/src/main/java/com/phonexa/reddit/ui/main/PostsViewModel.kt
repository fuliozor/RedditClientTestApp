package com.phonexa.reddit.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.phonexa.reddit.data.NetworkState
import com.phonexa.reddit.data.datasource.PostsDataSourceFactory
import com.phonexa.reddit.data.model.Post
import io.reactivex.disposables.CompositeDisposable
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class PostsViewModel : ViewModel(), KoinComponent {
    val PAGE_SIZE: Int = 10

    private val compositeDisposable = CompositeDisposable()

    private val dataSourceFactory: PostsDataSourceFactory

    var posts: LiveData<PagedList<Post>>

    init {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PAGE_SIZE)
            .build()

        dataSourceFactory = PostsDataSourceFactory(get(), compositeDisposable)

        posts = LivePagedListBuilder<String, Post>(dataSourceFactory, config).build()
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return switchMap(dataSourceFactory.postsDataSource) {
            it.networkState
        }
    }

    fun refresh() {
        dataSourceFactory.postsDataSource.value?.invalidate()
    }
}