package com.mcbianconi.tattoo.purchases

import androidx.compose.runtime.Composable

interface PurchaseUI {
    @Composable
    fun Paywall(offeringIdentifier: String?, source: String, dismissRequest: () -> Unit)
}
