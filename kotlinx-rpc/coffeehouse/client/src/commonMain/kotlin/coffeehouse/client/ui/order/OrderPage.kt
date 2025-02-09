package coffeehouse.client.ui.order

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import coffeehouse.client.support.ApplicationEvent
import coffeehouse.client.support.ApplicationEventBus
import coffeehouse.client.ui.shared.BlockingOverlayState
import coffeehouse.modules.order.domain.service.OrderPlacement

/**
 * Composable entry point for the Order Page.
 */
@Composable
fun OrderPage(
    orderPlacement: OrderPlacement,
    blockingOverlayState: BlockingOverlayState,
    snackbarHostState: SnackbarHostState,
    innerPadding: PaddingValues
) {
    val orderLines = remember { OrderLines() }
    val controller = remember { OrderController(orderLines = orderLines, orderPlacement = orderPlacement) }

    OrderContainer(
        orderLines = orderLines,
        orderActions = controller,
        blockingOverlayState = blockingOverlayState,
        snackbarHostState = snackbarHostState,
        innerPadding = innerPadding,
    )

    LaunchedEffect(Unit) {
        ApplicationEventBus.publishEvent(
            event = ApplicationEvent.PageLaunched(pageName = "Coffeehouse Order")
        )
    }
}
