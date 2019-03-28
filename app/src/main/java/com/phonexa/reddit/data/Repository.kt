package com.phonexa.reddit.data

import com.phonexa.reddit.data.model.Post
import com.phonexa.reddit.data.net.RetrofitRedditService
import io.reactivex.Single

class Repository(private val service: RetrofitRedditService) {

    fun top(limit: Int, additionalParams: Map<String, String>?): Single<MutableList<Post>> {
        return service.top(limit, additionalParams)
            .map {
                val posts: MutableList<Post> = mutableListOf()

                for (child in it.data.children) {
                    val thumbnail = if (child.data.thumbnail == "self" ||
                        child.data.thumbnail == "image" ||
                        child.data.thumbnail == "default"
                    ) {
                        ""
                    } else {
                        child.data.thumbnail
                    }

                    posts.add(
                        Post(
                            child.data.name,
                            child.data.title,
                            child.data.author,
                            child.data.subreddit,
                            child.data.created,
                            child.data.score,
                            child.data.comments,
                            thumbnail,
                            child.data.postLink
                        )
                    )
                }

                posts
            }
    }

}