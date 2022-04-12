package com.ineedyourcode.decomposeme.domain.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM UserEntity WHERE userLogin =:login")
    fun getUser(login: String) : UserEntity?

    @Query("SELECT * FROM UserEntity")
    fun getAllUsers(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertNewUser(entity: UserEntity)

    @Query("UPDATE UserEntity SET isAuthorized = :isAuthorized WHERE userLogin =:login")
    fun userLogin(login: String, isAuthorized: Boolean = true)

    @Query("UPDATE UserEntity SET isAuthorized = :isAuthorized WHERE userLogin =:login")
    fun userLogout(login: String, isAuthorized: Boolean = false)

    @Query("DELETE FROM UserEntity WHERE userLogin == :login")
    fun deleteUser(login: String)
}