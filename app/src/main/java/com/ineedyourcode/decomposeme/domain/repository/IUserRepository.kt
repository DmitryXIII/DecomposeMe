package com.ineedyourcode.decomposeme.domain.repository

import com.ineedyourcode.decomposeme.domain.UserDto
import com.ineedyourcode.decomposeme.domain.db.UserEntity

interface IUserRepository {
    fun checkUser(login: String, password: String) : Int
    fun remindUserPassword(login: String) : String
    fun registerNewUser(login: String, password: String) : Int
    fun getAllUsers() : MutableMap<String, UserDto>
    fun deleteUser(login: String)
}