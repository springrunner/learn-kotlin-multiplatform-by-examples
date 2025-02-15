package coffeehouse.modules.weather.domain.service

import coffeehouse.modules.weather.domain.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.rpc.RemoteService
import kotlinx.rpc.annotations.Rpc

/**
 * RPC interface for retrieving weather information.
 */
@Rpc
interface WeatherService : RemoteService {

    /**
     * Retrieves the current weather information for a given location.
     */
    suspend fun obtainWeather(location: String): Weather

    /**
     * Continuously observes weather updates for a given location.
     */
    suspend fun observeWeather(location: String): Flow<Weather>

}
