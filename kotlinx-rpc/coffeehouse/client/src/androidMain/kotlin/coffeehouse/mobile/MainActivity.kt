package coffeehouse.mobile

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import coffeehouse.client.ClientApplication
import coffeehouse.core.env.Environment

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Environment.setPropertyResolver { key ->
            this.applicationContext
                .packageManager
                .getApplicationInfo(this.packageName, PackageManager.GET_META_DATA)
                .metaData?.getString(key)
        }

        setContent {
            ClientApplication()
        }
    }
}

@Preview
@Composable
fun ClientApplicationPreview() {
    ClientApplication()
}
