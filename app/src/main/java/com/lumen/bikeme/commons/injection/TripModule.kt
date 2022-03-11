package com.lumen.bikeme.commons.injection

import com.lumen.bikeme.commons.repository.TripRepository
import com.lumen.bikeme.commons.repository.TripRoomRepository
import org.koin.dsl.module

val tripModule = module {
    single<TripRepository> {
        TripRoomRepository(get())
    }
}

