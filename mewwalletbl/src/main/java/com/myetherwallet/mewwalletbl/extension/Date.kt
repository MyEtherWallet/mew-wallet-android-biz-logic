package com.myetherwallet.mewwalletbl.extension

import android.text.format.DateFormat
import java.util.*

/**
 * Created by BArtWell on 23.09.2020.
 */

fun Date.toDateTimeZone() = DateFormat.format("dd-MM-yyyy hh:mm:ss z", this)
