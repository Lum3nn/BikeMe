package com.lumen.bikeme.tripForm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lumen.bikeme.databinding.TripsFormFragmentBinding
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.lumen.bikeme.FailReason
import com.lumen.bikeme.R
import com.lumen.bikeme.tripForm.TripsFormViewModel.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TripsFormFragment : Fragment() {

    private var _binding: TripsFormFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TripsFormViewModel by viewModels()

    private val myCalendar: Calendar = Calendar.getInstance()
    private val myFormat = "dd-MM-yyyy"
    private val dateFormat = SimpleDateFormat(myFormat, Locale.US)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TripsFormFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tripDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.saveTripFormBtn.setOnClickListener {
            saveTrip()
        }

        binding.cancelTripFormBtn.setOnClickListener {
            viewModel.cancelAddingTrip()
        }

        observeFlow()
    }

    private fun observeFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tripFormUiState.collect { tripFormUiState ->
                    when (tripFormUiState) {
                        is TripFormUiState.Fail -> {
                            showError(tripFormUiState.failReason)
                        }
                        TripFormUiState.Success, TripFormUiState.Leave -> {
                            val action =
                                TripsFormFragmentDirections.actionTripsFormFragmentToTripsListFragment()
                            findNavController().navigate(action)
                        }
                        TripFormUiState.Loading -> {}
                    }
                }
            }
        }
    }

    private fun showError(failReason: FailReason) {
        val errorText = when (failReason) {
            FailReason.EMPTY_NOTE -> R.string.empty_note
            FailReason.FIREBASE -> R.string.something_went_wrong
        }

        Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
    }

    private fun saveTrip() {
        val tripName = binding.tripName.text.toString()
        val tripDistance = binding.tripDistance.text.toString()
        val tripDate = binding.tripDate.text.toString()

        viewModel.insertTrip(tripName, tripDistance, tripDate)
    }

    private fun showDatePickerDialog() {
        val dateListener =
            OnDateSetListener { _, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel()
            }

        DatePickerDialog(
            requireContext(),
            dateListener,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateLabel() {
        binding.tripDate.setText(dateFormat.format(myCalendar.time))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}