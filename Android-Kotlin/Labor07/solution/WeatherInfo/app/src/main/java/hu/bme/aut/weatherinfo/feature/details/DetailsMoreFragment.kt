package hu.bme.aut.weatherinfo.feature.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import hu.bme.aut.weatherinfo.R
import kotlinx.android.synthetic.main.fragment_details_more.*

class DetailsMoreFragment : Fragment() {
    private var weatherDataHolder: WeatherDataHolder? = null
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherDataHolder = if (activity is WeatherDataHolder) {
            activity as WeatherDataHolder?
        } else {
            throw RuntimeException("Activity must implement WeatherDataHolder interface!")
        }
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (weatherDataHolder?.getWeatherData() != null) {
            showWeatherData()
        }
    }

    private fun showWeatherData() {
        val weatherData = weatherDataHolder!!.getWeatherData()
        tvTemperature.text = weatherData?.main?.temp.toString()
        tvMinTemp.text = weatherData?.main?.temp_min.toString()
        tvMaxTemp.text = weatherData?.main?.temp_max.toString()
        tvPressure.text = weatherData?.main?.pressure.toString()
        tvHumidity.text = weatherData?.main?.humidity.toString()
    }
}