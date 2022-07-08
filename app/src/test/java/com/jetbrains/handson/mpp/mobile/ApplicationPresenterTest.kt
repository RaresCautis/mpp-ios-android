package com.jetbrains.handson.mpp.mobile

import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApplicationPresenterTest {

    companion object {
        lateinit var presenter: ApplicationPresenter

        @BeforeClass
        @JvmStatic
        fun setup() {
            presenter = ApplicationPresenter()
        }
    }

    @Test
    fun generalPennyConversion_isCorrect() {
        val pennies = 1357
        val pounds = presenter.pennyConversion(pennies)

        assertEquals("£13.57", pounds)
    }

    @Test
    fun pennyConversion_roundsTo2DP() {
        var pennies = 1350
        var pounds = presenter.pennyConversion(pennies)

        assertEquals("£13.50", pounds)

        pennies = 1300
        pounds = presenter.pennyConversion(pennies)

        assertEquals("£13.00", pounds)
    }

    @Test
    fun pennyConversion_oneP() {
        val pennies = 1
        val pounds = presenter.pennyConversion(pennies)

        assertEquals("£0.01", pounds)
    }

    @Test
    fun pennyConversion_zero() {
        val pennies = 0
        val pounds = presenter.pennyConversion(pennies)

        assertEquals("£0.00", pounds)
    }
}
