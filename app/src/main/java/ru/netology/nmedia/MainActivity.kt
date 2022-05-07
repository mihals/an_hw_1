package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels <PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.data.observe(this) { post ->
            with(binding){
                shareValue.text = numToString(post.numShares)
                viewsValue.text = numToString(post.numViews)
                likesValue.text = numToString(post.numLikes)
                likes.setImageResource(
                    when {
                        post.likedByMe -> R.drawable.ic_baseline_favorite_24
                        else -> R.drawable.ic_baseline_favorite_border_24
                    },
                )
            }
        }

        binding.likes.setOnClickListener {
            viewModel.onLikeClicked()
        }

        binding.share.setOnClickListener {
            viewModel.onShareClicked()
        }

        binding.views.setOnClickListener {
            viewModel.onViewClicked()
        }
    }


    fun numToString(value: Int): String {
        return when {
            value >= 1_100_000 -> (value / 1_000_000).toString() + "." +
                    ((value / 100_000) % 10).toString() + "M"
            value >= 1_000_000 -> "1M"
            value > 10_000 -> (value / 1_000).toString() + "K"
            value >= 1_100 -> (value / 1_000).toString() + "." +
                    ((value / 100) % 10).toString() + "K"
            value >= 1_000 -> "1K"
            else -> value.toString()
        }
    }
}
