package com.example.seeme.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.seeme.databinding.FragmentCountrySelectionBottomSheetDialogBinding
import com.example.seeme.ui.fragments.country.CountrySelectFragmentViewModel
import com.example.seeme.ui.fragments.country.adapter.CountrySelectionAdapter
import com.example.seeme.ui.fragments.country.item.CountryItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CountrySelectionBottomSheetDialogFragment(
    private val countrySelectionInterface: CountrySelectionInterface,
    private val countryCode: String = "+91"
) : BottomSheetDialogFragment(), CountrySelectionInterface {

    private var _binding: FragmentCountrySelectionBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    private val countrySelectFragmentViewModel: CountrySelectFragmentViewModel by viewModels()
    private lateinit var countrySelectionAdapter: CountrySelectionAdapter
    private var countryList: List<CountryItem> = emptyList()

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
        countrySelectionAdapter = CountrySelectionAdapter(this, countryCode)
        binding.recyclerViewCountries.adapter = countrySelectionAdapter

        countrySelectFragmentViewModel.countryList.observe(viewLifecycleOwner, { list ->
            countryList = list
            countrySelectionAdapter.setItems(list)
        })

        binding.editTextCountrySearch.addTextChangedListener(searchBarWatcher)

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
