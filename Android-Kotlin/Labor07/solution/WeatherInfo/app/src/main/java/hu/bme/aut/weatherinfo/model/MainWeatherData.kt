package hu.bme.aut.weatherinfo.model

data class MainWeatherData(
    val temp: Float,
    val pressure: Float,
    val humidity: Float,
    val temp_min: Float,
    val temp_max: Float
)