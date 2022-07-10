package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity.apply
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat.apply
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.ScrollPostFragment
import ru.netology.nmedia.ScrollPostFragmentDirections
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FeedFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel
import java.lang.ProcessBuilder.Redirect.to

class FeedFragment : Fragment() {

    private val viewModel by viewModels <PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.sharePostContent.observe(this) { postContent ->
            //val srcFragment = findNavController()?.currentDestination?.id
            //val feedFragmentId= R.id.scrollPostFragment

            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
            //PostViewModel.sharedView?.update()
        }

        setFragmentResultListener(requestKey = PostContentFragment.REQUEST_KEY){
            requestKey, bundle ->
            if(requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(PostContentFragment.RESULT_KEY
            )?: return@setFragmentResultListener
            viewModel.onSaveButtonClick(newPostContent)
        }

        viewModel.navigateToPostContentScreenEvent.observe(this){initialContent ->
            val curFragment = findNavController().currentDestination?.id
            val feedFragment = findNavController().findDestination(R.id.feedFragment)?.id
            val scrFragment = findNavController().findDestination(R.id.scrollPostFragment)?.id

            if(PostViewModel.isEditHandled) return@observe
            if (findNavController().currentDestination?.id ==
                findNavController().findDestination(R.id.feedFragment)?.id
            ) {
                val direction = FeedFragmentDirections.toPostContentFragment(initialContent)
                findNavController().navigate(direction)
            }

            if (findNavController().currentDestination?.id ==
                findNavController().findDestination(R.id.scrollPostFragment)?.id
            ) {
                val direction = ScrollPostFragmentDirections.scrollToPostContentFragment(initialContent)
                findNavController().navigate(direction)
            }
            PostViewModel.isEditHandled = true

        }

        viewModel.playVideo.observe(this) { videoUrl ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
            startActivity(intent)
        }

        viewModel.navigateToSinglePostFragmentEvent.observe(this) {
            val jsonPost = Gson().toJson(it)

            findNavController().navigate(
                R.id.scrollPostFragment,
                ScrollPostFragment.createBundle(jsonPost)
            )
        }

//        viewModel.navigateToFeedFragmentFromScrollPost.observe(this){
//            //val direction = ScrollPostFragmentDirections
//            //    .scrollPostFragmentToFeedFragment()
//            val ctr = findNavController()//.navigate(direction)
//        }



//        val postContentActivityLauncher =
//            registerForActivityResult(PostContentActivity.ResultContract)
//            { editIntent ->
//                val postId = editIntent?.getLongExtra("postId", 0L)
//                val postContent = editIntent?.getStringExtra("postNewContent")
//                editIntent ?: return@registerForActivityResult
//                postContent ?: return@registerForActivityResult
//
//                if (postId == 0L) {
//                    viewModel.onSaveButtonClick(postContent)
//                }
//                else if(postId!=null){
//                    val srcPost = PostContentActivity.ResultContract.getSrcPost()
//                    val resultPost = srcPost.copy(id=postId,content = postContent)
//                    viewModel.onUpdatePost(resultPost)
//                }
//            }

//        val postContentActivityLauncher =
//            registerForActivityResult(PostContentActivity.ResultContract)
//            { postContent ->
//                postContent?: return@registerForActivityResult
//                viewModel.onSaveButtonClick(postContent)
//            }

//        viewModel.navigateToPostContentScreenEvent.observe(this) {
//            //intent.putExtra("postContent", it?.content)
//            postContentActivityLauncher.launch()
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            PostViewModel.sharedView = null
            val adapter = PostAdapter(viewModel)
            binding.postRecyclerView.adapter = adapter
            viewModel.data.observe(viewLifecycleOwner){
                    posts -> adapter.submitList(posts)
            }
            binding.fab.setOnClickListener {
                viewModel.onAddClicked()
            }

        }.root

    override fun onDestroy(){
        super.onDestroy()
    }

    fun getCurrentPost(): Boolean {
        return true
    }


}

