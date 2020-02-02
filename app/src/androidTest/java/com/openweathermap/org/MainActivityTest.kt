package com.synchronoss.currentweather

import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.openweathermap.org.MainActivity
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsNot.not

import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class MainActivityTest {

    private var device : UiDevice? = null

    @get:Rule
    var mainActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        this.device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun testAllowLocationPermission() {
        val allowButton = this.device?.findObject(UiSelector().text("ALLOW"))
        var permissionAllowedMessage = this.device?.findObject(UiSelector().text("Permission allowed"))
        allowButton!!.click()
        assert(permissionAllowedMessage!!.exists())
    }

    @Test
    fun testDenyLocationPermission() {
        val denyButton = this.device?.findObject(UiSelector().text("DENY"))
        val permissionDeniedMessage = this.device?.findObject(UiSelector().text("Permission denied"))
        denyButton!!.click()
        assert(permissionDeniedMessage!!.exists())
    }

    @Test
    fun testLocationLabel() {
        //onView(allOf(withId(citiesField), not(withText("Unwanted"))))
    }


    @After
    fun tearDown() {
        /*InstrumentationRegistry.getInstrumentation().uiAutomation.revokeRuntimePermission(
            InstrumentationRegistry.getInstrumentation().targetContext.packageName,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        InstrumentationRegistry.getInstrumentation().uiAutomation.revokeRuntimePermission(
            InstrumentationRegistry.getInstrumentation().targetContext.packageName,
            Manifest.permission.ACCESS_FINE_LOCATION)*/
    }
}