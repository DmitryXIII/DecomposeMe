package com.ineedyourcode.decomposeme.domain.repository

import com.ineedyourcode.decomposeme.data.db.UserEntity

interface IUserDatabaseRepository {
    fun addUser(login: String, password: String, callback: (Int) -> Unit)
    fun getUser(login: String, callback: (UserEntity?) -> Unit)
    fun getAllUsers(callback: (List<UserEntity>) -> Unit)
    fun updateUser(userId: Int, newLogin: String, newPassword: String, isAuthorized: Boolean, callback: (Int) -> Unit)
    fun deleteUser(login: String, callback: (Int) -> Unit)
}