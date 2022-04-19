package com.ineedyourcode.decomposeme.domain.api

interface IUserDatabaseApi {
    fun login(login: String, password: String) : Int
    fun logout(login: String?) : Int
    fun remindUserPassword(login: String) : Any
}