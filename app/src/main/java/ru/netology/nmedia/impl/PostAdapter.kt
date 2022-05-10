package ru.netology.nmedia.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.databinding.PostBinding

typealias OnPostLikeClicked = (Post) -> Unit
typealias OnPostShareClicked = (Post) -> Unit

internal class PostAdapter(
    private val onLikeClicked : OnPostLikeClicked,
    private val onShareClicked : OnPostShareClicked
) : ListAdapter<Post,PostAdapter.ViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater,parent,false)
        return ViewHolder(binding,onShareClicked,onLikeClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: PostBinding,
        onLikeClicked : OnPostLikeClicked,
        onShareClicked : OnPostShareClicked
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post : Post
        init{
            binding.share.setOnClickListener { onShareClicked(post) }
            binding.likes.setOnClickListener { onLikeClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                author.text = post.author
                published.text = post.published
                postContent.text = post.content
                shareValue.text = numToString(post.numShares)
                likesValue.text = numToString(post.numLikes)
                likes.setImageResource(getLikesIconResId(post.likedByMe))
            }
        }

        private fun getLikesIconResId(liked: Boolean) =
            if (liked) ru.netology.nmedia.R.drawable.ic_baseline_favorite_24 else ru.netology.nmedia.R.drawable.ic_baseline_favorite_border_24

        private fun numToString(value: Int): String {
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

    private object DiffCallBack : DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post)=
            oldItem == newItem
    }
}