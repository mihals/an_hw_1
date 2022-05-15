package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import ru.netology.nmedia.Post

interface PostRepository {

    fun like(id:Long)

    val data:LiveData<List<Post>>

    fun share(id:Long)

    fun delete(postId:Long)

    fun save(post:Post)

    companion object{
        const val NEW_POST_ID = 0L
    }
}