package com.lumen.bikeme.commons.service

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseUserService @Inject constructor() : UserService {

    private suspend fun getUser(): FirebaseUser {
        val firebaseUser = Firebase.auth.currentUser
        return if (firebaseUser == null) {
            val authResult = Firebase.auth.signInAnonymously().await()
            val user = authResult.user
            user ?: throw UserService.CreateAnonymousUserException()
        } else {
            firebaseUser
        }
    }

    override suspend fun getUserId(): String {
        return getUser().uid
    }

    override suspend fun getAccessToken(): String {
        val user = getUser()
        val tokenResult = user.getIdToken(false).await()
        return tokenResult.token ?: throw UserService.TokenException()
    }
}