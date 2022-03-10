package com.lumen.bikeme.tripList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.lumen.bikeme.databinding.TripsListFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TripsListFragment :
    Fragment(),
    TripListAdapter.OnTripDelete,
    TripListAdapter.OnTrippAdd {
    private var _binding: TripsListFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TripsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TripsListFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {
            navigateToFormList()
        }

        val adapter = TripListAdapter(this, this)
        binding.tripsRecycler.layoutManager = LinearLayoutManager(context)
        binding.tripsRecycler.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(SwipeToDelete(adapter, requireContext()))
        itemTouchHelper.attachToRecyclerView(binding.tripsRecycler)
        observeFlow(adapter)
    }

    private fun navigateToFormList() {
        val action =
            TripsListFragmentDirections.actionTripsListFragmentToTripsFormFragment()
        findNavController().navigate(action)
    }

    private fun observeFlow(adapter: TripListAdapter) {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tripListUiState.collect { tripListUiState ->
                    when (tripListUiState) {
                        is TripsListViewModel.TripListUiState.Fail -> {}
                        TripsListViewModel.TripListUiState.Loading -> {}
                        is TripsListViewModel.TripListUiState.Success -> {
                            adapter.submitList(tripListUiState.tripItemList)
                        }
                    }
                }
            }
        }
    }

    override fun onTripSwipeToDelete(tripId: Int) {
        viewModel.deleteTrip(tripId)
    }

    override fun onTripAdd(tripName: String, tripDistance: String, tripDate: String) {
        viewModel.addUndoTrip(tripName, tripDistance, tripDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}