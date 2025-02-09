package coffeehouse.client.remote

import coffeehouse.SERVER_PORT
import coffeehouse.modules.order.domain.service.OrderPlacement
import io.ktor.client.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.rpc.krpc.ktor.client.rpc
import kotlinx.rpc.krpc.ktor.client.rpcConfig
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.rpc.withService
import kotlin.coroutines.CoroutineContext

/**
 * Factory for creating kotlinx.rpc instances of order module
 */
object KrpcOrderClientFactory {

    /**
     * Builds a KrpcOrderPlacement instance.
     */
    fun buildOrderPlacement(
        httpClient: HttpClient,
        config: OrderPlacementConfig = OrderPlacementConfig()
    ) = KrpcOrderPlacement(httpClient, config)


    data class OrderPlacementConfig(
        val host: String = "localhost",
        val port: Int = SERVER_PORT,
        val path: String = "/order"
    )

    class KrpcOrderPlacement(
        private val httpClient: HttpClient,
        private val config: OrderPlacementConfig,
    ) : OrderPlacement {

        private var orderPlacement: OrderPlacement? = null

        /**
         * Ensures that the delegate OrderPlacement instance is initialized.
         * Attempts to establish a connection to the server if it's not already established.
         */
        private suspend fun ensureDelegate(): OrderPlacement {
            if (orderPlacement == null) {
                var attempts = 0
                while (attempts < MAX_ATTEMPTS && orderPlacement == null) {
                    orderPlacement = httpClient.rpc {
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
                    }.withService<OrderPlacement>()
                    if (orderPlacement == null) {
                        attempts++
                        delay(RETRY_DELAY)
                    }
                }
            }
            return orderPlacement
                ?: throw IllegalStateException("RPC client creation failed after $MAX_ATTEMPTS attempts. Cannot connect to server.")
        }

        override suspend fun placeOrder(orderLines: List<OrderPlacement.OrderLine>) = try {
            ensureDelegate().placeOrder(orderLines)
        } catch (error: Exception) {
            throw error
        }

        override val coroutineContext: CoroutineContext
            get() = orderPlacement?.coroutineContext ?: throw IllegalStateException("Uninitialized coroutine context")

        companion object {
            private const val MAX_ATTEMPTS = 3
            private const val RETRY_DELAY = 100L
        }
    }

}
