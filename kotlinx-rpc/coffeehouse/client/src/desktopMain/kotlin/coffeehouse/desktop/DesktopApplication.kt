package coffeehouse.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import coffeehouse.client.ClientApplication

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "coffeehouse",
    ) {
        ClientApplication()
    }
}
