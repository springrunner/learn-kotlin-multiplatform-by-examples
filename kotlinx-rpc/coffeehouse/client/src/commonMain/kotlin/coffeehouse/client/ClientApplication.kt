package coffeehouse.client

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coffeehouse.client.remote.KrpcOrderClientFactory
import coffeehouse.client.support.ApplicationEvent
import coffeehouse.client.support.ApplicationEventBus
import coffeehouse.client.support.registerListener
import coffeehouse.client.ui.order.OrderPage
import coffeehouse.client.ui.shared.BlockingOverlayHost
import coffeehouse.client.ui.shared.BlockingOverlayState
import coffeehouse.core.env.Environment
import coffeehouse.modules.order.domain.service.OrderPlacement
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import kotlinx.rpc.krpc.ktor.client.Krpc
import org.jetbrains.compose.ui.tooling.preview.Preview

val httpClient by lazy {
    HttpClient {
        install(WebSockets)
        install(Krpc)
    }
}

/**
 * Root composable for the client application.
 */
@Preview
@Composable
fun ClientApplication() {
    val rpcOrderClientProperties = KrpcOrderClientFactory.OrderPlacementConfig(
        host = Environment.getRequiredProperty("krpc.host")
    )

    // Produce OrderPlacement state asynchronously; initial value is null.
    val orderPlacement by produceState<OrderPlacement?>(initialValue = null) {
        value = KrpcOrderClientFactory.buildOrderPlacement(httpClient, rpcOrderClientProperties)
    }

    // If dependencies is ready, display the ClientApplication within a themed UI.
    if (orderPlacement != null) {
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
                        OrderPage(orderPlacement!!, blockingOverlayState, snackbarHostState, innerPadding)
                    }
                }
            )
        }
    } else {
        // While loading, display a centered circular progress indicator.
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
