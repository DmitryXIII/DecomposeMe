package com.ineedyourcode.decomposeme.domain.repository

import com.ineedyourcode.decomposeme.domain.UserDto

interface IUserRepository {
    fun checkUser(login: String, password: String) : Int
    fun remindUserPassword(login: String) : String
    fun registerNewUser(login: String, password: String) : Int
    fun getAllUsers() : List<UserDto>
    fun deleteUser(login: String)
}