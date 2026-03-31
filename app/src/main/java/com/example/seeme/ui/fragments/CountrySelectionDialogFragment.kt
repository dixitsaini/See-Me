package com.example.seeme.ui.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seeme.databinding.FragmentCountrySelectionBottomSheetDialogBinding
import com.example.seeme.ui.fragments.country.CountrySelectFragmentViewModel
import com.example.seeme.ui.fragments.country.adapter.CountrySelectionAdapter
import com.example.seeme.ui.fragments.country.item.CountryItem

class CountrySelectionDialogFragment(
    private val countrySelectionInterface: CountrySelectionInterface,
    private val countryCode: String = "+91"
) : DialogFragment(), CountrySelectionInterface {

    private var _binding: FragmentCountrySelectionBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    private val countrySelectFragmentViewModel: CountrySelectFragmentViewModel by viewModels()
    private lateinit var countrySelectionAdapter: CountrySelectionAdapter

    private val searchBarWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            val query = s?.toString().orEmpty().trim()
            if (query.isEmpty()) {
                countrySelectFragmentViewModel.displayCountries(requireContext())
            } else {
                countrySelectFragmentViewModel.searchCountry(query)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountrySelectionBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize Adapter
        countrySelectionAdapter = CountrySelectionAdapter(this, countryCode)
        
        // Setup RecyclerView
        binding.recyclerViewCountries.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = countrySelectionAdapter
            setHasFixedSize(true)
        }

        // Observe Data
        countrySelectFragmentViewModel.countryList.observe(viewLifecycleOwner) { list ->
            countrySelectionAdapter.setItems(list)
        }

        // Setup Search
        binding.editTextCountrySearch.addTextChangedListener(searchBarWatcher)

        // Load Data
        countrySelectFragmentViewModel.displayCountries(requireContext())
    }

    override fun onCountryItemSelected(countryItem: CountryItem) {
        countrySelectionInterface.onCountryItemSelected(countryItem)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
