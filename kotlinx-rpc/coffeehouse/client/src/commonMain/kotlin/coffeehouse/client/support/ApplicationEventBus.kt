package coffeehouse.client.support

import kotlin.reflect.KClass

/**
 * Publishes application events asynchronously.
 */
fun interface ApplicationEventPublisher {
    suspend fun publishEvent(event: ApplicationEvent)
}

/**
 * Listens for specific application events.
 */
fun interface ApplicationEventListener<T : ApplicationEvent> {
    fun onEvent(event: T)
}

/**
 * Registers listeners for different application event types.
 */
interface ApplicationEventRegistry {
    fun <T : ApplicationEvent> registerListener(eventType: KClass<T>, listener: ApplicationEventListener<T>)
}

/**
 * Inline function to register a listener for a specific ApplicationEvent type.
 * Simplifies registration by inferring the event type.
 */
inline fun <reified T : ApplicationEvent> ApplicationEventRegistry.registerListener(
    listener: ApplicationEventListener<T>
) {
    registerListener(T::class, listener)
}

/**
 * Centralized event bus for publishing and registering application events.
 */
object ApplicationEventBus : ApplicationEventPublisher, ApplicationEventRegistry {

    private val listeners = mutableListOf<ListenerInvoker<out ApplicationEvent>>()

    override suspend fun publishEvent(event: ApplicationEvent) {
        listeners.forEach { it(event) }
    }

    override fun <T : ApplicationEvent> registerListener(eventType: KClass<T>, listener: ApplicationEventListener<T>) {
        listeners.add(ListenerInvoker(eventType, listener))
    }

    /**
     * Invoke application event listener based on event type.
     */
    private data class ListenerInvoker<T : ApplicationEvent>(
        val eventType: KClass<T>,
        val listener: ApplicationEventListener<T>
    ) {
        operator fun invoke(event: ApplicationEvent) {
            if (eventType.isInstance(event)) {
                @Suppress("UNCHECKED_CAST")
                listener.onEvent(event as T)
            }
        }
    }

}
