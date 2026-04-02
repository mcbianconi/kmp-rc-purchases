package com.bearminds.purchases

class PurchaseCustomerInfoWrapper(
    grantProAccess: Boolean = true
) : PurchaseCustomerInfo {
    override val entitlements: Map<String, PurchaseEntitlementInfo> = if (grantProAccess) {
        ProEntitlementMap()
    } else {
        emptyMap()
    }
    override val activeSubscriptions: Set<String> = if (grantProAccess) setOf("web_pro") else emptySet()
    override val allPurchasedProductIdentifiers: Set<String> = if (grantProAccess) setOf("web_pro") else emptySet()
    override val nonSubscriptionTransactions: List<PurchaseStoreTransaction> = emptyList()
    override val latestExpirationDate: Long? = null
    override val requestDate: Long = 0L
    override val firstSeen: Long = 0L
    override val originalAppUserId: String = "web_user"
    override val managementURL: String? = null
    override val originalApplicationVersion: String? = null
    override val originalPurchaseDate: Long? = null
}

private class ProEntitlementMap : Map<String, PurchaseEntitlementInfo> {
    private val activeEntitlement = ProPurchaseEntitlementInfo()

    override val entries: Set<Map.Entry<String, PurchaseEntitlementInfo>> = emptySet()
    override val keys: Set<String> = emptySet()
    override val size: Int = 1
    override val values: Collection<PurchaseEntitlementInfo> = listOf(activeEntitlement)

    override fun isEmpty(): Boolean = false
    override fun get(key: String): PurchaseEntitlementInfo = activeEntitlement
    override fun containsValue(value: PurchaseEntitlementInfo): Boolean = value == activeEntitlement
    override fun containsKey(key: String): Boolean = true
}

class ProPurchaseEntitlementInfo : PurchaseEntitlementInfo {
    override val identifier: String = "web_pro"
    override val isActive: Boolean = true
}

class StubPurchaseError : PurchaseError {
    override val message: String = "Purchases not supported on web"
    override val code: Int = -1
}

class StubPurchaseStoreTransaction : PurchaseStoreTransaction {
    override val transactionIdentifier: String = ""
    override val productIdentifier: String = ""
    override val purchaseDate: Long = 0L
}
