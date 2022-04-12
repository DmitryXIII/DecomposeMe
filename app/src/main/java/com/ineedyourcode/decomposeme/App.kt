package com.ineedyourcode.decomposeme

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.room.Room
import com.ineedyourcode.decomposeme.data.MockUserDatabaseApi
import com.ineedyourcode.decomposeme.data.MockUserLoginInteractor
import com.ineedyourcode.decomposeme.data.MockUserRegistrationInteractor
import com.ineedyourcode.decomposeme.domain.IUserDatabaseApi
import com.ineedyourcode.decomposeme.domain.db.UserDao
import com.ineedyourcode.decomposeme.domain.db.UserDb
import com.ineedyourcode.decomposeme.domain.interactor.login.IUserLoginInteractor
import com.ineedyourcode.decomposeme.domain.interactor.registration.IUserRegistrationInteractor

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        val userDatabaseApi: IUserDatabaseApi by lazy { MockUserDatabaseApi(getUserDao()) }
        val userLoginInteractor: IUserLoginInteractor by lazy { MockUserLoginInteractor(userDatabaseApi, Handler(Looper.getMainLooper())) }
        val userRegistrationInteractor: IUserRegistrationInteractor by lazy { MockUserRegistrationInteractor(userDatabaseApi, Handler(Looper.getMainLooper())) }

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