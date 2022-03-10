package com.lumen.bikeme.tripList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lumen.bikeme.*
import com.lumen.bikeme.repository.TripRepository
import com.lumen.bikeme.repository.TripRoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TripsListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TripRepository = TripRoomRepository(application)
    private val _tripListUiState = MutableStateFlow<TripListUiState>(
        TripListUiState.Loading
    )
    val tripListUiState: StateFlow<TripListUiState> = _tripListUiState

    init {
        loadData()
    }

    private fun loadData() {
        _tripListUiState.value = TripListUiState.Loading

        viewModelScope.launch {
            try {
                val listTrip = repository.listTrips()
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

    fun deleteTrip(tripId: Int) {
        _tripListUiState.value = TripListUiState.Loading

        viewModelScope.launch {
            try {
                repository.deleteSingleTrip(tripId)
                val listTrip = repository.listTrips()
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
                repository.insertTrip(TripItem(tripName, tripDistance, tripDate.toDate()))
                val listTrip = repository.listTrips()
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