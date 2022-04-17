package com.ineedyourcode.decomposeme.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 4, exportSchema = false)
abstract class UserDb : RoomDatabase() {
    abstract fun userDao() : UserDao
}