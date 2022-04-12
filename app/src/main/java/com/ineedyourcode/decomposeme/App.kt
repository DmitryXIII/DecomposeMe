package com.ineedyourcode.decomposeme

import android.app.Application
import androidx.room.Room
import com.ineedyourcode.decomposeme.data.MockUserLoginApi
import com.ineedyourcode.decomposeme.domain.db.UserDao
import com.ineedyourcode.decomposeme.domain.db.UserDb
import com.ineedyourcode.decomposeme.domain.repository.IUserLoginApi

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        val userLoginApi: IUserLoginApi by lazy { MockUserLoginApi(getUserDao()) }

        private const val APP_DB_NAME = "Users.db"
        private var instance: App? = null
        private var appDb: UserDb? = null

        private fun getUserDao(): UserDao {
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