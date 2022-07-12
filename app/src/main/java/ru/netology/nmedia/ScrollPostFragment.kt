package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.gson.Gson
import ru.netology.nmedia.databinding.ScrollPostFragmentBinding
import ru.netology.nmedia.ui.FeedFragment
import ru.netology.nmedia.ui.PostContentFragment
import ru.netology.nmedia.util.SingleLiveEvent
import ru.netology.nmedia.viewModel.PostViewModel

class ScrollPostFragment : Fragment() {

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
            var share = (this.view?.findViewById(R.id.share) as TextView).text
                .toString().toIntOrNull()
            if(share != null)
                (view?.findViewById(R.id.share) as TextView).text =
                    (++share).toString()

            PostViewModel.isShareHandled = true
        }

        viewModel.playVideo.observe(this) { videoUrl ->
            if(PostViewModel.isPlayVideoHandled) return@observe
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
            startActivity(intent)
            PostViewModel.isPlayVideoHandled = true
        }

        viewModel.navigateToPostContentFromScrollPost.observe(this) { initialContent ->
            if(PostViewModel.isEditHandled) return@observe

            if (findNavController().currentDestination?.id ==
                findNavController().findDestination(R.id.scrollPostFragment)?.id
            ) {
                val direction =
                    ScrollPostFragmentDirections.scrollToPostContentFragment(initialContent)
                findNavController().navigate(direction)
            }
            PostViewModel.isEditHandled = true
        }

        setFragmentResultListener(requestKey = PostContentFragment.REQUEST_KEY){
                requestKey, bundle ->
            if(requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(PostContentFragment.RESULT_KEY
            )?: return@setFragmentResultListener
            (view?.findViewById(R.id.postContent) as TextView).setText(newPostContent)
            viewModel.onSaveButtonClick(newPostContent)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (PostViewModel.sharedView?.parent as ViewGroup).removeView(PostViewModel.sharedView)
        val scrollContainer = layoutInflater.inflate(R.layout.scroll_post_fragment, null, false)
        (scrollContainer.findViewById(R.id.linearContainer) as ViewGroup).addView(PostViewModel.sharedView)
        return scrollContainer//.findViewById(R.id.postContent)
    }

    override fun onDetach() {
        super.onDetach()

        (viewModel.currentPost.value?.content)?.let{
            viewModel.onSaveButtonClick(it)}
    }

    companion object{
        fun create(jsonPost: String)=PostContentFragment().apply{
            arguments = createBundle(jsonPost)
        }

        fun createBundle(jsonPost:String)=Bundle(1).apply {
            putString("jsonPost",jsonPost)
        }
    }
}