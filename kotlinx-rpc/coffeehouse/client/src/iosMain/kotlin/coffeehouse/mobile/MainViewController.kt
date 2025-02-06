package coffeehouse.mobile

import androidx.compose.ui.window.ComposeUIViewController
import coffeehouse.client.ClientApplication

fun MainViewController() = ComposeUIViewController {
    ClientApplication()
}
