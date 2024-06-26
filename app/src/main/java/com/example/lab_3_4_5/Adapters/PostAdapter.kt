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
        val sortedList = postList.sortedByDescending{ it.id }
        Log.d("1111", sortedList.toString())
        holder.bind(sortedList[position])
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

                firebaseRef.child(post.id.toString()).child("likedByUsers").setValue(post.likedByUsers)
                    .addOnCompleteListener {
                        Log.d("post_liked", "Пользователь с id{${userId}} поставил лайк на пост с id{${post.id}}")
                    }
                    .addOnFailureListener {
                        Log.d("post_liked", "Ошибка")
                    }

                firebaseRef.child(post.id.toString()).child("likes").setValue(post.likedByUsers.count())
                    .addOnCompleteListener {
                        Log.d("post_liked", "Количество лайков на посте с id{${post.id}} составляет ${post.likedByUsers.count()}")
                    }
                    .addOnFailureListener {
                        Log.d("post_liked", "Ошибка")
                    }
            }

            binding.postTitle.text = post.title
            binding.postText.text = post.text

            for (user in User.userList) {
                if (post.createdByUserId == user.id) {
                    binding.createdByUserName.text = "@${user.login}".lowercase()
                    break
                }
            }

            binding.postLikeCount.text = post.likedByUsers.count().toString()
            binding.createdDate.text = post.createdAt

            if (post.cityWhereBy != null) {
                binding.cityWhereBy.visibility = View.VISIBLE
                binding.cityWhereBy.text = "Привет из ${post.cityWhereBy}!"
            } else {
                binding.cityWhereBy.visibility = View.GONE
            }

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