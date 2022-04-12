package com.ineedyourcode.decomposeme.ui.registration

import android.os.Handler
import android.os.Looper
import com.ineedyourcode.decomposeme.App
import com.ineedyourcode.decomposeme.R
import com.ineedyourcode.decomposeme.domain.REQUEST_CODE_LOGIN_REGISTERED_YET
import com.ineedyourcode.decomposeme.domain.REQUEST_CODE_OK
import com.ineedyourcode.decomposeme.domain.repository.IUserLoginApi
import com.ineedyourcode.decomposeme.data.MockUserLoginApi

class RegistrationActivityPresenter : RegistrationActivityContract.RegistrationPresenter {
    private val userLoginApi: IUserLoginApi = MockUserLoginApi(App.getUserDao())

    private val uiThread = Handler(Looper.getMainLooper())

    private lateinit var view: RegistrationActivityContract.RegistrationView

    override fun onAttach(mView: RegistrationActivityContract.RegistrationView) {
        view = mView
    }

    override fun onRegister(login: String, password: String) {
        if (login.isBlank()) {
            view.setRegistrationError((view as RegistrationActivity).getString(R.string.login_can_not_be_blank))
        } else {
            view.showProgress()
            Thread {
                uiThread.post {
                    when (userLoginApi.addNewUser(login, password)) {
                        REQUEST_CODE_OK -> {
                            view.hideProgress()
                            view.setRegistrationSuccess(login)
                        }
                        REQUEST_CODE_LOGIN_REGISTERED_YET -> {
                            view.hideProgress()
                            view.setRegistrationError(
                                (view as RegistrationActivity).getString(
                                    R.string.login_registered_yet,
                                    login
                                )
                            )
                        }
                    }
                }
            }.start()
        }
    }
}