package hu.bme.aut.weatherinfo.city

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hu.bme.aut.weatherinfo.R
import hu.bme.aut.weatherinfo.city.AddCityDialogFragment
import hu.bme.aut.weatherinfo.city.CityAdapter
import hu.bme.aut.weatherinfo.feature.details.DetailsActivity
import kotlinx.android.synthetic.main.content_city.*

class CityActivity : AppCompatActivity(), CityAdapter.OnCitySelectedListener,
    AddCityDialogFragment.AddCityDialogListener {
    private lateinit var adapter: CityAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)
        initFab()
        initRecyclerView()
    }

    private fun initFab() {
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            AddCityDialogFragment().show(
                supportFragmentManager,
                AddCityDialogFragment::class.java.simpleName
            )
        }
    }

    private fun initRecyclerView() {
        MainRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CityAdapter(this)
        adapter.addCity("Budapest")
        adapter.addCity("Debrecen")
        adapter.addCity("Sopron")
        adapter.addCity("Szeged")
        MainRecyclerView.adapter = adapter
    }

    override fun onCitySelected(city: String?) {
        val showDetailsIntent = Intent()
        showDetailsIntent.setClass(this@CityActivity, DetailsActivity::class.java)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_CITY_NAME, city)
        startActivity(showDetailsIntent)
    }

    override fun onCityAdded(city: String) {
        adapter.addCity(city)
    }
}
