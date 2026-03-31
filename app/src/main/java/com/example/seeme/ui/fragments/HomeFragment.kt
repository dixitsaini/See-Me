package com.example.seeme.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.seeme.R
import com.example.seeme.databinding.FragmentHomeBinding
import com.example.seeme.ui.ClickCounterViewModel
import com.example.seeme.data.AppDatabase
import com.example.seeme.data.ClickCounterRepository
import com.example.seeme.ui.ClickCounterViewModelFactory

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClickCounterViewModel by viewModels {
        val database = AppDatabase.getDatabase(requireContext())
        val repository = ClickCounterRepository(database.clickCounterDao())
        ClickCounterViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.btnClickMe.setOnClickListener {
            viewModel.onClickButtonPressed()
        }

        binding.btnReset.setOnClickListener {
            viewModel.resetClicks()
        }

        binding.btnOpenMap.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mapFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
