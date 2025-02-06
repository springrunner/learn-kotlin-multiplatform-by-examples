package coffeehouse.client

import androidx.compose.material3.*
import androidx.compose.runtime.*
import coffeehouse.client.support.ApplicationEvent
import coffeehouse.client.support.ApplicationEventBus
import coffeehouse.client.support.registerListener
import coffeehouse.client.ui.order.OrderPage
import coffeehouse.client.ui.shared.BlockingOverlayHost
import coffeehouse.client.ui.shared.BlockingOverlayState
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Root composable for the client application.
 */
@Preview
@Composable
fun ClientApplication() {
    val blockingOverlayState = remember { BlockingOverlayState() }
    val snackbarHostState = remember { SnackbarHostState() }

    var appBarTitle by remember { mutableStateOf(value = "Coffeehouse") }
    ApplicationEventBus.registerListener<ApplicationEvent.PageLaunched> { event ->
        appBarTitle = event.pageName
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                @OptIn(ExperimentalMaterial3Api::class)
                TopAppBar(
                    title = { Text(text = appBarTitle) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            content = { innerPadding ->
                // BlockingOverlayHost wraps the OrderPage to show overlay when needed.
                BlockingOverlayHost(blockingOverlayState = blockingOverlayState) {
                    OrderPage(blockingOverlayState, snackbarHostState, innerPadding)
                }
            }
        )
    }
}
