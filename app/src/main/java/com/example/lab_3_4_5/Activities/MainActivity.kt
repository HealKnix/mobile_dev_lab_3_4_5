package com.example.lab_3_4_5.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 75, 0, 0)
            insets
        }

        sharedPreferences = getSharedPreferences("my_app_pref", Context.MODE_PRIVATE)

        if (checkUserData()) {
            val user = getUserData()

            fetchUsersData(user?.first ?: "", user?.second ?: "")
        }
    }

    private fun fetchUsersData(email: String, password: String) {
        firebaseRef.get().addOnCompleteListener {
            User.userList.clear()
            User.setCurrentUser(null)

            val userListFromDB = it.result

            for (userSnap in userListFromDB.children) {
                val user = userSnap.getValue(User::class.java)!!
                User.userList.add(user)

                if ((
                        user.login.lowercase() == email.lowercase() ||
                        user.email.lowercase() == email.lowercase()
                    ) &&
                    user.password == password)
                {
                    User.setCurrentUser(user)
                    Toast.makeText(applicationContext, "Вы успешно вошли", Toast.LENGTH_SHORT).show()

                    saveUserData(user.email, password)

                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)

                    finish()

                    return@addOnCompleteListener
                }
            }

            clearUserData()
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

        fetchUsersData(email.text.toString(), User.md5(password.text.toString()))
    }

    // При успешном входе пользователя сохраняем его данные
    fun saveUserData(email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    // Проверяем наличие сохраненных данных пользователя
    fun checkUserData(): Boolean {
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)

        // Проверяем, что данные не пусты
        return !(email.isNullOrEmpty() || password.isNullOrEmpty())
    }

    // Получаем сохраненные данные пользователя для автоматического входа
    fun getUserData(): Pair<String, String>? {
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)

        // Возвращаем пару значений (email, password)
        return if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            Pair(email, password)
        } else {
            null
        }
    }

    // Функция для удаления данных пользователя при выходе
    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.clear() // Удаляем все данные из SharedPreferences
        editor.apply()
    }

    // Переключение на активити с Постами
    fun toSignUpActivity(view: View?) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}