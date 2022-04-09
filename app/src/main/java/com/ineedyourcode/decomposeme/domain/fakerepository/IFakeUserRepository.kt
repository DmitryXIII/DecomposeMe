package com.ineedyourcode.decomposeme.domain.fakerepository

interface IFakeUserRepository {
    fun checkUser(login: String, password: String) : Int
    fun remindUserPassword(login: String) : String
    fun registerNewUser(login: String, password: String) : Int
}