package coffeehouse.core.env

import platform.Foundation.NSBundle

actual object Environment {

    actual fun getProperty(key: String): String? {
        return NSBundle.mainBundle.infoDictionary?.get(key) as? String
    }

    actual fun getRequiredProperty(key: String): String {
        return getProperty(key) ?: throw NullPointerException("$key is not defined")
    }

}
