package coffeehouse.client.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coffeehouse.client.ui.shared.BlockingOverlayState
import coffeehouse.client.ui.shared.SpinButton
import kotlinx.coroutines.launch
import org.lighthousegames.logging.logging

@Composable
internal fun OrderContainer(
    orderLines: OrderLines,
    orderActions: OrderActions,
    blockingOverlayState: BlockingOverlayState,
    snackbarHostState: SnackbarHostState,
    innerPadding: PaddingValues
) {
    val scope = rememberCoroutineScope()
    val log = logging()

    val orderLineFlow by orderLines.collectAsState(context = scope.coroutineContext)

    Column(
        modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Container to display and manage the list of order lines.
        OrderLinesContainer(
            orderLines = orderLineFlow,
            onAddOrderLine = orderActions::addOrderLine,
            onRemoveOrderLine = orderActions::removeOrderLine,
            onOrderLineChanged = orderActions::changeOrderLine
        )

        // Spacer to push the order button to the bottom.
        Spacer(modifier = Modifier.weight(1f))

        // Order button that triggers the order placement process.
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                scope.launch {
                    if (orderLineFlow.isEmpty()) {
                        snackbarHostState.showSnackbar("Please add a new order line")
                    } else if (orderLineFlow.any { it.validate() }) {
                        snackbarHostState.showSnackbar("Please select a beverage")
                    } else {
                        // Block the UI during processing.
                        blockingOverlayState.block()

                        snackbarHostState.showSnackbar("Order processing...")
                        orderActions.placeOrder().onSuccess {
                            snackbarHostState.showSnackbar("Your order has been placed")
                            orderActions.prepareOrder()
                        }.onFailure { error ->
                            log.e { "Order placement failed: $error" }
                            snackbarHostState.showSnackbar("Your order could not be placed.")
                        }

                        // Unblock the UI.
                        blockingOverlayState.unblock()
                    }
                }
            }
        ) {
            Text("Order")
        }
    }
}

@Composable
internal fun OrderLinesContainer(
    orderLines: List<OrderLine>,
    onAddOrderLine: () -> Unit,
    onRemoveOrderLine: (source: OrderLine) -> Unit,
    onOrderLineChanged: (source: OrderLine) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Display each order lines
        for (orderLine in orderLines) {
            OrderLineItem(
                orderLine = orderLine,
                onNameChange = { newName ->
                    onOrderLineChanged(orderLine.copy(beverageName = newName))
                },
                onQuantityChange = { newQuantity ->
                    onOrderLineChanged(orderLine.copy(quantity = newQuantity))
                },
                onRemove = {
                    onRemoveOrderLine(orderLine)
                }
            )
        }

        // Button to add a new order line
        Button(onClick = onAddOrderLine) {
            Text("Add Order Line")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OrderLineItem(
    orderLine: OrderLine,
    onNameChange: (String) -> Unit,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val beverageOptions = listOf("Espresso", "Americano", "Latte")

    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dropdown menu for selecting a beverage.
        ExposedDropdownMenuBox(
            modifier = Modifier.weight(1f),
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth().fillMaxHeight()
            ) {
                // Display selected beverage or a placeholder if blank.
                Text(text = orderLine.beverageName.ifBlank { "Select Beverage" })
            }
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                beverageOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            // Invoke callback with the selected beverage.
                            onNameChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }

        // SpinButton for changing the quantity.
        SpinButton(
            modifier = Modifier.fillMaxHeight(),
            count = orderLine.quantity,
            onIncrement = { onQuantityChange(orderLine.quantity + 1) },
            onDecrement = {
                if (orderLine.quantity > 1) {
                    onQuantityChange(orderLine.quantity - 1)
                }
            }
        )

        // IconButton for remove the order line item.
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = onRemove
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete Order Line"
            )
        }
    }
}
