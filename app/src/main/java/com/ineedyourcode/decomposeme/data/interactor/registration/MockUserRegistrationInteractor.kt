package com.ineedyourcode.decomposeme.data.interactor.registration

import com.ineedyourcode.decomposeme.domain.interactor.registration.IUserRegistrationInteractor
import com.ineedyourcode.decomposeme.domain.repository.IUserDatabaseRepository

class MockUserRegistrationInteractor(
    private val userRepository: IUserDatabaseRepository,
) : IUserRegistrationInteractor {
    override fun userRegistration(login: String, password: String, callback: (Int) -> Unit) {
        userRepository.addUser(login, password) { responseCode ->
            callback(responseCode)
        }
    }
}