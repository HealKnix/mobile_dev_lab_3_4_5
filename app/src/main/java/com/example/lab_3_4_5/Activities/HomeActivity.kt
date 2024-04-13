package com.example.lab_3_4_5.Activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab_3_4_5.Adapters.PostAdapter
import com.example.lab_3_4_5.Models.Post
import com.example.lab_3_4_5.R
import com.example.lab_3_4_5.databinding.FragmentPostListBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: FragmentPostListBinding
    private val adapter = PostAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPostListBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(R.layout.activity_posts)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.posts_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 75, 0, 0)
            insets
        }
        init()
    }

    private fun init() {
        adapter.addPost(Post(
            0,
            "123123",
            "dwadawdawdaw",
            23,
            true
        ))
        adapter.addPost(Post(
            1,
            "22222222",
            "tgthtrhtrhrth",
            1,
            false
        ))
        binding.apply {
            PostsRecyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)
            PostsRecyclerView.adapter = adapter
        }
    }

    fun likePost(view: View) {
//        val likeBtn: CardView = view as CardView
//        val btnLayout: ViewGroup = view as ViewGroup
//
//        val constraintLayout = btnLayout.getChildAt(0) as ViewGroup
//
//        val button: Button = constraintLayout.getChildAt(0) as Button
//        val horizontalLayout: ViewGroup = constraintLayout.getChildAt(1) as ViewGroup
//
//        val heartIcon: ImageView = horizontalLayout.getChildAt(0) as ImageView
//        val likeCount: TextView = horizontalLayout.getChildAt(1) as TextView
//        likeCount.setText(((likeCount.text.toString()).toInt() + 1).toString())
//
//        heartIcon.setImageDrawable(getDrawable(R.drawable.icon_heart_like_filled))
//        button.setBackgroundColor(getColor(R.color.btn_like_active_color))
    }
}