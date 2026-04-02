package com.mcbianconi.tattoo.purchases

class WebNetworkConnectivity : NetworkConnectivity {
    override suspend fun isConnected(): Boolean = true
}
