package coffeehouse.modules.weather.domain.service.support

import coffeehouse.modules.weather.domain.*
import coffeehouse.modules.weather.domain.service.WeatherService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class RandomWeatherService(override val coroutineContext: CoroutineContext) : WeatherService {

    private val fetchWeather: (String) -> Weather = { location ->
        val condition = WeatherCondition.entries.toTypedArray().random()
        val temperatureValue = when (condition) {
            WeatherCondition.SNOWY -> Random.nextDouble(-15.0, 0.0)
            else -> Random.nextDouble(-5.0, 38.0)
        }

        Weather(
            location = location,
            temperature = Temperature(value = temperatureValue),
            condition = condition,
            humidity = Humidity(value = Random.nextInt(0, 101)),
            wind = Wind(speed = Random.nextDouble(0.0, 20.1))
        )
    }

    override suspend fun obtainWeather(location: String): Weather {
        return fetchWeather(location)
    }

    override suspend fun observeWeather(location: String): Flow<Weather> {
        return flow {
            emit(fetchWeather(location))

            // continue emitting updated weather information.
        }
    }

}
