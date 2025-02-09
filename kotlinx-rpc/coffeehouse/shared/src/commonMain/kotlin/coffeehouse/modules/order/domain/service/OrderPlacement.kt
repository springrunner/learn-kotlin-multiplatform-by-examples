package coffeehouse.modules.order.domain.service

import coffeehouse.modules.order.domain.OrderId
import kotlinx.rpc.RemoteService
import kotlinx.rpc.annotations.Rpc
import kotlinx.serialization.Serializable

/**
 * Use cases for placing orders.
 */
@Rpc
interface OrderPlacement : RemoteService {

    /**
     * Places an order with the provided order lines.
     */
    suspend fun placeOrder(orderLines: List<OrderLine>): OrderId

    @Serializable
    data class OrderLine(val beverageName: String, val quantity: Int)

}
