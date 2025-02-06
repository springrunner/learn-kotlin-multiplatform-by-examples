package coffeehouse.client.support

sealed class ApplicationEvent {

    data class PageLaunched(val pageName: String) : ApplicationEvent()

}
