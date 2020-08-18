package com.autotools.uiwalker.listener

import android.support.test.uiautomator.UiWatcher
import com.autotools.uiwalker.data.TestData
import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.listener.TestTimeListener.timer
import com.autotools.uiwalker.manager.ActManager
import com.autotools.uiwalker.manager.AppManager
import com.autotools.uiwalker.utils.UiUtils.Companion.uiDevice


/**
 * 用于一些异常UI的监听
 */

object UnexpectedUiListener {

    private var myWatcher: MyUiWatcher? = null
    //避免重复调用

    fun startWatching() {
        if (myWatcher == null) {
            TestReport.i("UnexpectedUiListener_startWatching: 已开启【异常UI】监听。")
            myWatcher = MyUiWatcher()
            uiDevice.registerWatcher(MyUiWatcher::class.java.name, myWatcher)
        } else {
            TestReport.i("UnexpectedUiListener_startWatching: 【异常UI】监听已开启，无需重复开启！")
        }
    }

    fun stopWatching() {
        if (timer != null) {
            TestReport.i("UnexpectedUiListener_stopWatching: 已终止【异常UI】监听。")
            uiDevice.removeWatcher(MyUiWatcher::class.java.name)
        } else {
            TestReport.i("UnexpectedUiListener_stopWatching: 【异常UI】监听已终止，无需重复终止！")
        }
    }

    internal class MyUiWatcher : UiWatcher {
        override fun checkForCondition(): Boolean {
            if (AppManager.isOpeningApp) {
                TestReport.i("MyUiWatcher_checkForCondition: 正在打开app,避免重复打开。")
                return false
            }
            if (!AppManager.isInWhiteApps) {
                TestReport.i("MyUiWatcher_checkForCondition: 当前不在白名单应用中，退出重新进入应用。")
                AppManager.reopenApp(TestData.testPkg!!)
                //等待应用打开
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                return true
            }
            return false
        }
    }


}


