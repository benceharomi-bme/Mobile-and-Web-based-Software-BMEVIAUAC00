package hu.bme.aut.weatherinfo.feature.details

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.weatherinfo.R
import hu.bme.aut.weatherinfo.model.WeatherData
import hu.bme.aut.weatherinfo.network.NetworkManager
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity(), WeatherDataHolder {
    private var city: String? = null
    private var weatherData: WeatherData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        city = intent.getStringExtra(EXTRA_CITY_NAME)
        supportActionBar?.title = getString(R.string.weather, city)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        loadWeatherData()
    }

    private fun loadWeatherData() {
        NetworkManager.getWeather(city, ::displayWeatherData, ::showError)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "DetailsActivity"
        const val EXTRA_CITY_NAME = "extra.city_name"
    }

    private fun displayWeatherData(receivedWeatherData: WeatherData) {
        weatherData = receivedWeatherData
        val detailsPagerAdapter = DetailsPagerAdapter(supportFragmentManager, this)
        mainViewPager.adapter = detailsPagerAdapter
    }

    private fun showError(throwable: Throwable) {
        throwable.printStackTrace()
        Toast.makeText(
            this,
            "Network request error occurred, check LOG",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun getWeatherData(): WeatherData? {
        return weatherData
    }
}