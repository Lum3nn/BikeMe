package com.lumen.bikeme.commons.injection

import com.lumen.bikeme.tripForm.TripsFormViewModel
import com.lumen.bikeme.tripList.TripsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TripsListViewModel(get()) }
    viewModel { TripsFormViewModel(get()) }
}