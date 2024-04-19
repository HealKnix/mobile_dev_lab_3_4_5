package com.example.lab_3_4_5.Adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_3_4_5.Models.LikedPost
import com.example.lab_3_4_5.Models.Post
import com.example.lab_3_4_5.Models.User
import com.example.lab_3_4_5.R
import com.example.lab_3_4_5.databinding.PostCardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostHolder>() {
    companion object {
        private val firebaseRef = FirebaseDatabase.getInstance().getReference("LikedPosts")

        val postList = mutableListOf<Post>(
            Post(
                0,
                "Пост №1",
                "Идейные соображения высшего порядка, а также дальнейшее развитие различных форм деятельности требует от нас анализа переосмысления внешнеэкономических политик.",
                0,
                234323324,
                true
            ),
            Post(
                1,
                "Пост №2",
                "Сложно сказать, почему ключевые особенности структуры проекта, превозмогая сложившуюся непростую экономическую ситуацию, преданы социально-демократической анафеме.",
                0,
                0,
                false
            ),
            Post(
                2,
                "Пост №3",
                "Также как постоянное информационно-пропагандистское обеспечение нашей деятельности предопределяет высокую востребованность первоочередных требований.",
                0,
                532,
                true
            ),
            Post(
                3,
                "Пост №4",
                "Предварительные выводы неутешительны: новая модель организационной деятельности однозначно фиксирует необходимость модели развития. Мы вынуждены отталкиваться от того, что перспективное планирование прекрасно подходит для реализации стандартных подходов. Вот вам яркий пример современных тенденций — социально-экономическое развитие предполагает независимые способы реализации экономической целесообразности принимаемых решений.",
                0,
                23,
                false
            )
        )

//        fun fetchPostData(callback: (MutableList<Post>) -> Unit) {
//            firebaseRef.addValueEventListener(object: ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()) {
//                        for (postSnap in snapshot.children) {
//                            val post = postSnap.getValue(Post::class.java)!!
//                            postList.add(post)
//                        }
//                    }
//                    callback(postList)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.d("sign_up", "Failed to read value.", error.toException())
//                    callback(mutableListOf<Post>()) // передаем пустой список в случае ошибки
//                }
//            })
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.PostHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_card, parent, false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.PostHolder, position: Int) {
        holder.bind(postList[position])
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun addPost(post: Post) {
        postList.add(post)
        notifyDataSetChanged()
    }

    fun getPosts(): MutableList<Post> {
        return postList
    }

    fun clearPosts() {
        postList.clear()
        notifyDataSetChanged()
    }

    class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = PostCardBinding.bind(itemView)

        fun bind(post: Post) {
            binding.postCardBtn.setOnClickListener {
                if (post.isLiked) {
                    binding.postLikeCount.setText(((binding.postLikeCount.text.toString()).toInt() - 1).toString())

                    binding.postLikeIcon.setImageDrawable(getDrawable(itemView.context, R.drawable.icon_heart_like_outlined))
                    binding.postLikeBtn.setBackgroundColor(getColor(itemView.context, R.color.btn_like_color))

//                    LikedPost.fetchLikedPostData {
//                        val likedPostId = it.last().id
//                        val newLikedPost = LikedPost(
//                            likedPostId,
//                            post.id,
//                            User.getCurrentUser()?.id ?: -1,
//                            true
//                        )
//                        firebaseRef.child(likedPostId.toString()).setValue(newLikedPost)
//                    }
                } else {
                    binding.postLikeCount.setText(((binding.postLikeCount.text.toString()).toInt() + 1).toString())

                    binding.postLikeIcon.setImageDrawable(getDrawable(itemView.context, R.drawable.icon_heart_like_filled))
                    binding.postLikeBtn.setBackgroundColor(getColor(itemView.context, R.color.btn_like_active_color))


                    LikedPost.fetchLikedPostData {
                        val likedPostId = it.last().id
                        val newLikedPost = LikedPost(
                            likedPostId,
                            post.id,
                            User.getCurrentUser()?.id ?: -1,
                            false
                        )
                        firebaseRef.child(likedPostId.toString()).setValue(newLikedPost)
                    }
                }

                post.isLiked = !post.isLiked
            }

            binding.postTitle.text = post.title
            binding.postText.text = post.text
            binding.createdBy.text = "undefined"
            User.fetchUserData {
                for (user in it) {
                    if (user.id == post.createdBy) {
                        binding.createdBy.text = "@${user.login.lowercase()}"
                    }
                }
            }
            binding.postLikeCount.text = post.likes.toString()

            if (post.isLiked) {
                binding.postLikeIcon.setImageDrawable(AppCompatResources.getDrawable(itemView.context, R.drawable.icon_heart_like_filled))
                binding.postLikeBtn.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.btn_like_active_color))
            } else {
                binding.postLikeIcon.setImageDrawable(AppCompatResources.getDrawable(itemView.context, R.drawable.icon_heart_like_outlined))
                binding.postLikeBtn.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.btn_like_color))
            }
        }
    }
}