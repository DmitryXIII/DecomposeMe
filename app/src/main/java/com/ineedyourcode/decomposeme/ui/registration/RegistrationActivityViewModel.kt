package com.ineedyourcode.decomposeme.ui.registration

import com.ineedyourcode.decomposeme.domain.interactor.registration.IUserRegistrationInteractor
import com.ineedyourcode.decomposeme.ui.utils.MessageMapper
import com.ineedyourcode.decomposeme.ui.utils.Publisher

class RegistrationActivityViewModel(private val userRegistrationInteractor: IUserRegistrationInteractor) :
    RegistrationViewModelContract {

    private val messageMapper = MessageMapper()

    override val isInProgress: Publisher<Boolean> = Publisher()

    override val registrationSuccess: Publisher<String> = Publisher()

    override val messenger: Publisher<Pair<Int, Any?>> = Publisher(true)

    override fun onRegister(login: String, password: String) {
        if (login.isBlank()) {
            messenger.post(Pair(
                messageMapper.getStringResource(MessageMapper.ResponseCodes.RESPONSE_LOGIN_CANNOT_BE_BLANK.code),
                null)
            )
        } else {
            isInProgress.post(true)
            userRegistrationInteractor.userRegistration(login, password) { response ->
                when (response) {
                    MessageMapper.ResponseCodes.RESPONSE_SUCCESS.code -> {
                        isInProgress.post(false)
                        registrationSuccess.post(login)
                    }
                    MessageMapper.ResponseCodes.RESPONSE_LOGIN_REGISTERED_YET.code -> {
                        isInProgress.post(false)
                        messenger.post(Pair(
                            messageMapper.getStringResource(response),
                            login)
                        )
                    }
                }
            }
        }
    }
}
