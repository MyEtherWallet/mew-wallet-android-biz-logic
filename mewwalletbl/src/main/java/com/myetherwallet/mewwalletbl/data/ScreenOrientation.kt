package com.myetherwallet.mewwalletbl.data

import android.content.pm.ActivityInfo

/**
 * Created by BArtWell on 24.02.2022.
 */

enum class ScreenOrientation(internal val value: Int) {
    AUTO(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR),
    PORTRAIT(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
}
