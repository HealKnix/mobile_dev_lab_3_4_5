package com.example.lab_3_4_5.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab_3_4_5.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 75, 0, 0)
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
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}