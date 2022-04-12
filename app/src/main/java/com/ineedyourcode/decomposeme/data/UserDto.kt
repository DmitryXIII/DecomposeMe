package com.ineedyourcode.decomposeme.data

data class UserDto(
    val userId: String,
    val userLogin: String,
    val userPassword: String,
    val isAuthorized: Boolean
)