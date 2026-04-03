package com.mcbianconi.tattoo.purchases

import androidx.compose.runtime.Composable

class MacosPurchaseUI : PurchaseUI {
    @Composable
    override fun Paywall(offeringIdentifier: String?, source: String, dismissRequest: () -> Unit) = Unit
}
