package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.impl.InMemoryPostRepository

class PostViewModel:ViewModel(), PostInteractionListener {
    private val repository:PostRepository = InMemoryPostRepository()

    val data by repository::data

    override fun onLikeClicked(post : Post) = repository.like(post.id)

    override fun onShareClicked(post : Post) = repository.share(post.id)

    override fun onRemoveClicked(post: Post) {
        repository.delete(post.id)
    }

    val currentPost = MutableLiveData<Post?>(null)

    override fun onSaveButtonClick(content: String) {
        if(content.isBlank()) return

        val post = currentPost.value?.copy(
            content = content
        )?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "User",
            content = content,
            published = "Now",
            numShares = 0,
            numLikes = 0,
            likedByMe = false
        )
        repository.save(post)
        currentPost.value = null
    }

    override fun onEditClicked(post: Post) {
        currentPost.value = post
    }

    fun onDeleteViewClicked(post:Post) = repository.delete(post.id)
}