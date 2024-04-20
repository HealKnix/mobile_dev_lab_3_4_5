package com.example.lab_3_4_5.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_3_4_5.Models.Post
import com.example.lab_3_4_5.Models.User
import com.example.lab_3_4_5.R
import com.example.lab_3_4_5.databinding.PostCardBinding
import com.google.firebase.database.FirebaseDatabase

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostHolder>() {
    companion object {
        private val firebaseRef = FirebaseDatabase.getInstance().getReference("Posts")
        val postList = mutableListOf<Post>()
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
            val userId = User.getCurrentUser()?.id ?: -1

            binding.postCardBtn.setOnClickListener {
                Log.d("qwerty", post.toString())
                Log.d("qwerty", User.getCurrentUser().toString())

                var isUserLikedPost = false

                post.likedByUsers.forEach {
                    if (it == userId) {
                        isUserLikedPost = true
                        return@forEach
                    }
                }

                if (isUserLikedPost) {
                    post.likedByUsers.remove(userId)
                } else {
                    post.likedByUsers.add(userId)
                }

                var likeCount = 0
                post.likedByUsers.forEach {
                    likeCount++
                }

                val newPost = Post(
                    post.id,
                    post.title,
                    post.text,
                    post.createdByUserId,
                    post.createdByUserName,
                    post.likedByUsers,
                    likeCount
                )

                firebaseRef.child(post.id.toString()).setValue(newPost)
                    .addOnCompleteListener {
                        Log.d("post_liked", "Пользователь с id{${userId}} поставил лайк на пост с id{${post.id}}")
                    }
                    .addOnFailureListener {
                        Log.d("post_liked", "Ошибка")
                    }
            }

            var likeCount = 0
            post.likedByUsers.forEach {
                likeCount++
            }

            binding.postTitle.text = post.title
            binding.postText.text = post.text
            binding.createdByUserName.text = "@${post.createdByUserName}"
            binding.postLikeCount.text = post.likes.toString()

            var isLiked = false

            post.likedByUsers.forEach {
                if (it == userId) {
                    isLiked = true
                    return@forEach
                }
            }

            if (post.createdByUserId == userId) {
                binding.deletePostBtn.visibility = View.VISIBLE
                binding.deletePostBtn.setOnClickListener {
                    binding.postCard.visibility = View.GONE
                    binding.postWrapper.setPadding(0, 0, 0, 0)
                    binding.postWrapper.visibility = View.GONE

                    firebaseRef.child(post.id.toString()).removeValue()
                        .addOnCompleteListener {
                            Log.d("post_liked", "Пользователь с id{${userId}} удалил пост с id{${post.id}}")
                        }
                        .addOnFailureListener {
                            Log.d("post_liked", "Ошибка удаление поста")
                        }
                }
            } else {
                binding.deletePostBtn.visibility = View.GONE
            }

            if (isLiked) {
                binding.postLikeIcon.setImageDrawable(getDrawable(itemView.context, R.drawable.icon_heart_like_filled))
                binding.postLikeBtn.setBackgroundColor(getColor(itemView.context, R.color.btn_like_active_color))
            } else {
                binding.postLikeIcon.setImageDrawable(getDrawable(itemView.context, R.drawable.icon_heart_like_outlined))
                binding.postLikeBtn.setBackgroundColor(getColor(itemView.context, R.color.btn_like_color))
            }
        }
    }
}