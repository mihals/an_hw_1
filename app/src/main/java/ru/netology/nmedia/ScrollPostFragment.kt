package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.gson.Gson
import ru.netology.nmedia.databinding.ScrollPostFragmentBinding
import ru.netology.nmedia.ui.FeedFragment
import ru.netology.nmedia.ui.PostContentFragment
import ru.netology.nmedia.viewModel.PostViewModel

class ScrollPostFragment : Fragment() {
    //val m = navGraphViewModels<PostViewModel>(R.id.feedFragment)
    private val viewModel:PostViewModel by viewModels()
    //private val viewModel:PostViewModel by viewModels ({ requireParentFragment() })
    private val sc = view?.findViewById<View>(R.id.linearContainer)

    private val jsonPost
        get() = requireArguments().getString("jsonPost")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel.sharePostContent.observe(this) { postContent ->
//            val intent = Intent().apply {
//                action = Intent.ACTION_SEND
//                putExtra(Intent.EXTRA_TEXT, postContent)
//                type = "text/plain"
//            }
//
//            val shareIntent = Intent.createChooser(
//                intent, getString(R.string.chooser_share_post)
//            )
//            startActivity(shareIntent)
//        }

//        viewModel.navigateToFeedFragmentFromScrollPost.observe(this){it ->
//            findNavController().popBackStack()
//        }
//        viewModel.navigateToFeedFragmentFromScrollPost.observe(this){
//            val direction = ScrollPostFragmentDirections
//                .scrollPostFragmentToFeedFragment()
//            findNavController().navigate(direction)
//        }
//        viewModel.navigateToFeedFragmentFromScrollPost.hasObservers()
//        viewModel.navigateToFeedFragmentFromScrollPost.hasActiveObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val scrollContainer = PostViewModel.sharedView
//        (scrollContainer?.parent as ViewGroup).removeView(scrollContainer)
//        layoutInflater.inflate(R.layout.scroll_post_fragment, null, false)
//        (scrollContainer.findViewById(R.id.linearContainer) as ViewGroup).addView(scrollContainer)
        (PostViewModel.sharedView?.parent as ViewGroup).removeView(PostViewModel.sharedView)
        val scrollContainer = layoutInflater.inflate(R.layout.scroll_post_fragment, null, false)
        (scrollContainer.findViewById(R.id.linearContainer) as ViewGroup).addView(PostViewModel.sharedView)

//        viewModel.navigateToFeedFragmentFromScrollPost.observe(this) { it ->
//            findNavController().popBackStack()
//        }

//        val scrollContainer = PostViewModel.sharedView
//        (scrollContainer?.parent as ViewGroup).removeView(scrollContainer)
//        val myView = layoutInflater.inflate(R.layout.scroll_post_fragment, container, false)
//        view.findViewById<>()
//        myView.findViewById(R.layout.scroll_post_fragment)
        //(view as View).findViewById()
        //layoutInflater.inflate(R.layout.scroll_post_fragment, null, false)

            //(scrollContainer.findViewById(R.id.linearContainer) as ViewGroup).addView(scrollContainer)

//        viewModel.navigateToFeedFragmentFromScrollPost.observe(this) { it ->
//            findNavController().popBackStack()
//        }
            return scrollContainer
    }

    override fun onResume() {
        super.onResume()
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