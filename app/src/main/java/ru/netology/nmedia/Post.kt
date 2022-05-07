package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var numLikes: Int,
    var numShares: Int,
    var numViews: Int,
    var likedByMe: Boolean = false
)