package coffeehouse.modules.weather.domain

import kotlinx.serialization.Serializable

@Serializable
data class Temperature(val value: Double, val unit: String = "℃")

@Serializable
enum class WeatherCondition {
    SUNNY,
    CLOUDY,
    RAINY,
    SNOWY,
    THUNDERSTORM,
    FOGGY,
    WINDY,
    PARTLY_CLOUDY
}

@Serializable
data class Wind(val speed: Double, val unit: String = "m/s")

@Serializable
data class Humidity(val value: Int, val unit: String = "%")

@Serializable
data class Weather(
    val location: String,
    val temperature: Temperature,
    val condition: WeatherCondition,
    val humidity: Humidity?,
    val wind: Wind?
)
