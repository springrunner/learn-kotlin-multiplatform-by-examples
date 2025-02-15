package coffeehouse.client.remote

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.take
import kotlinx.rpc.krpc.ktor.client.Krpc
import kotlinx.rpc.krpc.streamScoped
import kotlin.test.Ignore
import kotlin.test.Test

/**
 * This test is disabled for demo
 */
internal class KrpcWeatherClientDemo {

    private val httpClient = HttpClient() {
        install(WebSockets)
        install(Krpc)
    }

    @Ignore
    @Test
    fun runObtainWeather() = runBlocking {
        // Build the WeatherService instance using the factory.
        val weatherService = KrpcWeatherClientFactory.buildWeatherService(httpClient)

        // Call obtainWeather to retrieve current weather information for "London".
        val currentWeather = weatherService.obtainWeather("London")
        println("Current weather in London: $currentWeather")
    }

    @Ignore
    @Test
    fun runObtainWeatherWithTimeout() = runBlocking {
        // Build the WeatherService instance using the factory.
        val weatherService = KrpcWeatherClientFactory.buildWeatherService(httpClient)

        try {
            withTimeout(1500) {
                // Call obtainWeather to retrieve current weather information for "London".
                val currentWeather = weatherService.obtainWeather("London")
                println("Current weather in London: $currentWeather")
            }
        } catch (timeout: TimeoutCancellationException) {
            // handle timeout
        }
    }

    @Ignore
    @Test
    fun runObtainWeathers() = runBlocking {
        // Build the WeatherService instance using the factory.
        val weatherService = KrpcWeatherClientFactory.buildWeatherService(httpClient)

        // Define a list of UK cities
        val cities = listOf("London", "Manchester", "Birmingham", "Edinburgh", "Glasgow")

        // Launch concurrent async tasks for each city to fetch weather data
        val weatherDeferreds = cities.map { city ->
            async { city to weatherService.obtainWeather(city) }
        }

        // Await all responses concurrently
        val weatherResults = weatherDeferreds.awaitAll()

        // Print out the weather for each city
        weatherResults.forEach { (city, weather) ->
            println("Current weather in $city: $weather")
        }
    }

    @Ignore
    @Test
    fun runObserveWeather() = runBlocking {
        // Build the WeatherService instance.
        val weatherService = KrpcWeatherClientFactory.buildWeatherService(httpClient)

        // Call observeWeather to continuously receive weather updates for "London".
        println("Observing weather updates for London:")
        streamScoped {
            // For demonstration, collect only the first 3 updates.
            weatherService.observeWeather("London")
                .take(3)
                .collect { update -> println("Weather update: $update") }
        }
    }
}
