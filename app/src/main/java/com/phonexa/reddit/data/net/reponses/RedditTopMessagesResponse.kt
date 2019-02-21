package com.phonexa.reddit.data.net.reponses

import com.google.gson.annotations.SerializedName

data class RedditTopMessagesResponse(val data: Data)

data class Data(val children: List<MessageData>)

data class MessageData(val data: Post)

data class Resolution(val url: String)

data class Images(val source: Resolution?)

data class Preview(val images: List<Images>)

data class Post(
    val name: String,
    val title: String,
    val author: String,
    @SerializedName("subreddit_name_prefixed")
    val subreddit: String,
    @SerializedName("created_utc")
    val created: Long,
    val score: Long,
    @SerializedName("num_comments")
    val comments: Long,
    @SerializedName("permalink")
    val postLink: String,
    val thumbnail: String,
    val preview: Preview
)