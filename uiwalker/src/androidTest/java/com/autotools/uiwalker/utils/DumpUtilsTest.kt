package com.autotools.uiwalker.utils

import com.autotools.uiwalker.data.TestReport
import org.junit.Before
import org.junit.Test
import java.io.File
import android.content.Intent
import android.provider.Settings
import android.support.test.InstrumentationRegistry


class DumpUtilsTest {
    @Before
    fun before() {
        UiUtils.getInstance()
    }

    @Test
    fun dumpUiXml() {
        val f = DumpUtils.dumpUiXml()
        assert(f.totalSpace > 0)
    }

    @Test
    fun dumpUiXml1() {
        val f = File("/sdcard/1.xml")
        DumpUtils.dumpUiXml(f, true)
        assert(f.totalSpace > 0)
    }

}