package coffeehouse.client.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

/**
 * A state holder for controlling a blocking overlay.
 * The [isBlocking] state indicates whether the overlay should be visible.
 */
@Stable
class BlockingOverlayState {
    var isBlocking by mutableStateOf(false)
        private set

    /**
     * Activate the blocking overlay.
     */
    suspend fun block() {
        isBlocking = true
    }

    /**
     * Deactivate the blocking overlay.
     */
    suspend fun unblock() {
        isBlocking = false
    }
}

/**
 * A composable host that displays its content and shows a blocking overlay on top when needed.
 */
@Composable
fun BlockingOverlayHost(
    blockingOverlayState: BlockingOverlayState,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Display the main content.
        content()
        // If blocking is active, display an overlay.
        if (blockingOverlayState.isBlocking) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                    .clickable(enabled = true, onClick = { })
            )
        }
    }
}
