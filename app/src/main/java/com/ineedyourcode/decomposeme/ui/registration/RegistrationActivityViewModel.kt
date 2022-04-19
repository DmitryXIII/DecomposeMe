package com.ineedyourcode.decomposeme.ui.registration

import com.ineedyourcode.decomposeme.domain.interactor.registration.IUserRegistrationInteractor
import com.ineedyourcode.decomposeme.ui.utils.Publisher

class RegistrationActivityViewModel(private val userRegistrationInteractor: IUserRegistrationInteractor) :
    RegistrationViewModelContract {

    override val isInProgress: Publisher<Boolean> = Publisher()

    override val registrationSuccess: Publisher<String> = Publisher()

    override val messenger: Publisher<String> = Publisher(true)

    override fun onRegister(login: String, password: String) {
        if (login.isBlank()) {
            messenger.post(MessageSource.LOGIN_CANNOT_BE_BLANK.message)
        } else {
            isInProgress.post(true)
            userRegistrationInteractor.userRegistration(login, password) { response ->
                when (response) {
                    ResponseCodes.RESPONSE_SUCCESS.code -> {
                        isInProgress.post(false)
                        registrationSuccess.post(login)
                    }
                    ResponseCodes.RESPONSE_LOGIN_REGISTERED_YET.code -> {
                        isInProgress.post(false)
                        messenger.post(MessageSource.LOGIN_REGISTERED_YET.message)
                    }
                }
            }
        }
    }
}

private enum class ResponseCodes(val code: Int) {
    RESPONSE_SUCCESS(200),
    RESPONSE_LOGIN_REGISTERED_YET(444)
}

private enum class MessageSource(val message: String) {
    LOGIN_CANNOT_BE_BLANK("Логин не может быть пустым"),
    LOGIN_REGISTERED_YET("Логин уже был зарегистрирован ранее")
}