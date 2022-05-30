package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels <PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(viewModel)
        binding.postRecyclerView.adapter = adapter

        viewModel.data.observe(this){
            posts -> adapter.submitList(posts)
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }


        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
        }

        val postContentActivityLauncher =
            registerForActivityResult(PostContentActivity.ResultContract)
            { editIntent ->
                val postId = editIntent?.getLongExtra("postId", 0L)
                val postContent = editIntent?.getStringExtra("postNewContent")
                editIntent ?: return@registerForActivityResult
                postContent ?: return@registerForActivityResult

                if (postId == 0L) {
                    viewModel.onSaveButtonClick(postContent)
                }
                else if(postId!=null){
                    val srcPost = PostContentActivity.ResultContract.getSrcPost()
                    val resultPost = srcPost.copy(id=postId,content = postContent)
                    viewModel.onUpdatePost(resultPost)
                }
            }
        viewModel.navigateToPostContentScreenEvent.observe(this) {
            intent.putExtra("postContent", it?.content)
            postContentActivityLauncher.launch(it)
        }
    }

}

