package com.lumen.bikeme.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lumen.bikeme.databinding.MapsFragmentBinding

class MapsFragment : Fragment() {

    private var _binding: MapsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MapsFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }
}