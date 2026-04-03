package com.mcbianconi.tattoo.purchases

import io.github.oshai.kotlinlogging.KotlinLogging
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * JVM implementation of PurchaseHelper that grants pro access by default.
 * Purchases are not supported on desktop JVM, but we auto-grant pro for development.
 */
private val logger = KotlinLogging.logger {}

class JVMPurchaseHelper : PurchaseHelper {

    private val proCustomerInfo = PurchaseCustomerInfoWrapper()

    override val isInitialized: Boolean = true  // Always initialized on JVM (no SDK needed)

    override val isCustomerCenterSupported: Boolean = false  // RevenueCat doesn't support desktop

    override val cachedOfferings: PurchaseOfferings? = null

    override suspend fun initialize(apiKey: String) {
        // JVM platform - no initialization needed, pro access is auto-granted
    }

    override suspend fun getOfferings(
        onSuccess: (PurchaseOfferings) -> Unit,
        onError: (PurchaseError) -> Unit
    ) {
        logger.debug { "PurchaseHelper: JVM platform - getOfferings not supported (pro already granted)" }
        onError(StubPurchaseError())
    }

    override suspend fun purchase(
        packageToPurchase: PurchasePackage,
        onSuccess: (PurchaseStoreTransaction, PurchaseCustomerInfo) -> Unit,
        onError: (PurchaseError, Boolean) -> Unit
    ) {
        logger.debug { "PurchaseHelper: JVM platform - purchase not needed (pro already granted)" }
        // Auto-complete purchase since pro is granted
        onSuccess(StubPurchaseStoreTransaction(), proCustomerInfo)
    }

    override suspend fun restorePurchases(
        onSuccess: (PurchaseCustomerInfo) -> Unit,
        onError: (PurchaseError) -> Unit
    ) {
        logger.debug { "PurchaseHelper: JVM platform - restore returning pro access" }
        onSuccess(proCustomerInfo)
    }

    override suspend fun getCustomerInfo(
        forceRefresh: Boolean,
        onSuccess: (PurchaseCustomerInfo) -> Unit,
        onError: (PurchaseError) -> Unit
    ) {
        logger.debug { "PurchaseHelper: JVM platform - returning pro customer info (forceRefresh=$forceRefresh)" }
        onSuccess(proCustomerInfo)
    }

    override suspend fun hasActiveEntitlement(entitlementIdentifier: String): Boolean {
        logger.debug { "PurchaseHelper: JVM platform - hasActiveEntitlement returning true (pro granted)" }
        return true
    }

    override fun setPreferredLocale(locale: String) {
        logger.debug { "PurchaseHelper: JVM platform - setPreferredLocale not supported" }
    }

    override fun setFirebaseAppInstanceId(firebaseAppInstanceId: String) {
        logger.debug { "PurchaseHelper: JVM platform - setFirebaseAppInstanceId not supported" }
    }

    @Composable
    override fun Paywall(offeringIdentifier: String?, source: String, dismissRequest: () -> Unit) {
        logger.debug { "PurchaseHelper: JVM platform - Paywall not supported (source: $source, offering: $offeringIdentifier)" }
    }

    @Composable
    override fun CustomerCenter(modifier: Modifier, dismissRequest: () -> Unit) {
        // Not supported on JVM - CustomerCenterScreenDestination handles showing unavailable screen
    }
}

