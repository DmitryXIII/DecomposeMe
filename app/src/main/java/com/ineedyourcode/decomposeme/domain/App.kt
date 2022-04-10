package com.ineedyourcode.decomposeme.domain

import android.app.Application
import androidx.room.Room
import com.ineedyourcode.decomposeme.domain.db.UserDao
import com.ineedyourcode.decomposeme.domain.db.UserDb

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: App? = null
        private var appDb: UserDb? = null
        private const val APP_DB_NAME = "Users.db"

        fun getUserDao(): UserDao {
            if (appDb == null) {
                synchronized(UserDb::class.java) {
                    if (appDb == null) {
                        if (instance == null) throw IllegalAccessException("App is null")
                        appDb = Room.databaseBuilder(
                            instance!!.applicationContext,
                            UserDb::class.java,
                            APP_DB_NAME
                        )
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }

            return appDb!!.userDao()
        }
    }
}