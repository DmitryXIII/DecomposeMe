package com.ineedyourcode.decomposeme

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.room.Room
import com.ineedyourcode.decomposeme.data.api.MockUserDatabaseApi
import com.ineedyourcode.decomposeme.data.interactor.login.MockUserLoginInteractor
import com.ineedyourcode.decomposeme.data.interactor.remindpassword.MockRemindPasswordInteractor
import com.ineedyourcode.decomposeme.domain.api.IUserDatabaseApi
import com.ineedyourcode.decomposeme.data.db.UserDao
import com.ineedyourcode.decomposeme.data.db.UserDb
import com.ineedyourcode.decomposeme.data.interactor.registration.MockUserRegistrationInteractor
import com.ineedyourcode.decomposeme.data.repository.MockUserDatabaseRepository
import com.ineedyourcode.decomposeme.domain.interactor.login.IUserLoginInteractor
import com.ineedyourcode.decomposeme.domain.interactor.registration.IUserRegistrationInteractor
import com.ineedyourcode.decomposeme.domain.interactor.remindpassword.IRemindPasswordInteractor
import com.ineedyourcode.decomposeme.domain.repository.IUserDatabaseRepository

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private val uiHandler = Handler(Looper.getMainLooper())

        private val userDatabaseApi: IUserDatabaseApi by lazy {
            MockUserDatabaseApi(getUserDao())
        }

        val userRepository: IUserDatabaseRepository by lazy {
            MockUserDatabaseRepository(getUserDao(), uiHandler)
        }

        val userLoginInteractor: IUserLoginInteractor by lazy {
            MockUserLoginInteractor(userDatabaseApi, uiHandler)
        }

        val userRemindPasswordInteractor: IRemindPasswordInteractor by lazy {
            MockRemindPasswordInteractor(userDatabaseApi, uiHandler)
        }

        val userRegistrationInteractor: IUserRegistrationInteractor by lazy {
            MockUserRegistrationInteractor(userRepository)
        }

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