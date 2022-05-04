package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val list:List<Int> = listOf(999,1000,1099,1100,1508,9999,10_105,1_000_100,1_200_235)
        //list.forEach{(println(it.toString()+" to "+numToString(it)))}

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            1,
            "Нетология. Университет интернет-профессий будущего.",
            "Привет! Это новая Нетология! Когдо-то Нетология была не очень-то, но сейчас Нетология ого-го!",
            "3 мая 2022 года в 12:00",
            999,
            1099,
            999_999,
            false
        )


        with(binding) {
            author.text = post.author
            postContent.text = post.content
            published.text = post.published
            likesValue.text = numToString(post.numLikes)
            shareValue.text = numToString(post.numShares)
            viewsValue.text = numToString(post.numViews)

            if (post.likedByMe) {
                likes.setImageResource(R.drawable.ic_baseline_favorite_24)
            }

            likes.setOnClickListener {
                if(post.likedByMe){
                    likesValue.text = numToString(--post.numLikes)
                }else{
                    likesValue.text = numToString(++post.numLikes).toString()
                }
                post.likedByMe = !post.likedByMe
                likes.setImageResource(
                    if (post.likedByMe) R.drawable.ic_baseline_favorite_24
                    else R.drawable.ic_baseline_favorite_border_24
                )
            }

            share.setOnClickListener{
                shareValue.text = numToString(++post.numShares)
            }

            views.setOnClickListener{
                viewsValue.text = numToString(++post.numViews)
            }
        }
    }

    fun numToString(value:Int):String{
        return  when{
            value >= 1_100_000 -> (value/1_000_000).toString() + "." +
                    ((value/100_000)%10).toString() + "M"
            value >= 1_000_000 -> "1M"
            value > 10_000 -> (value/1_000).toString()  + "K"
            value >= 1_100 -> (value/1_000).toString() + "." +
                    ((value/100)%10).toString() + "K"
            value >= 1_000 -> "1K"
            else -> value.toString()
        }
    }
}
