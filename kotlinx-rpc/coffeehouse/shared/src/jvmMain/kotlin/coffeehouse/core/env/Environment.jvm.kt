package coffeehouse.core.env

import java.util.*

actual object Environment {

    private val properties = Properties().apply {
        val stream = Environment::class.java.classLoader.getResourceAsStream("application.properties")
        if (stream != null) {
            load(stream)
            stream.close()
        }
    }

    actual fun getProperty(key: String): String? {
        return properties.getProperty(key) ?: System.getProperty(key)
    }

    actual fun getRequiredProperty(key: String): String {
        return getProperty(key) ?: throw NullPointerException("$key is not defined")
    }

}
