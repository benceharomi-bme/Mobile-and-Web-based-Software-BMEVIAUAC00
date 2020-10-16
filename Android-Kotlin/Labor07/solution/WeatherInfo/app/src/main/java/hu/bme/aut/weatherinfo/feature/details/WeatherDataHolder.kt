package hu.bme.aut.weatherinfo.feature.details

import hu.bme.aut.weatherinfo.model.WeatherData

interface WeatherDataHolder {
    fun getWeatherData(): WeatherData?
}