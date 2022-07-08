package ru.netology.nmedia.viewModel

import android.app.Application
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.impl.FilePostRepository
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInteractionListener {
    private val repository: PostRepository =
        FilePostRepository(application)

    val data by repository::data
    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<String>()
    val playVideo = SingleLiveEvent<String>()
    val navigateToSinglePostFragmentEvent = SingleLiveEvent<View?>()
    val navigateToFeedFragmentFromScrollPost = SingleLiveEvent<String?>()
    var singlePostView = MutableLiveData<View?>()


    override fun onLikeClicked(post: Post) = repository.like(post.id)

    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        sharePostContent.value = post.content

    }

    override fun onRemoveClicked(post: Post) {
        //currentPost.value=post
        //findNavController(R.id.nav_graph)
        repository.delete(post.id)
        navigateToFeedFragmentFromScrollPost.call()
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
        currentPost.value=post
        if(sharedView!=null)
        {
        }
        navigateToPostContentScreenEvent.value = post.content
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

    override fun onPostCardClicked(postView: View) {
        if(sharedView==null){
            sharedView=postView
            navigateToSinglePostFragmentEvent.call()
        }

    }

    override fun onRemoveFromScrolledPost() {
        val obs = navigateToFeedFragmentFromScrollPost.hasObservers()
        val activeObs = navigateToFeedFragmentFromScrollPost.hasActiveObservers()
        //navigateToFeedFragmentFromScrollPost.value="str"
        navigateToFeedFragmentFromScrollPost.call()
    }

    companion object{
        var sharedView:View? = null
    }
}