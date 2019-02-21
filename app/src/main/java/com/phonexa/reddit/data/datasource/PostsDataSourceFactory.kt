package com.phonexa.reddit.data.datasource

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.phonexa.reddit.data.Repository
import com.phonexa.reddit.data.model.Post
import io.reactivex.disposables.CompositeDisposable

class PostsDataSourceFactory(
    private val repository: Repository,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<String, Post>() {

    val postsDataSource = MutableLiveData<PostsDataSource>()

    override fun create(): DataSource<String, Post> {
        val dataSource = PostsDataSource(repository, compositeDisposable)
        postsDataSource.postValue(dataSource)
        return dataSource
    }

}