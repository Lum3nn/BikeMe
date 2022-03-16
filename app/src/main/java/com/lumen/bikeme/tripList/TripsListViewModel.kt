package com.lumen.bikeme.tripList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lumen.bikeme.commons.FailReason
import com.lumen.bikeme.commons.model.TripItem
import com.lumen.bikeme.commons.model.TripItemDate
import com.lumen.bikeme.commons.model.TripItemList
import com.lumen.bikeme.commons.repository.TripDataRepository
import com.lumen.bikeme.commons.service.FirebaseUserService
import com.lumen.bikeme.commons.toFormattedShortString
import com.lumen.bikeme.commons.toShortDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripsListViewModel @Inject constructor(private val tripDataRepository: TripDataRepository) :
    ViewModel() {

    private val _tripListUiState = MutableStateFlow<TripListUiState>(
        TripListUiState.Loading
    )
    val tripListUiState: StateFlow<TripListUiState> = _tripListUiState

    init {
        loadData()
        viewModelScope.launch {
            val id = FirebaseUserService().getUserId()
            //CBQLoMjFtAcniOPClJrBi6Ueg253
            println("KITKA INIT ANONYMOUS USER ID $id")
            val token = FirebaseUserService().getAccessToken()
            //CBQLoMjFtAcniOPClJrBi6Ueg253
            println("KITKA INIT ANONYMOUS USER TOKEN $token")
        }
    }

    private fun loadData() {
        _tripListUiState.value = TripListUiState.Loading

        viewModelScope.launch {
            try {
                val listTrip = tripDataRepository.listTrips()
                val formattedListTrip = createListTrip(listTrip)
                _tripListUiState.value = TripListUiState.Success(formattedListTrip)
            } catch (e: Exception) {
                _tripListUiState.value = TripListUiState.Fail(FailReason.FIREBASE)
            }
        }
    }

    private fun createListTrip(listTrip: List<TripItem>): List<TripItemList> {
        val finalTripItemList = mutableListOf<TripItemList>()

        val tripDates = listTrip.map {
            it.date
        }

        val formattedShortDates = tripDates.map { dateLong ->
            dateLong.toFormattedShortString().toShortDate()
        }.distinct()

        formattedShortDates.forEach { dateTrip ->
            finalTripItemList.add(TripItemDate(dateTrip))

            val tripsByDate = listTrip.filter {
                dateTrip == it.date.toFormattedShortString().toShortDate()
            }
            finalTripItemList.addAll(tripsByDate)
        }
        return finalTripItemList
    }

    fun deleteTrip(tripId: String) {
        _tripListUiState.value = TripListUiState.Loading

        viewModelScope.launch {
            try {
                tripDataRepository.deleteSingleTrip(tripId)
                val listTrip = tripDataRepository.listTrips()
                val formattedListTrip = createListTrip(listTrip)
                _tripListUiState.value = TripListUiState.Success(formattedListTrip)
            } catch (e: Exception) {
                _tripListUiState.value = TripListUiState.Fail(FailReason.FIREBASE)
            }
        }
    }

    fun addUndoTrip(tripName: String, tripDistance: String, tripDate: String) {

        _tripListUiState.value = TripListUiState.Loading

        viewModelScope.launch {
            try {
                tripDataRepository.insertTrip(tripName, tripDistance, tripDate)
                val listTrip = tripDataRepository.listTrips()
                val formattedListTrip = createListTrip(listTrip)
                _tripListUiState.value = TripListUiState.Success(formattedListTrip)
            } catch (e: Exception) {
                _tripListUiState.value = TripListUiState.Fail(FailReason.FIREBASE)
            }
        }

    }

    sealed class TripListUiState {
        object Loading : TripListUiState()
        data class Success(val tripItemList: List<TripItemList>) : TripListUiState()
        data class Fail(val failReason: FailReason) : TripListUiState()
    }
}