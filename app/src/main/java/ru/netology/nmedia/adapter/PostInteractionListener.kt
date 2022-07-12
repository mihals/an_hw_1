package ru.netology.nmedia.adapter

import android.view.View
import ru.netology.nmedia.Post

interface PostInteractionListener {
    fun onLikeClicked(post:Post)

    fun onShareClicked(post:Post)

    fun onRemoveClicked(post:Post)

    fun onSaveButtonClick(content:String)

    fun onEditClicked(post:Post)

    fun onPostCardClicked(postView: View)

    fun onRemoveFromScrolledPost()

    abstract fun onPlayVideoClicked(post: Post)
    //action_feedFragment_to_scrollPostFragment

}