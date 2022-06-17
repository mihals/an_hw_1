package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post
import ru.netology.nmedia.activity.PostContentActivity
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.impl.FilePostRepository
import ru.netology.nmedia.impl.InMemoryPostRepository
import ru.netology.nmedia.impl.SharedPrefsPostRepository
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInteractionListener {
    private val repository: PostRepository =
        FilePostRepository(application)

    val data by repository::data
    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<Post?>()
    val playVideo = SingleLiveEvent<String>()

    override fun onLikeClicked(post: Post) = repository.like(post.id)

    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        sharePostContent.value = post.content
    }

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
        navigateToPostContentScreenEvent.value=post.copy()
    }

    override fun onPlayVideoClicked(post: Post) {
        val url = requireNotNull(post.video)
        playVideo.value = url
    }

    fun onUpdatePost(post:Post){
        repository.save(post)
    }

    fun onDeleteViewClicked(post:Post) = repository.delete(post.id)
    fun onAddClicked() {
        navigateToPostContentScreenEvent.call()
    }
}