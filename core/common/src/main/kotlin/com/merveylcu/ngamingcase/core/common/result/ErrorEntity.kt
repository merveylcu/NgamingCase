package com.merveylcu.ngamingcase.core.common.result

public sealed class ErrorEntity {

    public data class Network(val reason: NetworkReason) : ErrorEntity() {
        public enum class NetworkReason { TIMEOUT, NO_INTERNET, CONNECTION, SSL, UNKNOWN }
    }

    public data class Http(val code: Int) : ErrorEntity()

    public data object Unknown : ErrorEntity()
}
