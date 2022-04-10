package com.ineedyourcode.decomposeme.domain.db

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM UserEntity")
    fun getAllUsers(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertNewUser(entity: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE userLogin == :login")
    fun getUser(login: String): UserEntity?

    @Query("DELETE FROM UserEntity WHERE userLogin == :login")
    fun deleteUser(login: String)
}