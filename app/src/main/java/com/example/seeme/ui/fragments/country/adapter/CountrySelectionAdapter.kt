package com.example.seeme.ui.fragments.country.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seeme.R
import com.example.seeme.ui.fragments.country.item.CountryItem
import com.example.seeme.ui.fragments.CountrySelectionInterface

class CountrySelectionAdapter(
    private val listener: CountrySelectionInterface,
    private val selectedCode: String
) : RecyclerView.Adapter<CountrySelectionAdapter.CountryViewHolder>() {

    private var countries: List<CountryItem> = listOf()

    fun setItems(items: List<CountryItem>) {
        countries = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country_selection, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.bind(country)
        holder.itemView.setOnClickListener {
            listener.onCountryItemSelected(country)
        }
    }

    override fun getItemCount(): Int = countries.size

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCountryName: TextView = itemView.findViewById(R.id.tvCountryName)
        private val tvDialCode: TextView = itemView.findViewById(R.id.tvDialCode)

        fun bind(item: CountryItem) {
            tvCountryName.text = item.name
            tvDialCode.text = item.dialCode
        }
    }
}
