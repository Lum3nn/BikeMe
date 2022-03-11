package com.lumen.bikeme

import android.app.Application
import com.lumen.bikeme.commons.injection.databaseModule
import com.lumen.bikeme.commons.injection.tripModule
import com.lumen.bikeme.commons.injection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule, tripModule, viewModelModule
                )
            )
        }
    }
}