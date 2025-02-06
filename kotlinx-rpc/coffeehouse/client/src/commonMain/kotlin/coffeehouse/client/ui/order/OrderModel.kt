package coffeehouse.client.ui.order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Data object representing a single order line.
 */
@OptIn(ExperimentalUuidApi::class)
internal data class OrderLine(
    val id: String = Uuid.random().toString(),
    val beverageName: String = "",
    val quantity: Int = 1
) {

    // Validate returns true if the order line is invalid.
    fun validate() = beverageName.isBlank() || quantity < 1
}

/**
 * First-class collection of OrderLine objects with observer support
 */
internal class OrderLines(initialItems: List<OrderLine> = listOf(OrderLine())) : Iterable<OrderLine> {

    private val _items = MutableStateFlow(initialItems)
    internal val items: StateFlow<List<OrderLine>> get() = _items.asStateFlow()

    /**
     * Add a new order line to the collection.
     */
    fun add(orderLine: OrderLine = OrderLine()) {
        _items.value += orderLine
    }

    /**
     * Update an existing order line (matched by unique id).
     */
    fun update(updatedOrderLine: OrderLine) {
        _items.value = _items.value.map { if (it.id == updatedOrderLine.id) updatedOrderLine else it }
    }

    /**
     * Remove an order line from the collection.
     */
    fun delete(orderLine: OrderLine) {
        _items.value = _items.value.filter { it.id != orderLine.id }
    }

    /**
     * Reset the order lines to a default state.
     */
    fun reset() {
        _items.value = listOf(OrderLine())
    }

    /**
     * Check if any order line in the collection is invalid.
     */
    fun hasError(): Boolean = _items.value.any { it.validate() }

    /**
     * Applies a transformation to each order line.
     */
    fun <R> map(transform: (OrderLine) -> R): List<R> {
        return _items.value.map(transform)
    }

    /**
     * Provide an iterator over the current order lines.
     */
    override fun iterator() = _items.value.iterator()

}

@Composable
internal fun OrderLines.collectAsState(
    context: CoroutineContext = EmptyCoroutineContext
): State<List<OrderLine>> = this.items.collectAsState(context = context)
