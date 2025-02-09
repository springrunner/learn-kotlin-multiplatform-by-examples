package coffeehouse.client.ui.order

import coffeehouse.modules.order.domain.OrderId
import coffeehouse.modules.order.domain.service.OrderPlacement

/**
 * Interface defining order actions for the UI
 */
internal interface OrderActions {

    /**
     * Reset or prepare the order state
     */
    fun prepareOrder()

    /**
     * Add a new order line
     */
    fun addOrderLine()

    /**
     * Update an existing order line
     */
    fun changeOrderLine(orderLine: OrderLine)

    /**
     * Delete an order line
     */
    fun removeOrderLine(orderLine: OrderLine)

    /**
     * Place the order and return the order ID
     */
    suspend fun placeOrder(): Result<OrderId>
}

/**
 * Controller that implements OrderActions, handling order operations
 */
internal class OrderController(
    val orderLines: OrderLines,
    val orderPlacement: OrderPlacement
) : OrderActions {

    override fun prepareOrder() {
        orderLines.reset()
    }

    override fun addOrderLine() {
        orderLines.add(OrderLine())
    }

    override fun changeOrderLine(orderLine: OrderLine) {
        orderLines.update(orderLine)
    }

    override fun removeOrderLine(orderLine: OrderLine) {
        orderLines.delete(orderLine)
    }

    override suspend fun placeOrder(): Result<OrderId> {
        return try {
            // Check for any errors in the order lines
            if (orderLines.hasError()) {
                Result.failure(Exception("There are invalid order-lines."))
            } else {
                val orderId = orderPlacement.placeOrder(
                    orderLines = orderLines.map {
                        OrderPlacement.OrderLine(it.beverageName, it.quantity)
                    }
                )
                Result.success(orderId)
            }
        } catch (error: Throwable) {
            Result.failure(error)
        }
    }

}
