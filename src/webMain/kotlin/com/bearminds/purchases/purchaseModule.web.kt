package com.bearminds.purchases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformPurchaseModule: Module = module {

    single<NetworkConnectivity> {
        WebNetworkConnectivity()
    }

    single<PurchaseHelper> {
        WebPurchaseHelper()
    }

    single<PurchaseStateManager> {
        PurchaseStateManagerImpl(
            purchaseHelper = get(),
            scope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
            entitlementId = "ignore"
        )
    }
}
