package com.example.lab_3_4_5.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_3_4_5.Models.Post
import com.example.lab_3_4_5.R
import com.example.lab_3_4_5.databinding.PostCardBinding

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostHolder>() {
    val postList = mutableListOf<Post>(
        Post(
            0,
            "123123",
            "dwadawdawdaw",
            23,
            true
        ),
        Post(
            1,
            "22222222",
            "tgthtrhtrhrth",
            1,
            false
        ))

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

    class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = PostCardBinding.bind(itemView)

        fun bind(post: Post) {
            binding.postTitle.text = post.title
            binding.postText.text = post.text
            binding.postLikeCount.text = post.likes.toString()

//            binding.postLikeIcon.setImageDrawable(AppCompatResources.getDrawable(itemView.context, R.drawable.icon_heart_like_outlined))
//            binding.postLikeBtn.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.btn_like_color))

//            if (post.isLiked) {
//                binding.postLikeIcon.setImageDrawable(AppCompatResources.getDrawable(itemView.context, R.drawable.icon_heart_like_filled))
//                binding.postLikeBtn.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.btn_like_active_color))
//            } else {
//                binding.postLikeIcon.setImageDrawable(AppCompatResources.getDrawable(itemView.context, R.drawable.icon_heart_like_outlined))
//                binding.postLikeBtn.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.btn_like_color))
//            }
        }
    }
}