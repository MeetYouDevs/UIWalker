package com.autotools.uiwalker.listener

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.event.SystemEvent
import com.autotools.uiwalker.utils.UiUtils
import com.autotools.uiwalker.utils.UiUtils.Companion.context


/**
 * 屏幕监听
 */

object ScreenListener {
    private var isRun = false
    private val sysEvent = SystemEvent()
    //屏幕监听
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (Intent.ACTION_SCREEN_OFF == intent.action) {
                TestReport.i("SelfTest_onReceive: 灭屏了")
//                sysEvent.unlockWithScreenOff()
            } else if (Intent.ACTION_SCREEN_ON == intent.action) {
                TestReport.i("SelfTest_onReceive: 亮屏了")
                if (isScreenLocked) {
//                    sysEvent.unlockWithScreenOn()
                }
            }
        }
    }

    val isScreenLocked: Boolean
        get() {
            val km = UiUtils.context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            return km.inKeyguardRestrictedInputMode()
        }

    fun startWatching() {
        if (!isRun) {
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            filter.addAction(Intent.ACTION_SCREEN_ON)
            context.registerReceiver(receiver, filter)
            TestReport.i("ScreenListener_startWatching: 开启【屏幕监听】")
            isRun = true
        } else {
            TestReport.i("ScreenListener_startWatching: 已开启【屏幕监听】，无需重复开启")
        }
    }

    fun stopWatching() {
        if (isRun) {
            context.unregisterReceiver(receiver)
            TestReport.i("ScreenListener_stopWatching: 关闭【屏幕监听】")
            isRun = false
        } else {
            TestReport.i("ScreenListener_stopWatching: 已关闭【屏幕监听】，无需重复关闭")
        }
    }

}
