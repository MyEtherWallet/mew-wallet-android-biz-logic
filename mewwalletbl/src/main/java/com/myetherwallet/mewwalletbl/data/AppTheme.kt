package com.myetherwallet.mewwalletbl.data

import androidx.appcompat.app.AppCompatDelegate


enum class AppTheme(val themeMode: Int) {
    DEFAULT(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    DARK(AppCompatDelegate.MODE_NIGHT_YES)
}
