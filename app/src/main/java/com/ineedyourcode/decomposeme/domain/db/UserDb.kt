package com.ineedyourcode.decomposeme.domain.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserDb : RoomDatabase() {
    abstract fun userDao() : UserDao
}