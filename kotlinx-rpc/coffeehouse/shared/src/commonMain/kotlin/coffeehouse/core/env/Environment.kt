package coffeehouse.core.env

expect object Environment {

    fun getProperty(key: String): String?

    fun getRequiredProperty(key: String): String

}
