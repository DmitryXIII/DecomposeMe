package com.ineedyourcode.decomposeme.ui.registration

import com.ineedyourcode.decomposeme.ui.utils.Publisher

interface RegistrationViewModelContract {
    val isInProgress: Publisher<Boolean>
    val registrationSuccess: Publisher<String>
    val messenger: Publisher<Pair<Int, Any?>>

    fun onRegister(login: String, password: String)
}