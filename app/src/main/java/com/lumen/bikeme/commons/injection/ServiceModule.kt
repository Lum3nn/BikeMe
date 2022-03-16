package com.lumen.bikeme.commons.injection

import com.lumen.bikeme.commons.service.FirebaseUserService
import com.lumen.bikeme.commons.service.UserService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class ServiceModule {

    @Binds
    abstract fun bindUserService(
        firebaseUserService: FirebaseUserService
    ): UserService
}