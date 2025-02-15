package coffeehouse.client.remote

import coffeehouse.SERVER_PORT
import coffeehouse.modules.weather.domain.service.WeatherService
import io.ktor.client.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.rpc.krpc.ktor.client.rpc
import kotlinx.rpc.krpc.ktor.client.rpcConfig
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.rpc.withService

/**
 * Factory for creating kotlinx.rpc instances of weather module
 */
object KrpcWeatherClientFactory {

    /**
     * Builds a KrpcWeatherService instance.
     */
    fun buildWeatherService(
        httpClient: HttpClient,
        config: WeatherServiceConfig = WeatherServiceConfig()
    ): WeatherService = runBlocking {
        val weatherService = httpClient.rpc {
            url {
                host = config.host
                port = config.port
                encodedPath = config.path
            }
            rpcConfig {
                waitForServices = true
                serialization {
                    json()
                }
            }
        }.withService<WeatherService>()

        weatherService
    }

    data class WeatherServiceConfig(
        val host: String = "localhost",
        val port: Int = SERVER_PORT,
        val path: String = "/weather"
    )

}
