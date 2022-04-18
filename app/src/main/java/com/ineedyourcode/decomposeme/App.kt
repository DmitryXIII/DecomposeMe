package com.ineedyourcode.decomposeme

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.ineedyourcode.decomposeme.data.api.MockUserDatabaseApi
import com.ineedyourcode.decomposeme.data.db.UserDatabase
import com.ineedyourcode.decomposeme.data.interactor.login.MockUserLoginInteractor
import com.ineedyourcode.decomposeme.data.interactor.registration.MockUserRegistrationInteractor
import com.ineedyourcode.decomposeme.data.interactor.remindpassword.MockRemindPasswordInteractor
import com.ineedyourcode.decomposeme.data.repository.MockUserDatabaseRepository
import com.ineedyourcode.decomposeme.domain.api.IUserDatabaseApi
import com.ineedyourcode.decomposeme.domain.interactor.login.IUserLoginInteractor
import com.ineedyourcode.decomposeme.domain.interactor.registration.IUserRegistrationInteractor
import com.ineedyourcode.decomposeme.domain.interactor.remindpassword.IRemindPasswordInteractor
import com.ineedyourcode.decomposeme.domain.repository.IUserDatabaseRepository

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        applicationInstance = this
        appContext = this.applicationContext
    }

    companion object {
        private lateinit var applicationInstance: App
        private lateinit var appContext: Context
        private val uiHandler = Handler(Looper.getMainLooper())

        private val userDatabase: UserDatabase by lazy {
            UserDatabase.getUserDatabase(appContext)
        }

        private val userDatabaseApi: IUserDatabaseApi by lazy {
            MockUserDatabaseApi(userDatabase.userDao())
        }

        val userRepository: IUserDatabaseRepository by lazy {
            MockUserDatabaseRepository(userDatabase.userDao(), uiHandler)
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
    }
}