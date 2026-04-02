# Purchases Module

A Kotlin Multiplatform abstraction layer for RevenueCat that enables in-app purchase functionality across all supported platforms, including those where RevenueCat doesn't natively compile.

## Why This Module Exists

### The Problem

[RevenueCat's Kotlin Multiplatform SDK](https://www.revenuecat.com/docs/getting-started/installation/kotlin-multiplatform) only supports **Android** and **iOS**. If your KMP project targets additional platforms like JVM, JS, WasmJS, or macOS, the project will **fail to compile** when RevenueCat is used in common code.

### The Solution

This module provides a platform-agnostic abstraction layer that:

1. **Wraps RevenueCat** on supported platforms (Android/iOS) with full functionality
2. **Excludes RevenueCat dependencies** from unsupported platforms at build time
3. **Provides no-op implementations** for unsupported platforms, allowing the code to compile and run everywhere
4. **Hides RevenueCat types** from common code, keeping business logic platform-independent

## Targets

| Platform | Behavior |
|----------|----------|
| **Android** | Full RevenueCat integration |
| **iOS** (arm64, x64, simulatorArm64) | Full RevenueCat integration |
| **JVM** | No-op (compiles, returns errors/false) |
| **JS** (browser) | No-op |
| **WasmJS** (browser) | No-op |
| **macOS** (arm64) | No-op |

iOS and macOS also produce a static XCFramework (`purchasesKit`).

## Quick Start

### 1. Add Dependency

```kotlin
commonMain.dependencies {
    implementation(project(":purchases"))
}
```

### 2. Include in DI

```kotlin
val appModule = module {
    includes(platformPurchaseModule)
}
```

### 3. Use in Code

```kotlin
class YourViewModel(
    private val purchaseStateManager: PurchaseStateManager,
    private val purchaseHelper: PurchaseHelper
) {
    val isPro = purchaseStateManager.isPro

    suspend fun purchase(pkg: PurchasePackage) {
        purchaseHelper.purchase(
            packageToPurchase = pkg,
            onSuccess = { transaction, customerInfo ->
                purchaseStateManager.updateFromCustomerInfo(customerInfo)
            },
            onError = { error, cancelled ->
                purchaseStateManager.emitEvent(
                    if (cancelled) PurchaseEvent.PurchaseCancelled
                    else PurchaseEvent.Error(error)
                )
            }
        )
    }
}
```

## API Overview

### Core Components

| Component | Description |
|-----------|-------------|
| `PurchaseHelper` | Interface for purchase operations: initialize, getOfferings, purchase, restore, getCustomerInfo, hasActiveEntitlement. Also provides `@Composable` Paywall and CustomerCenter UI. |
| `PurchaseStateManager` | Manages `isPro` state (`StateFlow<Boolean>`) and emits `PurchaseEvent` via `SharedFlow`. |
| `PaywallListener` | Handles RevenueCat paywall callbacks (Android/iOS). |
| `CustomerCenterListener` | Handles RevenueCat customer center callbacks (Android). |

### Abstract Types

All types are interfaces defined in `commonMain` — platform source sets provide implementations that wrap RevenueCat types on Android/iOS or return stub values elsewhere.

| Type | Key Fields |
|------|------------|
| `PurchaseCustomerInfo` | `entitlements`, `activeSubscriptions`, `managementURL` |
| `PurchaseOfferings` | `current`, `all` |
| `PurchaseOffering` | `identifier`, `availablePackages`, `monthly`, `annual` |
| `PurchasePackage` | `localizedPriceString`, `hasFreeTrial`, `freeTrialDays`, `hasIntroductoryOffer`, `discountPercentage` |
| `PurchaseError` | `message`, `code` (see `PurchaseErrorCode` constants) |
| `PurchaseStoreTransaction` | `transactionIdentifier`, `productIdentifier`, `purchaseDate` |

### Dependency Injection

Each platform provides a Koin module via `expect/actual`:

```kotlin
// commonMain
expect val platformPurchaseModule: Module

// androidMain / iosMain / jvmMain / webMain / macosMain
actual val platformPurchaseModule: Module = module { ... }
```

## Architecture

```
purchases/src/
├── commonMain/     # Interfaces: PurchaseHelper, PurchaseTypes, PurchaseStateManager, NetworkConnectivity
├── androidMain/    # RevenueCat Android implementation + PaywallListener + CustomerCenterListener
├── iosMain/        # RevenueCat iOS implementation + PaywallListener
├── jvmMain/        # No-op implementation
├── webMain/        # No-op implementation (shared by JS + WasmJS)
└── macosMain/      # No-op implementation
```

## Build Configuration

RevenueCat is excluded from unsupported platform configurations via `afterEvaluate` in `build.gradle.kts`. Amazon Appstore SDK is also excluded globally (Google Play only).

## RevenueCat Setup

This module handles the KMP integration, but you still need platform-specific RevenueCat setup. Refer to the [official RevenueCat KMP documentation](https://www.revenuecat.com/docs/getting-started/installation/kotlin-multiplatform) for account setup, product configuration, and native SDK integration.

## License

Free to use, copy, or fork. This is a helper library for RevenueCat's KMP SDK — no additional charges beyond RevenueCat's own pricing.
