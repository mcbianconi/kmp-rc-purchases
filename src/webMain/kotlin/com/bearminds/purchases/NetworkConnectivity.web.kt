package com.bearminds.purchases

class WebNetworkConnectivity : NetworkConnectivity {
    override suspend fun isConnected(): Boolean = true
}
