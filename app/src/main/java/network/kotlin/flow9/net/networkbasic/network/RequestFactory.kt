package network.kotlin.flow9.net.networkbasic.network

import network.kotlin.flow9.net.networkbasic.domain.Melon
import network.kotlin.flow9.net.networkbasic.network.request.MelonRequest
import network.kotlin.flow9.net.networkbasic.network.request.NetworkRequest

class RequestFactory {

    companion object {

        val melonRequest: NetworkRequest<*>
            get() = MelonRequest(1, 10)

        fun getMelonRequest(listener: NetworkManager.OnResultListener<Melon>): NetworkRequest<*> {
            val request = MelonRequest(1, 10)
            request.setOnResultListener(listener)
            return request
        }

    }
}
