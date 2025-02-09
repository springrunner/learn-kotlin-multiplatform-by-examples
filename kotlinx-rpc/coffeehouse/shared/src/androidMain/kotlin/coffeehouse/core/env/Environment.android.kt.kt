package coffeehouse.core.env

import org.lighthousegames.logging.logging

actual object Environment {

    private var _propertyResolver: (String) -> String? = {
        throw IllegalStateException("Environment has not been initialized yet")
    }
    private val log = logging()

    fun setPropertyResolver(propertyResolver: (String) -> String?) {
        _propertyResolver = propertyResolver
    }

    actual fun getProperty(key: String): String? {
        return try {
            _propertyResolver(key)
        } catch (error: Exception) {
            log.error(err = error) { "Error resolving property '$key'" }
            null
        }
    }

    actual fun getRequiredProperty(key: String): String {
        return getProperty(key) ?: throw NullPointerException("$key is not defined")
    }

}
