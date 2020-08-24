package com.myetherwallet.mewwalletbl.data.wyre

import java.math.BigDecimal

class WyreRequestParams(
    val amount: BigDecimal,
    val sourceCurrency: String,
    val destCurrency: String,
    val referrerAccountId: String,
    val dest: String,
    val redirectUrl: String
)