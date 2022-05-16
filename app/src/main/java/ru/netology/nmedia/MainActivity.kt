package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.util.hideKeyboard
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

        binding.saveButton.setOnClickListener {

            val caption = findViewById<View>(R.id.canselEditGroup)
            caption.visibility = View.GONE
            with(binding.content) {
                val content = binding.content.text.toString()
                viewModel.onSaveButtonClick(content)
                clearFocus()
                hideKeyboard()
            }
        }

            binding.cancelEditButton.setOnClickListener{
            val caption = binding.canselEditGroup
            caption.visibility = View.GONE
            binding.content.setText("")
            binding.content.clearFocus()
            binding.content.hideKeyboard()
        }

        viewModel.currentPost.observe(this){currentPost ->
            binding.content.setText(currentPost?.content)
        }
    }

    fun showCap(item : MenuItem):Boolean{
        val caption = findViewById<View>(R.id.canselEditGroup)
        caption.visibility = View.VISIBLE
        return false
    }
}
