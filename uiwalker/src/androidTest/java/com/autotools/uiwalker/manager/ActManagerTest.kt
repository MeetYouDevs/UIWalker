package com.autotools.uiwalker.manager

import com.autotools.uiwalker.utils.UiUtils
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ActManagerTest {
    @Before
    fun before(){
        UiUtils.getInstance()
    }

    @Test
    fun getCurrentActName() {
        val currentAct=ActManager.currentActName
        val currentPkg=ActManager.currentPkg
        val act=ActManager.getActivityModelByName(currentPkg,currentAct)
        assert(act.pkgName.isNotEmpty())
    }

    @Test
    fun getCurrentUiList() {
    }

    @Test
    fun getActivityModelByName() {
    }

    @Test
    fun isNewActEntry() {
    }

    @Test
    fun isNewViewEntry() {
    }
}