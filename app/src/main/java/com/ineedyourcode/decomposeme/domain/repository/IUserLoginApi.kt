package com.ineedyourcode.decomposeme.domain.repository

import com.ineedyourcode.decomposeme.domain.db.UserEntity

interface IUserLoginApi {
    fun getUser(login: String) : UserEntity?
    fun login(login: String, password: String) : Int
    fun logout(login: String) : Int
    fun remindUserPassword(login: String) : String
    fun addNewUser(login: String, password: String) : Int
    fun getAllUsers() : List<UserEntity>
    fun deleteUser(login: String) : Int
}