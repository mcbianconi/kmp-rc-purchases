package com.mcbianconi.tattoo.purchases

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.revenuecat.purchases.kmp.ui.revenuecatui.Paywall
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallListener
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallOptions

// https://www.revenuecat.com/docs/tools/paywalls/displaying-paywalls
class AndroidPurchaseUI(private val listener: PaywallListener) : PurchaseUI {
    @Composable
    override fun Paywall(offeringIdentifier: String?, source: String, dismissRequest: () -> Unit) {
        val options = remember(dismissRequest) {
            PaywallOptions(dismissRequest = dismissRequest) {
                shouldDisplayDismissButton = true
                listener = this@AndroidPurchaseUI.listener
            }
        }
        Paywall(options)
    }
}
