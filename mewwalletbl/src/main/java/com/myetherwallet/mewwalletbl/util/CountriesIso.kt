package com.myetherwallet.mewwalletbl.util

import android.content.Context

private const val PIYASA = "piyasa"
private const val BINANCE = "binance"

enum class CountriesIso(val iso: String, private val usingFor: String) {
    TURKEY("tr", PIYASA),
    AZERBAIJAN("az", PIYASA),
    KAZAKHSTAN("kz", PIYASA),
    KYRGYZSTAN("kg", PIYASA),
    GEORGIA("ge", PIYASA),
    CYPRUS("cy", PIYASA),

    USA("us", BINANCE),
    Albania("al", BINANCE),
    BosniaAndHerzegovina("ba", BINANCE),
    Belarus("by", BINANCE),
    Congo("cg", BINANCE),
    DemocraticRepublicOfTheCongo("cd", BINANCE),
    CotedIvoire("ci", BINANCE),
    Cuba("cu", BINANCE),
    Iraq("iq", BINANCE),
    Iran("ir", BINANCE),
    NorthKorea("kp", BINANCE),
    Liberia("lr", BINANCE),
    Macedonia("mk", BINANCE),
    Myanmar("mm", BINANCE),
    Serbia("rs", BINANCE),
    Sudan("sd", BINANCE),
    SouthSudan("ss", BINANCE),
    Syria("sy", BINANCE),
    Zimbabwe("zw", BINANCE);

    companion object {

        fun isTurkeyOrNeighbors(context: Context) =
            values()
                .find {
                    it.usingFor == PIYASA && it.iso.equals(ApplicationUtils.getCountryIso(context), true)
                } != null

        fun isAvailableForBinance(context: Context) =
            values()
                .find {
                    it.usingFor == BINANCE && it.iso.equals(ApplicationUtils.getCountryIso(context), true)
                } == null
    }
}
