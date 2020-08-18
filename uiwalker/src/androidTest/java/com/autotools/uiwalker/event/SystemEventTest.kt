package com.autotools.uiwalker.event

import com.autotools.uiwalker.utils.UiUtils
import org.junit.Test

import org.junit.Assert.*

class SystemEventTest {
    var uiUtils = UiUtils.getInstance()
    var event: SystemEvent = SystemEvent()

    @Test
    fun longPressMenu_Event() {
        event.longPressMenu_Event()
    }

    @Test
    fun pressMenu_Event() {
        event.pressMenu_Event()
    }

    @Test
    fun pressHome_Event() {
        event.pressHome_Event()
    }

    @Test
    fun pressBack_Event() {
        event.pressBack_Event()
    }


    @Test
    fun click_Event() {
        event.random_click_Event()
    }

    @Test
    fun swipeFromRight_Event() {
        event.swipeFromRight_Event()
    }

    @Test
    fun swipe_Event() {
        event.random_swipe_Event()
    }

    @Test
    fun injectRandom() {
    }

    @Test
    fun trigger() {
    }

    @Test
    fun drag_Event() {
    }


}