package com.myetherwallet.mewwalletbl.data

import android.content.Context
import android.content.res.Configuration
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.*

/**
 * Created by BArtWell on 19.05.2021.
 */
class AppCurrencyTest {

    private lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun testInRussian() {
        setAppLocale(instrumentationContext, AppLanguage.RUSSIAN)
        Assert.assertEquals("1,00 $", AppCurrency.USD.format(BigDecimal.ONE))
        Assert.assertEquals("1,00 €", AppCurrency.EUR.format(BigDecimal.ONE))
        Assert.assertEquals("1,00 ₽", AppCurrency.RUB.format(BigDecimal.ONE))
    }

    @Test
    fun testInEnglish() {
        setAppLocale(instrumentationContext, AppLanguage.ENGLISH)
        Assert.assertEquals("$1.00", AppCurrency.USD.format(BigDecimal.ONE))
        Assert.assertEquals("€1.00", AppCurrency.EUR.format(BigDecimal.ONE))
        Assert.assertEquals("RUB 1.00", AppCurrency.RUB.format(BigDecimal.ONE))
    }

    private fun setAppLocale(context: Context, language: AppLanguage) {
        val locale = Locale(language.code)
        val config = Configuration(context.resources.configuration)
        Locale.setDefault(locale)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
