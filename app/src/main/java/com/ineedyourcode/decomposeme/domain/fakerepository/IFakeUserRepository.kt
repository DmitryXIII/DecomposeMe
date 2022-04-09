package com.ineedyourcode.decomposeme.domain.fakerepository

interface IFakeUserRepository {
    fun checkUser(login: String, password: String) : String
    fun remindUserPassword(login: String) : String
}