package com.mcbianconi.tattoo.purchases

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class WebPurchaseHelper : PurchaseHelper {

    private val proCustomerInfo = PurchaseCustomerInfoWrapper()

    override val isInitialized: Boolean = true

    override val isCustomerCenterSupported: Boolean = false

    override val cachedOfferings: PurchaseOfferings? = null

    override suspend fun initialize(apiKey: String) {}

    override suspend fun getOfferings(
        onSuccess: (PurchaseOfferings) -> Unit,
        onError: (PurchaseError) -> Unit
    ) {
        onError(StubPurchaseError())
    }

    override suspend fun purchase(
        packageToPurchase: PurchasePackage,
        onSuccess: (PurchaseStoreTransaction, PurchaseCustomerInfo) -> Unit,
        onError: (PurchaseError, Boolean) -> Unit
    ) {
        onSuccess(StubPurchaseStoreTransaction(), proCustomerInfo)
    }

    override suspend fun restorePurchases(
        onSuccess: (PurchaseCustomerInfo) -> Unit,
        onError: (PurchaseError) -> Unit
    ) {
        onSuccess(proCustomerInfo)
    }

    override suspend fun getCustomerInfo(
        forceRefresh: Boolean,
        onSuccess: (PurchaseCustomerInfo) -> Unit,
        onError: (PurchaseError) -> Unit
    ) {
        onSuccess(proCustomerInfo)
    }

    override suspend fun hasActiveEntitlement(entitlementIdentifier: String): Boolean = true

    override fun setPreferredLocale(locale: String) {}

    override fun setFirebaseAppInstanceId(firebaseAppInstanceId: String) {}

    @Composable
    override fun Paywall(offeringIdentifier: String?, source: String, dismissRequest: () -> Unit) {}

    @Composable
    override fun CustomerCenter(modifier: Modifier, dismissRequest: () -> Unit) {}
}
