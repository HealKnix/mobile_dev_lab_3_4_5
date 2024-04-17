package com.example.lab_3_4_5.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.lab_3_4_5.Fragments.PostCreateFragment
import com.example.lab_3_4_5.Fragments.PostListFragment
import com.example.lab_3_4_5.Fragments.ProfileFragment
import com.example.lab_3_4_5.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_posts)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.posts_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 75, 0, 0)
            insets
        }

        val bbv = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bbv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_post_list -> replaceFragment(PostListFragment())
                R.id.nav_post_create -> replaceFragment(PostCreateFragment())
                R.id.nav_profile -> replaceFragment(ProfileFragment())
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }
}