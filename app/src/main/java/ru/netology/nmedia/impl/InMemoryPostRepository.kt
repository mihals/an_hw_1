package ru.netology.nmedia.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.Post

class InMemoryPostRepository : PostRepository {
    override val data = MutableLiveData<Post>(
        Post(
            1,
            "Нетология. Университет интернет-профессий будущего.",
            "Привет! Это новая Нетология! Когдо-то Нетология была не очень-то, но сейчас Нетология ого-го!",
            "3 мая 2022 года в 12:00",
            999,
            1099,
            999_999,
            false
        )
    )

    override fun like() {
        val currentPost = checkNotNull(data.value)

        val likedPost = currentPost.copy(
            likedByMe = !currentPost.likedByMe
        )
        likedPost.numLikes = when {
            likedPost.likedByMe -> ++likedPost.numLikes
            else ->    --likedPost.numLikes}

        data.value = likedPost
    }

    override fun share(){
        val currentPost = checkNotNull(data.value)

        val sharedPost = currentPost.copy(
            numShares = ++currentPost.numShares
        )

        data.value = sharedPost
    }

    override fun view(){
        val currentPost = checkNotNull(data.value)

        val viewedPost = currentPost.copy(
            numViews = ++currentPost.numViews
        )

        data.value = viewedPost
    }
}