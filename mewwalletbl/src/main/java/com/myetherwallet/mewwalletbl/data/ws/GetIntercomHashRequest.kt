package com.myetherwallet.mewwalletbl.data.ws

/**
 * Created by BArtWell on 13.09.2021.
 */

data class GetIntercomHashRequest(
    val id: String,
    val iso: String
) {

    val platform = "android"
}
