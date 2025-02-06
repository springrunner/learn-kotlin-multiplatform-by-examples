package coffeehouse.modules.order.domain

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class OrderId(private val value: String)
