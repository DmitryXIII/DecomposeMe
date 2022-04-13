package com.ineedyourcode.decomposeme.domain.interactor.registration

interface IUserRegistrationInteractor {
    fun userRegistration(login: String, password: String, callback:(Int) -> Unit)
}