package ru.netology.nmedia.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.android.material.chip.ChipDrawable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.Post
import kotlin.properties.Delegates

class FilePostRepository(
    private val application:Application
) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    val prefs =  application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE)


    private var nextId by Delegates.observable(
        prefs.getLong(NEXT_ID_PREFS_KEY, 0L)
    ) { _, _, newValue ->
        prefs.edit { putLong(NEXT_ID_PREFS_KEY, newValue) }
    }

    private var posts
        get() = checkNotNull(data.value) { "" }
        set(value){
            application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE
            ).bufferedWriter().use{
                it.write(gson.toJson(value))
            }
            data.value = value
        }

    override fun like(id: Long) {
        posts = posts.map {
            if (it.id != id) it else {
                if (it.likedByMe) --it.numLikes
                else ++it.numLikes
                it.copy(likedByMe = !it.likedByMe)
            }
        }
    }

    override val data:MutableLiveData<List<Post>>

    init{
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts:List<Post> = if(postsFile.exists()){
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use{gson.fromJson(it, type)}
        }else emptyList()

        if(nextId==0L && !posts.isEmpty()){
            nextId = posts.maxOfOrNull { it.id }!! + 1L
        }

        posts.first().video =  "https://www.youtube.com/watch?v=cqOdV4mDPXc&list=WL&index=20"

        data = MutableLiveData(posts)
    }

    override fun share(id: Long) {
        data.value = posts.map {
            if (it.id != id) it else {
                it.copy(numShares = it.numShares+1)
            }
        }
    }

    override fun delete(postId: Long) {
        posts = posts.filter{
            postId != it.id
        }
    }

    override fun save(post: Post) {
        if(post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun update(post:Post){
        posts = posts.map{
            if(it.id == post.id) post else it
        }
    }

    private fun insert(post:Post){
        posts = listOf(
            post.copy(
                id = ++nextId
            )
        ) + posts
    }

    private companion object {
        const val GENERATED_POST_AMOUNT = 20
        const val POSTS_PREFS_KEY = "posts"
        const val NEXT_ID_PREFS_KEY = "posts"
        const val FILE_NAME = "posts.json"
    }
}