package com.phonexa.reddit.data.datasource

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.ItemKeyedDataSource
import com.phonexa.reddit.data.*
import com.phonexa.reddit.data.model.Post
import io.reactivex.disposables.CompositeDisposable

class PostsDataSource(
    private val repository: Repository,
    private val compositeDisposable: CompositeDisposable
) : ItemKeyedDataSource<String, Post>() {
    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<Post>) {
        loadData(params.requestedLoadSize, null, null, callback)
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<Post>) {
        loadData(params.requestedLoadSize, "after", params.key, callback)
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<Post>) {
        loadData(params.requestedLoadSize, "before", params.key, callback)
    }

    override fun getKey(item: Post): String {
        return item.name
    }

    private fun loadData(
        requestedLoadSize: Int,
        paramName: String?,
        paramValue: String?,
        callback: LoadCallback<Post>
    ) {
        networkState.postValue(StateLoading)

        val params = mutableMapOf<String, String>()

        if (paramName != null && paramValue != null) {
            params[paramName] = paramValue
        }

        compositeDisposable.add(
            repository.top(
                requestedLoadSize,
                params
            ).subscribe({
                networkState.postValue(StateLoaded)
                callback.onResult(it)
            }, {
                val error = StateError(it.message)
                networkState.postValue(error)
            })
        )
    }
}
