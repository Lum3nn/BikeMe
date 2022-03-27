package com.lumen.bikeme.tripList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.lumen.bikeme.commons.model.TripItemList
import com.lumen.bikeme.databinding.TripsListFragmentBinding
import com.lumen.bikeme.tripList.adapter.SwipeToDelete
import com.lumen.bikeme.tripList.adapter.TripListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TripsListFragment : Fragment(),
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

        val adapter = setupAdapter()
        observeFlow(adapter)
    }

    private fun setupAdapter(): TripListAdapter {
        val adapter = TripListAdapter(this, this)
        binding.tripsRecycler.layoutManager = LinearLayoutManager(context)
        binding.tripsRecycler.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(SwipeToDelete(adapter, requireContext()))
        itemTouchHelper.attachToRecyclerView(binding.tripsRecycler)

        return adapter
    }

    private fun navigateToFormList() {
        val action =
            TripsListFragmentDirections.actionTripsListFragmentToTripsFormFragment()
        findNavController().navigate(action)
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    private fun observeFlow(adapter: TripListAdapter) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tripListUiState.collect { tripListUiState ->
                    when (tripListUiState) {
                        is TripsListViewModel.TripListUiState.Fail -> {}
                        TripsListViewModel.TripListUiState.Loading -> {}
                        is TripsListViewModel.TripListUiState.Success -> {
                            toggleEmptyView(tripListUiState.tripItemList)
                            adapter.submitList(tripListUiState.tripItemList)
                        }
                    }
                }
            }
        }
    }

    private fun toggleEmptyView(tripItemList: List<TripItemList>) {
        binding.emptyView.isVisible = tripItemList.isEmpty()
    }

    override fun onTripSwipeToDelete(tripId: String) {
        viewModel.deleteTrip(tripId)
    }

    override fun onTripAdd(
        tripName: String,
        tripDistance: String,
        tripDate: String,
        tripId: String
    ) {
        viewModel.addUndoTrip(tripName, tripDistance, tripDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}