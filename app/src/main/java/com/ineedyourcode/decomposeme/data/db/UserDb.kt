package com.ineedyourcode.decomposeme.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val USER_DB_NAME = "Users.db"

@Database(entities = [UserEntity::class], version = 4, exportSchema = false)
abstract class UserDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private var INSTANCE: UserDb? = null

        fun getDatabase(context: Context): UserDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    UserDb::class.java,
                    USER_DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}