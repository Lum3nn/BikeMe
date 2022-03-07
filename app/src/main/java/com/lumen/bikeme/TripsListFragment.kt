package com.lumen.bikeme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lumen.bikeme.databinding.TripsListFragmentBinding

class TripsListFragment : Fragment() {

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

        binding.tripsListInfo.text = "Tu bedzie lista tripów : RecyclerView / Test bindingu"
        binding.floatingActionButton.setOnClickListener {
            val action =
                TripsListFragmentDirections.actionTripsListFragmentToTripsFormFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}