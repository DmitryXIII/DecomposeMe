package com.ineedyourcode.decomposeme.data.resourses

enum class MockUserDatabaseResponseCodes(val code: Int) {
    RESPONSE_SUCCESS(200),
    RESPONSE_INVALID_PASSWORD(403),
    RESPONSE_LOGIN_NOT_REGISTERED(404),
    RESPONSE_LOGIN_REGISTERED_YET(444),
    RESPONSE_USER_UPDATE_FAILED(454),
    RESPONSE_USER_DELETE_FAILED(464)
}