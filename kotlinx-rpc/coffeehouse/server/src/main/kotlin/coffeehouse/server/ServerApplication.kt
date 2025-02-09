package coffeehouse.server

import coffeehouse.SERVER_HOST
import coffeehouse.SERVER_PORT
import coffeehouse.modules.order.domain.service.OrderPlacement
import coffeehouse.modules.order.domain.service.support.NoOpOrderPlacementProcessor
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import kotlinx.rpc.krpc.ktor.server.Krpc
import kotlinx.rpc.krpc.ktor.server.rpc
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = SERVER_HOST) {
        install(Krpc)
        routing {
            rpc("/order") {
                rpcConfig {
                    serialization { json(Json) }
                }
                registerService<OrderPlacement> { ctx ->
                    NoOpOrderPlacementProcessor(ctx)
                }
            }
        }
    }.start(wait = true)
}
