package com.lumen.bikeme.tripForm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lumen.bikeme.commons.FailReason
import com.lumen.bikeme.commons.repository.TripDataRepository
import com.lumen.bikeme.commons.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripsFormViewModel @Inject constructor(
    private val tripDataRepository: TripDataRepository
)
    : ViewModel() {

    private val _tripFormUiState = MutableStateFlow<TripFormUiState>(
        TripFormUiState.Loading
    )
    val tripFormUiState: StateFlow<TripFormUiState> = _tripFormUiState

    fun insertTrip(tripName: String, tripDistance: String, tripDate: String) {

        _tripFormUiState.value = TripFormUiState.Loading
        viewModelScope.launch {
            try {
                tripDataRepository.insertTrip(tripName, tripDistance, tripDate)
                _tripFormUiState.value = TripFormUiState.Success
            } catch (e: TripRepository.EmptyFieldException) {
                _tripFormUiState.value = TripFormUiState.Fail(FailReason.EMPTY_NOTE)
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