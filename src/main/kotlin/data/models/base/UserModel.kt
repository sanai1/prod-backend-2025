package ru.kotleteri.data.models.base

data class UserModel(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)
