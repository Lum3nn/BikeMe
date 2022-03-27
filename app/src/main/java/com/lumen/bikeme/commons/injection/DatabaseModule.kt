/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lumen.bikeme.commons.injection

import android.content.Context
import androidx.room.Room
import com.lumen.bikeme.commons.network.AccessTokenInterceptor
import com.lumen.bikeme.commons.network.FirebaseService
import com.lumen.bikeme.commons.repository.dao.MapResponseDao
import com.lumen.bikeme.commons.repository.dao.TripResponseDao
import com.lumen.bikeme.commons.repository.database.AppDatabase
import com.lumen.bikeme.commons.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    private const val BASE_URL = "https://bikeme-6e9e7-default-rtdb.firebaseio.com/"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "trips.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideRateListDao(database: AppDatabase): TripResponseDao {
        return database.tripListDao()
    }

    @Singleton
    @Provides
    fun provideMapDao(database: AppDatabase): MapResponseDao {
        return database.mapDao()
    }

    @Singleton
    @Provides
    fun provideFirebaseClient(userService: UserService): FirebaseService {

        val logging = HttpLoggingInterceptor { message -> println("KITKA $message") }
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val tokenInterceptor = AccessTokenInterceptor(userService)

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FirebaseService::class.java)
    }
}
