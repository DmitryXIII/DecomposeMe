package com.ineedyourcode.decomposeme.domain.repository

import com.ineedyourcode.decomposeme.domain.UserDto
import com.ineedyourcode.decomposeme.domain.db.UserEntity

interface IUserRepository {
    fun getUser(login: String) : UserEntity?
    fun checkUser(login: String, password: String) : Int
    fun remindUserPassword(login: String) : String
    fun addNewUser(login: String, password: String) : Int
    fun getAllUsers() : List<UserDto>
    fun deleteUser(login: String)
}