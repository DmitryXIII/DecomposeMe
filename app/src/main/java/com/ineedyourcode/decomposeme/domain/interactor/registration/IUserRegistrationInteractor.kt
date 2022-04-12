package com.ineedyourcode.decomposeme.domain.interactor.registration

interface IUserRegistrationInteractor {
    fun addNewUser(login: String, password: String, callback: (Int) -> Unit)
}