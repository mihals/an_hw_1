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
import androidx.fragment.app.activityViewModels
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

    private val viewModel:PostViewModel by activityViewModels ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.sharePostContent.observe(this) { postContent ->
            if(PostViewModel.isShareHandled) return@observe
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
            PostViewModel.isShareHandled = true
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
                findNavController().findDestination(R.id.feedFragment)?.id) {
                val direction = FeedFragmentDirections.toPostContentFragment(initialContent)
                findNavController().navigate(direction)
            }

            PostViewModel.isEditHandled = true
        }

        viewModel.playVideo.observe(this) { videoUrl ->
            if(PostViewModel.isPlayVideoHandled) return@observe
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
            startActivity(intent)
            PostViewModel.isPlayVideoHandled = true
        }

        viewModel.navigateToSinglePostFragmentEvent.observe(this) {
            findNavController().navigate(R.id.scrollPostFragment)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            PostViewModel.sharedView?.invalidate()
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
}

