package com.merveylcu.ngamingcase.core.common

public sealed interface UiText {
    public data class DynamicString(val value: String) : UiText

    public class StringResource(public val id: Int, public vararg val args: Any) : UiText
}
