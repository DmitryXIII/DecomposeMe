package com.ineedyourcode.decomposeme.domain.api

import com.ineedyourcode.decomposeme.data.db.UserEntity

interface IUserDatabaseApi {
    fun login(login: String, password: String) : Int
    fun logout(login: String) : Int
    fun remindUserPassword(login: String) : String
}