package hu.bme.aut.weatherinfo.model

data class WeatherData(
    var coord: Coord,
    var weather: List<Weather>?,
    var base: String,
    var main: MainWeatherData?,
    var visibility: Int,
    var wind: Wind?,
    var clouds: Cloud,
    var dt: Int,
    var sys: Sys,
    var timezone: Int,
    var id: Int,
    var name: String,
    var cod: Int
)