package com.example.lab_3_4_5.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab_3_4_5.Models.User
import com.example.lab_3_4_5.R
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private val firebaseRef = FirebaseDatabase.getInstance().getReference("Users")

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

    private fun fetchUsersData(email: String, password: String) {
        firebaseRef.get().addOnCompleteListener {
            User.userList.clear()
            User.setCurrentUser(null)

            val userListFromDB = it.result

            for (userSnap in userListFromDB.children) {
                val user = userSnap.getValue(User::class.java)!!

                if ((user.login.lowercase() == email.lowercase() || user.email.lowercase() == email.lowercase()) && user.password == User.md5(password)) {
                    User.setCurrentUser(user)
                    Toast.makeText(applicationContext, "Вы успешно вошли", Toast.LENGTH_SHORT).show()

                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)

                    finish()

                    return@addOnCompleteListener
                }
            }

            Toast.makeText(applicationContext, "Такой пользователь не найден", Toast.LENGTH_SHORT).show()
        }
    }

    fun login(view: View?) {
        val email = findViewById<TextView>(R.id.editTextTextEmailAddressLogin)
        val password = findViewById<TextView>(R.id.editTextTextPasswordLogin)

        if (email.text.isEmpty())
            email.error = "Пустое поле!"
        if (password.text.isEmpty())
            password.error = "Пустое поле!"

        if (email.text.isEmpty() || password.text.isEmpty())
            return

        fetchUsersData(email.text.toString(), password.text.toString())
    }

    // Переключение на активити с Постами
    fun toSignUpActivity(view: View?) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}