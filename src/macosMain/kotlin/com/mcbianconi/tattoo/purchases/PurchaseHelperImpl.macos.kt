package com.mcbianconi.tattoo.purchases

import io.github.oshai.kotlinlogging.KotlinLogging
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * macOS implementation of PurchaseHelper that grants pro access by default.
 * RevenueCat doesn't support macOS natively, so we auto-grant pro for development.
 */
private val logger = KotlinLogging.logger {}

class MacosPurchaseHelper : PurchaseHelper {

    private val proCustomerInfo = MacosPurchaseCustomerInfo()

    override val isInitialized: Boolean = true  // Always initialized on macOS (no SDK needed)

    override val isCustomerCenterSupported: Boolean = false  // RevenueCat doesn't support macOS

    override val cachedOfferings: PurchaseOfferings? = null

    override suspend fun initialize(apiKey: String) {
        // macOS platform - no initialization needed, pro access is auto-granted
        logger.debug { "PurchaseHelper: macOS platform - initialization skipped (pro access auto-granted)" }
    }

    override suspend fun getOfferings(
        onSuccess: (PurchaseOfferings) -> Unit,
        onError: (PurchaseError) -> Unit
    ) {
        logger.debug { "PurchaseHelper: macOS platform - getOfferings not supported (pro already granted)" }
        onError(MacosPurchaseError())
    }

    override suspend fun purchase(
        packageToPurchase: PurchasePackage,
        onSuccess: (PurchaseStoreTransaction, PurchaseCustomerInfo) -> Unit,
        onError: (PurchaseError, Boolean) -> Unit
    ) {
        logger.debug { "PurchaseHelper: macOS platform - purchase not needed (pro already granted)" }
        // Auto-complete purchase since pro is granted
        onSuccess(MacosPurchaseStoreTransaction(), proCustomerInfo)
    }

    override suspend fun restorePurchases(
        onSuccess: (PurchaseCustomerInfo) -> Unit,
        onError: (PurchaseError) -> Unit
    ) {
        logger.debug { "PurchaseHelper: macOS platform - restore returning pro access" }
        onSuccess(proCustomerInfo)
    }

    override suspend fun getCustomerInfo(
        forceRefresh: Boolean,
        onSuccess: (PurchaseCustomerInfo) -> Unit,
        onError: (PurchaseError) -> Unit
    ) {
        logger.debug { "PurchaseHelper: macOS platform - returning pro customer info (forceRefresh=$forceRefresh)" }
        onSuccess(proCustomerInfo)
    }

    override suspend fun hasActiveEntitlement(entitlementIdentifier: String): Boolean {
        logger.debug { "PurchaseHelper: macOS platform - hasActiveEntitlement returning true (pro granted)" }
        return true
    }

    override fun setPreferredLocale(locale: String) {
        logger.debug { "PurchaseHelper: macOS platform - setPreferredLocale not supported" }
    }

    override fun setFirebaseAppInstanceId(firebaseAppInstanceId: String) {
        logger.debug { "PurchaseHelper: macOS platform - setFirebaseAppInstanceId not supported" }
    }

    @Composable
    override fun Paywall(offeringIdentifier: String?, source: String, dismissRequest: () -> Unit) {
        logger.debug { "PurchaseHelper: macOS platform - Paywall not supported (source: $source, offering: $offeringIdentifier)" }
    }

    @Composable
    override fun CustomerCenter(modifier: Modifier, dismissRequest: () -> Unit) {
        // Not supported on macOS - CustomerCenterScreenDestination handles showing unavailable screen
    }
}
