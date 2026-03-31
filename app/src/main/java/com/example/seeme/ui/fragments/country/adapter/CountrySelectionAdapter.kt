package com.example.seeme.ui.fragments.country.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seeme.R
import com.example.seeme.ui.fragments.CountrySelectionInterface
import com.example.seeme.ui.fragments.country.item.CountryItem

class CountrySelectionAdapter(
    private val listener: CountrySelectionInterface,
    private val selectedCode: String
) : RecyclerView.Adapter<CountrySelectionAdapter.CountryViewHolder>() {

    private var countries: ArrayList<CountryItem> = arrayListOf()

    fun setItems(items: List<CountryItem>) {
        countries.clear()
        countries.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country_selection, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.bind(country, selectedCode)
        holder.itemView.setOnClickListener {
            listener.onCountryItemSelected(country)
        }
    }

    override fun getItemCount(): Int = countries.size

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCountryName: TextView = itemView.findViewById(R.id.tvCountryName)
        private val tvDialCode: TextView = itemView.findViewById(R.id.tvDialCode)
        private val ivFlag: ImageView = itemView.findViewById(R.id.ivFlag)

        fun bind(item: CountryItem, selectedCode: String) {
            tvCountryName.text = item.name
            tvDialCode.text = item.dialCode
            
            // Highlight if selected
            itemView.isSelected = item.dialCode == selectedCode

            // Dynamically load flag drawable
            val context = itemView.context
            val resourceId = context.resources.getIdentifier(
                "country_${item.code.lowercase()}", 
                "drawable", 
                context.packageName
            )
            if (resourceId != 0) {
                ivFlag.setImageResource(resourceId)
            } else {
                ivFlag.setImageResource(android.R.drawable.ic_menu_help)
            }
        }
    }
}
