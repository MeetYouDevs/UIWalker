package com.autotools.uiwalker.testcase

import android.support.test.InstrumentationRegistry
import android.util.Log
import com.autotools.uiwalker.data.TestData
import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.utils.UiUtils
import org.junit.Test
import java.io.File


/**
 * 用于单独遍历某个应用，通过代码配置被测应用信息
 */

class TravelByCmdArgs {


    @Test
    fun startTravel() {
        val configFile = "/sdcard/ui_walker.json"
        UiUtils.getInstance()
        val bundle = InstrumentationRegistry.getArguments()
        if (!File(configFile).exists()) {
            TestReport.e("TravelByCmdArgs.startTravel: $configFile not exist.")
            return
        }
        Log.e("test", bundle.getString("config_file", configFile))
        TestData.initDataByJson(configFile)
        TestData.showTestDataInfo()
        UiUtils.getInstance().travel()
    }
}
