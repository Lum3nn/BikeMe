package com.lumen.bikeme.tripForm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lumen.bikeme.FailReason
import com.lumen.bikeme.repository.TripRepository
import com.lumen.bikeme.repository.TripRoomRepository
import com.lumen.bikeme.toDate
import com.lumen.bikeme.tripList.TripItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TripsFormViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TripRepository = TripRoomRepository(application)

    private val _tripFormUiState = MutableStateFlow<TripFormUiState>(
        TripFormUiState.Loading
    )
    val tripFormUiState: StateFlow<TripFormUiState> = _tripFormUiState

    fun insertTrip(tripName: String, tripDistance: String, tripDate: String) {

        _tripFormUiState.value = TripFormUiState.Loading

        if (tripName.isEmpty() || tripDistance.isEmpty() || tripDate.isEmpty()) {
            _tripFormUiState.value = TripFormUiState.Fail(FailReason.EMPTY_NOTE)
            return
        }

        viewModelScope.launch {
            try {
                repository.insertTrip(TripItem(tripName, tripDistance, tripDate.toDate()))
                _tripFormUiState.value = TripFormUiState.Success
            } catch (e: Exception) {
                _tripFormUiState.value = TripFormUiState.Fail(FailReason.FIREBASE)
            }
        }
    }

    fun cancelAddingTrip() {
        _tripFormUiState.value = TripFormUiState.Leave
    }

    sealed class TripFormUiState {
        object Loading : TripFormUiState()
        object Leave : TripFormUiState()
        object Success : TripFormUiState()
        data class Fail(val failReason: FailReason) : TripFormUiState()
    }
}