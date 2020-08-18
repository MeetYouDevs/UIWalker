package com.autotools.uiwalker.event

import android.support.test.runner.AndroidJUnit4
import com.autotools.uiwalker.utils.UiUtils
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaseEventsTest {

    @Test
    fun test01() {
        UiUtils.getInstance()
//        BaseEvents.click(500, 1720)
        BaseEvents.inputText(300, 150, "test")
//        BaseEvents.longClick(300, 900)
//        BaseEvents.swipe(300.0f, 600.0f, 600.0f, 600.0f)
    }
}