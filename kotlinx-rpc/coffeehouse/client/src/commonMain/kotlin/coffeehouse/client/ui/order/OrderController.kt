package coffeehouse.client.ui.order

import coffeehouse.modules.order.domain.OrderId

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
    val orderLines: OrderLines
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
                TODO("Processing for order placement")
            }
        } catch (error: Error) {
            Result.failure(error)
        }
    }

}
