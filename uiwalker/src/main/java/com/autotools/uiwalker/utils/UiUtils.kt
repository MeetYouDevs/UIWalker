package com.autotools.uiwalker.utils

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.uiautomator.UiDevice
import com.autotools.uiwalker.data.*
import com.autotools.uiwalker.event.LoginEvent

import com.autotools.uiwalker.event.TravelEvent
import com.autotools.uiwalker.manager.ActManager
import com.autotools.uiwalker.manager.AppManager
import com.autotools.uiwalker.manager.TaskManager
import java.io.File



/**
 * 用于Ui的操作，如获取控件，页面对比等
 */

class UiUtils {

    companion object {

        lateinit var uiDevice: UiDevice
        lateinit var context: Context
        lateinit var travelEvent: TravelEvent
        var device_height: Int = 0
        var device_weight: Int = 0
        var isRun = true

        private var instance: UiUtils? = null

        fun getInstance(): UiUtils {
            if (instance == null) {
                instance = UiUtils()
                context = InstrumentationRegistry.getContext()
                uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                device_height = uiDevice.displayHeight
                device_weight = uiDevice.displayWidth
                uiDevice.waitForIdle(100)
                grantPermission()
                if (!File(PathData.root).exists()) {
                    File(PathData.root).mkdirs()
                }
                travelEvent = TravelEvent()
//                InputMethodUtils.installUTF7()

            }
            return instance!!
        }

        private fun grantPermission() {
            val list = ArrayList<String>()
            list.add("android.permission.WRITE_EXTERNAL_STORAGE")
            list.add("android.permission.READ_EXTERNAL_STORAGE")
            list.add("android.permission.MOUNT_UNMOUNT_FILESYSTEMS")
            list.add("android.permission.INSTALL_PACKAGES")
            list.add("android.permission.DUMP")
            list.add("android.permission.INJECT_EVENTS")
            list.add("android.permission.PACKAGE_USAGE_STATS")
            list.add("android.permission.GET_TASKS")
            //设置FLAG
            for (p in list) {
                val cmd = "pm grant " + CommonArgs.travelLib_pkg + " " + p
                CMDUtils.runCmdByDevice(cmd, true)
            }
        }




    }


    /**
     * 结束遍历
     */
    fun stopTravel() {
        isRun = false
        TaskManager.stopTask(TaskManager.TaskType.All)
        //恢复FLAG

    }


    /**
     * 遍历目标应用
     */
    fun travel() {
        //开启测试时长监听
        TaskManager.startTask(TaskManager.TaskType.All)
        //打开被测应用
        AppManager.openApp(TestData.testPkg, TestData.act)
        var currentAct: String
        var currentPkg: String
        //isRun依据为时间
        while (isRun) {
            //获取当前界面信息
            val currentInfo :CurrentView = ActManager.currentInfo
            currentAct = currentInfo.act
            currentPkg = currentInfo.pkg
            //过滤webView
            if (currentAct.toLowerCase().indexOf("WebViewActivity".toLowerCase())> -1) {
                uiDevice.pressBack()
                TestReport.i("UiUtils.travel: 【WebView】当前activity为WebViewActivity，返回上级界面，不响应本轮操作！")
                continue
            }
            //act黑名单
            if (TestData!!.blackActivitys.contains(currentAct)) {
                uiDevice.pressBack()
                TestReport.i("UiUtils.travel: 【Activity黑名单】$currentAct，返回上级界面，不响应本轮操作！")
                continue
            }
            //根据isNeedLoginc参数先判断是否需要登录
            if (TestData.isNeedLogin) {
                if (TestData.loginActivity == currentAct) {
                    LoginEvent.checkLogin()
                }
            }
            //再次检测界面是否发生变化，如果页面没发生变化，则遍历当前页面
            if (ActManager.currentActName != currentAct) {
                TestReport.i("UiUtils.travel: 【异常变化】控件未被响应前，activity发送了变化，不响应本轮操作！")
                continue
            }
            //获取界面模型
            val activityModel = ActManager.getActivityModelByName(currentPkg,currentAct)
            //将界面模型复制给当前activity
            travelEvent.setTravelAct(activityModel)
            //控件遍历
            travelEvent.trigger()
        }
        TestReport.i("UiUtils_travel: 遍历结束")
        ReportUtils.getReportList()

    }

}
