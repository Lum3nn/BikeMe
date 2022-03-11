package com.lumen.bikeme.tripForm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lumen.bikeme.commons.FailReason
import com.lumen.bikeme.commons.repository.TripRepository
import com.lumen.bikeme.commons.toDate
import com.lumen.bikeme.commons.model.TripItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripsFormViewModel @Inject constructor(
    private val tripRoomRepository: TripRepository
    )
    : ViewModel() {

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
                tripRoomRepository.insertTrip(TripItem(tripName, tripDistance, tripDate.toDate()))
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