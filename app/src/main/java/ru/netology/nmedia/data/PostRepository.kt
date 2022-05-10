package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import ru.netology.nmedia.Post

interface PostRepository {

    //fun getAll(): LiveData<List<Post>>

    fun like(id:Long)

    val data:LiveData<List<Post>>

    //fun like()

    fun share(id:Long)

    //fun view()
}