package com.myetherwallet.mewwalletbl.data

import java.math.BigDecimal

enum class PurchaseProvider(val maxAmount: BigDecimal) {
    WYRE(BigDecimal.valueOf(250.0)), SIMPLEX(BigDecimal.valueOf(20000.0))
}