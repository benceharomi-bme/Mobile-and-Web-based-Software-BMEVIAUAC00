package hu.bme.aut.weatherinfo.city

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hu.bme.aut.weatherinfo.R
import kotlinx.android.synthetic.main.item_city.view.*


class CityAdapter internal constructor(private val listener: OnCitySelectedListener?) :
    RecyclerView.Adapter<CityAdapter.CityViewHolder>() {
    private val cities: MutableList<String>

    interface OnCitySelectedListener {
        fun onCitySelected(city: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val item = cities[position]
        holder.nameTextView.text = cities[position]
        holder.item = item
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    fun addCity(newCity: String) {
        cities.add(newCity)
        notifyItemInserted(cities.size - 1)
    }

    fun removeCity(position: Int) {
        cities.removeAt(position)
        notifyItemRemoved(position)
        if (position < cities.size) {
            notifyItemRangeChanged(position, cities.size - position)
        }
    }

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.CityItemNameTextView
        val removeButton = itemView.CityItemRemoveButton
        var item: String? = null

        init {
            itemView.setOnClickListener { listener?.onCitySelected(item) }
            removeButton.setOnClickListener { removeCity(cities.indexOf(item)) }
        }
    }

    init {
        cities = ArrayList()
    }
}