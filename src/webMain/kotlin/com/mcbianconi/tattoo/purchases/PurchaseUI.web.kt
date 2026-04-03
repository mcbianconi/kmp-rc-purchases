package com.mcbianconi.tattoo.purchases

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

// https://www.revenuecat.com/docs/web/web-billing/web-purchase-links
class WebPurchaseUI : PurchaseUI {
    @Composable
    override fun Paywall(offeringIdentifier: String?, source: String, dismissRequest: () -> Unit) {
        LaunchedEffect(Unit) { dismissRequest() }
    }
}
