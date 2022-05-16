package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding

internal class PostAdapter(
    private val interactionListener:PostInteractionListener
) : ListAdapter<Post,PostAdapter.ViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater,parent,false)
        return ViewHolder(binding,interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: PostBinding,
        listener:PostInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post : Post

        private val popupMenu by lazy{
            PopupMenu(itemView.context,binding.options ).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { item ->
                    when(item.itemId){
                        R.id.remove -> {
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init{
            binding.share.setOnClickListener { listener.onShareClicked(post) }
            binding.likes.setOnClickListener { listener.onLikeClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                author.text = post.author
                published.text = post.published
                postContent.text = post.content
                share.text = numToString(post.numShares)
                likes.text = numToString(post.numLikes)
                //likes.setButtonDrawable(getLikesIconResId(post.likedByMe))
                likes.isChecked=post.likedByMe
                options.setOnClickListener {popupMenu.show()}
            }
        }

        private fun getLikesIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_baseline_favorite_24 else ru.netology.nmedia.R.drawable.ic_baseline_favorite_border_24

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