package com.example.seeme.ui.fragments.country

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seeme.ui.fragments.country.item.CountryItem
import org.json.JSONObject

class CountrySelectFragmentViewModel : ViewModel() {

    private val _countryList = MutableLiveData<List<CountryItem>>()
    val countryList: LiveData<List<CountryItem>> = _countryList

    fun displayCountries(context: Context) {
        val json = loadCountriesJsonFromAssets(context)
        if (json != null) {
            _countryList.value = parseCountriesFromJson(json)
        } else {
            _countryList.value = emptyList()
        }
    }

    fun searchCountry(query: String) {
        val currentCountries = _countryList.value ?: emptyList()
        _countryList.value = if (query.isBlank()) {
            currentCountries
        } else {
            currentCountries.filter {
                it.name.contains(query, ignoreCase = true) || it.dialCode.contains(query)
            }
        }
    }

    private fun loadCountriesJsonFromAssets(context: Context): String? {
        return try {
            context.assets.open("countries.json").bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun parseCountriesFromJson(jsonString: String): List<CountryItem> {
        val list = mutableListOf<CountryItem>()
        try {
            val parent = JSONObject(jsonString)
            val countriesJson = parent.optJSONArray("countries")
            if (countriesJson != null) {
                for (i in 0 until countriesJson.length()) {
                    val item = countriesJson.optJSONObject(i)
                    if (item != null) {
                        list.add(
                            CountryItem(
                                name = item.optString("name"),
                                dialCode = item.optString("phone_code"),
                                code = item.optString("name_code")
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}

