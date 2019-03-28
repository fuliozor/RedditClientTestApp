package com.phonexa.reddit.data.net

import com.phonexa.reddit.data.net.reponses.RedditTopMessagesResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RetrofitRedditService {

    @GET("/top.json")
    fun top(@Query("limit") limit: Int, @QueryMap additionalParams: Map<String, String>?): Single<RedditTopMessagesResponse>
}
