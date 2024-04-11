package com.example.lab_3_4_5

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Переключение на активити с Постами
    fun toSignUpActivity(view: View?) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    // Переключение на активити с Регистрацией
    fun toPostsActivity(view: View?) {
        val intent = Intent(this, PostsActivity::class.java)
        startActivity(intent)
    }
}