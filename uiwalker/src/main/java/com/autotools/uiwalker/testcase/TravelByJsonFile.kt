package com.autotools.uiwalker.testcase

import android.net.TrafficStats
import com.autotools.uiwalker.data.TestData
import com.autotools.uiwalker.utils.UiUtils

import android.support.test.InstrumentationRegistry
import org.junit.Before
import org.junit.Test

import android.os.Bundle
import com.autotools.uiwalker.data.CommonArgs
import com.autotools.uiwalker.data.PathData
import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.utils.ReportUtils


/**
 * 用于单独遍历某个应用，通过代码配置被测应用信息
 */

class TravelByJsonFile {

    @Before
    fun initTestData() {
        UiUtils.getInstance()
        val bundle: Bundle = InstrumentationRegistry.getArguments()
        val file=bundle.getString("config_file", PathData.configPath)
        TestReport.e("TravelByJsonFile.initTestData: $file")
        TestData.initDataByJson(file)

    }

    @Test
    fun startTravel() {
//        UiUtils.getInstance().travel()
    }
}
