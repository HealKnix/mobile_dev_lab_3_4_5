package com.example.lab_3_4_5.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab_3_4_5.Models.User
import com.example.lab_3_4_5.R
import com.example.lab_3_4_5.databinding.ActivitySignUpBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUpActivity : AppCompatActivity() {
    private val firebaseRef = FirebaseDatabase.getInstance().getReference("Users")
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sign_up_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 75, 0, 0)
            insets
        }
        binding = ActivitySignUpBinding.inflate(layoutInflater)
    }

    private fun fetchUsersData(login: String, email: String, password: String) {
                firebaseRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                User.userList.clear()

                val newUser = User(
                    0,
                    login,
                    email,
                    password
                )

                if (snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val user = userSnap.getValue(User::class.java)!!
                        if (user.login == newUser.login || user.email == newUser.email) {
                            Toast.makeText(applicationContext, "Такой пользователь уже существует", Toast.LENGTH_SHORT).show()
                            return
                        }
                        User.userList.add(user)
                    }
                }

                if (User.userList.isNotEmpty())
                    newUser.id = User.userList[User.userList.count() - 1].id + 1

                User.userList.add(newUser)
                User.setCurrentUser(newUser)

                firebaseRef.child(newUser.id.toString()).setValue(newUser)
                    .addOnCompleteListener {
                        Toast.makeText(applicationContext, "Пользователь зарегистрирован", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, "Ошибка регистрации пользователя", Toast.LENGTH_SHORT).show()
                    }

                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Ошибка загрузки данных о пользователях", Toast.LENGTH_SHORT).show()
                Log.d("sign_up", "Failed to read value.", error.toException())
            }
        })
    }

    fun signUp(view: View?) {
        val login = findViewById<TextView>(R.id.editTextTextLogin)
        val email = findViewById<TextView>(R.id.editTextTextEmailAddress)
        val password = findViewById<TextView>(R.id.editTextTextPassword)

        if (login.text.isEmpty())
            login.error = "Пустое поле!"
        if (email.text.isEmpty())
            email.error = "Пустое поле!"
        if (password.text.isEmpty())
            password.error = "Пустое поле!"

        if (login.text.isEmpty() || email.text.isEmpty() || password.text.isEmpty())
            return

        fetchUsersData(
            login.text.toString(),
            email.text.toString(),
            password.text.toString()
        )
    }

    fun toLoginActivity(view: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}