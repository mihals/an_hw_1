package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.Post
import ru.netology.nmedia.databinding.PostContentActivityBinding

class PostContentActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val postContent = intent.getStringExtra("postContent")
        val postId = intent.getLongExtra("postId",0L)

        val binding = PostContentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(postId != 0L){
            binding.edit.setText(postContent)
        }
        binding.edit.requestFocus()
        binding.ok.setOnClickListener{
            val postId = this.intent.getLongExtra(KEY_POST_ID,0L)
            val intent = Intent()
            val text = binding.edit.text
            if(text.isNullOrBlank()){
                setResult(Activity.RESULT_CANCELED,intent)
            }else{
                val content = text.toString()
                intent.putExtra(RESULT_KEY,content)
                intent.putExtra(KEY_POST_ID,postId)
                setResult(Activity.RESULT_OK,intent)
            }
            finish()
        }
    }

    object ResultContract: ActivityResultContract<Post?, Intent?>(){
        //val selectedPost:Post
        override fun createIntent(context: Context, input: Post?): Intent{
            val intent = Intent(context, PostContentActivity::class.java)
            if(input!=null) {
                intent.putExtra("postContent",input.content)
                intent.putExtra(KEY_POST_ID, input.id)
            }
            if (input != null) {
                selectedPost = input.copy()
            }
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?) =
            if(resultCode == Activity.RESULT_OK){
                intent
            }else null

        fun getSrcPost():Post{
            return selectedPost.copy()
        }
    }

    private companion object{
        private const val RESULT_KEY = "postNewContent"
        private const val KEY_POST_ID = "postId"
        private lateinit var selectedPost:Post
    }
}