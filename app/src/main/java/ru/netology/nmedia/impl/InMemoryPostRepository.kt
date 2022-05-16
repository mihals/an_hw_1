package ru.netology.nmedia.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.Post

class InMemoryPostRepository() : PostRepository {

    private var nextId = GENERATED_POST_AMOUNT.toLong()
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

    override fun delete(postId: Long) {
        data.value = posts.filter{
            postId != it.id
        }
    }

    override fun save(post: Post) {
        if(post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun update(post:Post){
        data.value = posts.map{
            if(it.id == post.id) post else it
        }
    }

    private fun insert(post:Post){
        data.value = listOf(
            post.copy(
                id = ++nextId
            )
        ) + posts
    }

    private companion object {
        const val GENERATED_POST_AMOUNT = 20
    }
}