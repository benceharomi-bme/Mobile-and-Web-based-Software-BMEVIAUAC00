package hu.bme.aut.weatherinfo.model

data class Sys(
    var type: Int,
    var id: Int,
    var country: String,
    var sunrise: Int,
    var sunset: Int
)