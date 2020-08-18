package com.autotools.uiwalker.utils

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ReportUtilsTest {

    @Before
    fun before(){
        UiUtils.getInstance()
    }

    @Test
    fun getReportList() {
        ReportUtils.getReportList()
    }
}