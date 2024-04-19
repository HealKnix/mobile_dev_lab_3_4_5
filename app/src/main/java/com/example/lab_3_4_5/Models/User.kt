package com.example.lab_3_4_5.Models

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
    }
}
