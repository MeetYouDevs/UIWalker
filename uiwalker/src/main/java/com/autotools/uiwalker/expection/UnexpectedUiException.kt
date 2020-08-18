package com.autotools.uiwalker.expection

import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.utils.UiUtils


/**
 * 用于处理一些特殊的情况，比如动态界面造成无法dump等情况
 */

class UnexpectedUiException(internal var from: String, internal var description: String, internal var e: Exception, internal var showInLog: Boolean) : Exception() {

    fun report() {
        if (showInLog) {
            e.printStackTrace()
        }
        TestReport.e(from + " ------ " + description + ":" + e.message)
    }

    /**
     * 对于特殊情况获取不到界面的，进行随机的操作
     */
    fun adjust() {
        TestReport.i("UnexpectedUiException_adjust: 随机点击，直至退出动态界面")
        UiUtils.travelEvent.triggerOnSystem()
    }
}
