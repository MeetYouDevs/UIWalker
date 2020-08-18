package com.autotools.uiwalker.listener

import com.autotools.uiwalker.data.TestData
import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.utils.UiUtils


import java.util.Timer
import java.util.TimerTask


object TestTimeListener {
    internal var PERIOD = (1000 * 10).toLong()
    internal var DELAY = (1000 * 20).toLong()
    internal var timer: Timer? = null
    internal var startTime: Long = 0

    fun startWatching() {
        if (timer == null) {
            TestReport.i("TestTimeListener_startWatching: 已开启【测试时长】监听。")
            timer = Timer()
            startTime = System.currentTimeMillis()
            val task = TestTimerTask()
            timer!!.schedule(task, DELAY, PERIOD)
        } else {
            TestReport.i("TestTimeListener_startWatching: 【测试时长】监听已开启，无需重复开启！")
        }
    }

    fun stopWatching() {
        if (timer != null) {
            TestReport.i("TestTimeListener_startWatching: 已终止【测试时长】监听。")
            timer!!.cancel()
        } else {
            TestReport.i("TestTimeListener_startWatching: 【测试时长】监听已终止，无需重复终止！")
        }
    }

    internal class TestTimerTask : TimerTask() {

        override fun run() {
            if (System.currentTimeMillis() - startTime > TestData.runTime_ms) {
                TestReport.i("TestTimerTask_run: 【测试时长】已达" + TestData.getRunTime_min() + "分钟，测试终止。")
                UiUtils.getInstance().stopTravel()
            }
        }
    }


}


