package com.phonexa.reddit.data.model

data class Post(
    var name: String,
    var title: String,
    var author: String,
    var subreddit: String,
    var created: Long,
    var score: Long,
    var comments: Long,
    var thumbnail: String?,
    var postLink: String
)