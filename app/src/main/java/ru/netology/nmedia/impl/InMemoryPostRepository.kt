package ru.netology.nmedia.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.Post

class InMemoryPostRepository() : PostRepository {

    private val posts
        get() = checkNotNull(data.value) { "" }

    override fun like(id: Long) {
        data.value = posts.map {
            if (it.id != id) it else {
                if (it.likedByMe) --it.numLikes
                else ++it.numLikes
                it.copy(likedByMe = !it.likedByMe)
            }
        }
    }

    override val data = MutableLiveData(
        List(20){index ->
            Post(
                id=index+1L,
                author="Нетология",
                content="Пост ${index+1}",
                published="7 мая",
                numLikes = 999,
                numShares = 1,
                //var numViews: Int,
                likedByMe = false
            )
        }
    )

    override fun share(id: Long) {
        data.value = posts.map {
            if (it.id != id) it else {
                it.copy(numShares = it.numShares+1)
            }
        }
    }
}