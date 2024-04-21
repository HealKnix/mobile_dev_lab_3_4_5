package com.example.lab_3_4_5.Models

import java.math.BigInteger
import java.security.MessageDigest

data class User(
    var id: Int = -1,
    var login: String = "",
    var email: String = "",
    var password: String = ""
) {
    companion object {
        val userList = mutableListOf<User>()
        private var currentUser: User? = null

        fun setCurrentUser(user: User?) {
            currentUser = user
        }

        fun getCurrentUser(): User? {
            return currentUser
        }

        fun md5(input:String): String {
            if (input.isEmpty()) return ""
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }
    }
}
