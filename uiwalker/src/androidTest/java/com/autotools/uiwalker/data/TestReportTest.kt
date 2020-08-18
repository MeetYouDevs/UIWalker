package com.autotools.uiwalker.data

import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestReportTest {
    @Test
    fun testI() {
        TestReport.i("uiwalker_test", "test")
        TestReport.i("testI")
    }
    @Test
    fun testF() {
        TestReport.f("uiwalker_test", "test")
    }
    @Test
    fun testE() {
        TestReport.e("uiwalker_test", "testE")
        TestReport.e("testE")
    }

}