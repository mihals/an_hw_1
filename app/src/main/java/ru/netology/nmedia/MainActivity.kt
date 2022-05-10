package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.impl.PostAdapter
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels <PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val l=viewModel::onLikeClicked
        val s=viewModel::onShareClicked

        val adapter = PostAdapter(viewModel :: onShareClicked,viewModel :: onLikeClicked)
        binding.postRecyclerView.adapter = adapter

        viewModel.data.observe(this){
            posts -> adapter.submitList(posts)
        }
    }


}
