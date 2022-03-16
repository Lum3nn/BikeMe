package com.lumen.bikeme.commons.service

interface UserService {
    suspend fun getUserId(): String
    suspend fun getAccessToken(): String

    class CreateAnonymousUserException : Exception()
    class TokenException : Exception()

}