package ru.netology.nmedia.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.Post
import ru.netology.nmedia.databinding.PostContentFragmentBinding
import ru.netology.nmedia.util.SingleLiveEvent

class PostContentFragment:Fragment()  {

    private val args  by navArgs<PostContentFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            binding.edit.setText(args.initialContent)
            binding.edit.requestFocus()
            binding.ok.setOnClickListener{onOkButtonClicked(binding)}
        }.root



    private fun onOkButtonClicked(binding: PostContentFragmentBinding) {
        val text = binding.edit.text
        if (!text.isNullOrBlank()) {
            val resultBundle = Bundle(1)
            resultBundle.putString(RESULT_KEY, text.toString())
            setFragmentResult(REQUEST_KEY, resultBundle)
        }
        findNavController().popBackStack()
    }

    override fun onDestroy(){
        super.onDestroy()
    }

    companion object{
        const val REQUEST_KEY = "requestKey"
        const val RESULT_KEY = "postNewContent"


        private const val KEY_POST_ID = "postId"
        private lateinit var selectedPost:Post
    }
}