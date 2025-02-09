package coffeehouse.modules.order.domain.service.support

import coffeehouse.modules.order.domain.OrderId
import coffeehouse.modules.order.domain.service.OrderPlacement
import org.lighthousegames.logging.logging
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * A no-operation implementation of the [OrderPlacement] use cases.
 */
class NoOpOrderPlacementProcessor(override val coroutineContext: CoroutineContext) : OrderPlacement {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun placeOrder(orderLines: List<OrderPlacement.OrderLine>): OrderId {
        log.info { "Placing order with $orderLines lines" }

        return OrderId(Uuid.random().toString())
    }

    companion object {
        val log = logging()
    }
}
