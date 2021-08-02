package com.myetherwallet.mewwalletbl.data

/**
 * Created by BArtWell on 17.06.2020.
 */

enum class AppLanguage(val english: String, val native: String?, val code: String, val region: String) {
    ENGLISH("English", null, "en", "US"),
    RUSSIAN("Russian", "Русский", "ru", "RU")
}
