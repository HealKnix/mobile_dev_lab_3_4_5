package com.example.lab_3_4_5.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.lab_3_4_5.Activities.MainActivity
import com.example.lab_3_4_5.Models.User
import com.example.lab_3_4_5.R
import com.example.lab_3_4_5.databinding.FragmentProfileBinding
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        binding = FragmentProfileBinding.inflate(layoutInflater)
        sharedPreferences = requireContext().getSharedPreferences("my_app_pref", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val login = view.findViewById<EditText>(R.id.textViewProfileLogin)
        val email = view.findViewById<EditText>(R.id.textViewProfileEmail)

        login.setText(User.getCurrentUser()?.login)
        email.setText(User.getCurrentUser()?.email)

        val updateUserBtn = view.findViewById<Button>(R.id.update_profile_btn)
        val logoutBtn = view.findViewById<Button>(R.id.logout_btn)

        updateUserBtn.setOnClickListener {
            val firebaseDB = FirebaseDatabase.getInstance().getReference("Users")

            val currentUser = User.getCurrentUser()

            val password = view.findViewById<TextView>(R.id.textViewProfilePassword)

            if (login.text.isEmpty())
                login.error = "Пустое поле"
            if (email.text.isEmpty())
                email.error = "Пустое поле"

            if (login.text.isEmpty() || email.text.isEmpty())
                return@setOnClickListener

            if (login.text.isEmpty() || email.text.isEmpty())
                return@setOnClickListener

            var newLogin: String = login.text.toString()
            var newEmail: String = email.text.toString()
            var newPassword: String = User.md5(password.text.toString())

            if (
                login.text.toString() == currentUser?.login &&
                email.text.toString() == currentUser.email &&
                newPassword.isEmpty()
            ) {
                Toast.makeText(context, "Нечего обновлять", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (login.text.toString() == currentUser?.login) {
                newLogin = currentUser.login
            }
            if (email.text.toString() == currentUser?.password) {
                newEmail = currentUser.password
            }
            if (newPassword.isEmpty()) {
                newPassword = currentUser?.password ?: newPassword
            }

            val firebaseDBUser = FirebaseDatabase.getInstance().getReference("Users")
            firebaseDB.get().addOnCompleteListener {
                val userListFromDB = it.result

                for (user in userListFromDB.children) {
                    val userInDb = user.getValue(User::class.java)

                    if (
                        userInDb?.id != User.getCurrentUser()?.id &&
                        (userInDb?.login?.lowercase() == newLogin.lowercase() ||
                        userInDb?.email?.lowercase() == newEmail.lowercase())
                    ) {
                        Toast.makeText(context, "Такой пользователь уже существует", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                }

                val updatedUser = currentUser?.let { user ->
                    User(
                        user.id,
                        newLogin,
                        newEmail,
                        newPassword
                    )
                }

                firebaseDB.child(currentUser?.id.toString()).setValue(updatedUser)
                    .addOnCompleteListener {
                        User.setCurrentUser(updatedUser)
                        saveUserData(newEmail, newPassword)
                        password.text = ""
                        Toast.makeText(context, "Пользователь обновлён", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Ошибка обновлении данныз о пользователе", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        logoutBtn.setOnClickListener {
            User.setCurrentUser(null)

            clearUserData()

            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)

            Toast.makeText(activity, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()

            activity?.finish()
        }

        return view
    }

    // При успешном входе пользователя сохраняем его данные
    fun saveUserData(email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    // Функция для удаления данных пользователя при выходе
    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.clear() // Удаляем все данные из SharedPreferences
        editor.apply()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}