package com.autotools.uiwalker.manager


import android.content.pm.PackageManager
import android.support.test.uiautomator.By

import android.support.test.uiautomator.UiSelector
import android.text.TextUtils
import com.autotools.uiwalker.data.CommonArgs
import com.autotools.uiwalker.data.TestData
import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.event.SystemEvent
import com.autotools.uiwalker.utils.CMDUtils
import com.autotools.uiwalker.utils.DumpUtils
import com.autotools.uiwalker.utils.UiUtils
import com.autotools.uiwalker.utils.UiUtils.Companion.context
import com.autotools.uiwalker.utils.UiUtils.Companion.uiDevice
import java.io.File


import java.util.ArrayList


/**
 * 用于管理app的启动终止等
 */

object AppManager {
    //用于保证打开app这个操作不会被重复调用
    var isOpeningApp = false

    /**
     * 判断当前是否在合法的应用中
     *
     * @return true if it is in white apps
     */
    // 排除被测应用和android系统包
    val isInWhiteApps: Boolean
        get() {
            val whitePkgList = TestData.whiteApps
            val currentPkg = DumpUtils.dumpCurrentPkg()
            if (currentPkg == TestData.testPkg)
                return true
            return whitePkgList.contains(currentPkg)
        }

    /**
     * 获取当前travelLib的版本
     *
     * @return
     */
    val versionName: String
        get() {
            val command = "dumpsys package " + CommonArgs.travelLib_apk_pkg + " | grep versionName"
            val rs = CMDUtils.runCmdByDevice(command, false)

            var versionName = "versionName_unknow"
            if (!TextUtils.isEmpty(rs)) {
                val strs = rs.split("\n".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                for (i in strs.indices) {
                    if (strs[i].contains("versionName")) {
                        versionName = strs[i]
                        break
                    }

                }
                val start = versionName.indexOf("=") + 1
                versionName = versionName.substring(start)
            }
            return versionName
        }

    /**
     * 启动特定的界面
     *
     * @param pkgName 包名
     */
    fun openApp(pkgName: String, act: String) {
        TestReport.i("AppManager_launchApp: 打开应用$pkgName")
        isOpeningApp = true
        try {
            val systemEvent = SystemEvent()
            systemEvent.pressHome_Event()
            systemEvent.pressHome_Event()
            handleSystemWindow(ActManager.currentInfo.act)
            launchAppByCmd(pkgName, act)
            handleSystemWindow(ActManager.currentInfo.act)
            Thread.sleep(5000)
        } catch (e: Exception) {
            e.printStackTrace()
            TestReport.i("AppManager_openApp: 打开app出现异常，" + e.message)
        }

        isOpeningApp = false
    }

    /**
     * 重新启动特定的界面
     *
     * @param pkgName 包名
     */
    fun reopenApp(pkgName: String) {
        TestReport.i("AppManager_reopenApp: 重新打开应用$pkgName")
        isOpeningApp = true
        try {
            val systemEvent = SystemEvent()
            systemEvent.pressHome_Event()
            systemEvent.pressHome_Event()
            launchAppByCmd(pkgName, TestData.act)
            handleSystemWindow(ActManager.currentInfo.act)
            Thread.sleep(5000)
        } catch (e: Exception) {
            e.printStackTrace()
            TestReport.i("AppManager_reopenApp: 打开app出现异常，" + e.message)
        }

        isOpeningApp = false
    }

    /**
     * 判断应用是否有instrument
     */
    fun checkInstrumentation(): Boolean {
        val myJacoco = TestData.testPkg + "/.jacoco.JacocoInstrumentation"
        val cm = "pm list instrumentation"
        val rs = CMDUtils.runCmdByDevice(cm, true)
        return rs.contains(myJacoco)
    }

    /**
     * 以instrument启动应用
     */
    private fun launchAppByInstument(pkgName: String, act: String) {
        val commandString = ("am start $pkgName/$act")
        CMDUtils.runCmdByDevice(commandString, true)
    }

    private fun launchAppByCmd(pkgName: String, act: String) {
        val commandString = ("am start $pkgName/$act")
        CMDUtils.runCmdByDevice(commandString, true)
    }


    /**
     * 通过startActivity的方式启动应用
     *
     * @param packageName 启动的包名
     */
    fun launchAppByIntent(packageName: String) {
//                Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
//                String act = packageName + "/" + intent.getComponent().getClassName();
//                String cmd = "am start -n " + act;
//                CMDUtils.runCmdByDevice(cmd, false);
//
//                Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                context.startActivity(intent);


    }

    fun getAppName(packageName: String): String {
        var appName: String
        val packageManager = UiUtils.context.getApplicationContext().getPackageManager()
        try {
            appName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)) as String
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            appName = "unknown"
        }

        return appName
    }

    /**
     * 通过startActivity的方式启动应用
     */
    @Throws(Exception::class)
    fun clickAppIcon(appName: String, pkgName: String) {
        val systemEvent = SystemEvent()
        systemEvent.pressBack_Event()
        systemEvent.pressBack_Event()
        systemEvent.pressBack_Event()
        systemEvent.pressHome_Event()
        systemEvent.pressHome_Event()
        systemEvent.pressHome_Event()
        while (UiUtils.isRun) {
            //点击掉可能出现的anr弹窗
            handleSystemWindow(ActManager.currentInfo.act)
            var icon = uiDevice.findObject(UiSelector().text(appName))
            if (icon.exists()) {
                icon.click()
                if (DumpUtils.getCurrentActByActivityRecord().pkg != pkgName ) {
                    launchAppByInstument(pkgName, TestData.act)
                }
                handleSystemWindow(ActManager.currentInfo.act)
                return
            } else {
                //如果当前桌面不存在icon，则尝试查找文件夹
                val dirs = uiDevice.findObjects(By.descContains("文件夹"))
                if (dirs != null && dirs.size > 0) {
                    for (dirsInstance in dirs) {
                        dirsInstance.click()
                        Thread.sleep(1000)
                        icon = uiDevice.findObject(UiSelector().text(appName))
                        if (icon.exists()) {
                            icon.click()
                            handleSystemWindow(ActManager.currentInfo.act)
                            return
                        } else {
                            systemEvent.pressBack_Event()
                        }
                    }
                }
                Thread.sleep(1000)
                //如果文件夹仍然没有，则向右滑动
                systemEvent.swipeFromRight_Event()
                Thread.sleep(1000)
            }

        }
    }

    /**
     * 停止某个进程
     *
     * @param progressName 如Uiautomator
     */
    fun stopProgress(progressName: String) {
        val temp = CMDUtils.runCmdByDevice("ps", false).split("\n")
        var targetLine = ""
        for (str in temp) {
            if (!TextUtils.isEmpty(str) && str.contains(progressName)) {
                targetLine = str
            }
        }
        val pid: String
        val list = ArrayList<String>()
        if (!TextUtils.isEmpty(targetLine)) {
            val xxStrings = targetLine.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (xxString in xxStrings) {
                if (xxString.trim { it <= ' ' }.length > 0) {
                    list.add(xxString)
                }
            }
            pid = list[1]
            CMDUtils.runCmdByDevice("kill $pid", false)
        }
    }

    /**
     * 安装apk
     *
     * @param apkFile apk文件在本机的路径
     */
    fun installApk(apkFile: File) {
        val installCommand = "pm install -r " + apkFile.absolutePath
        val result = CMDUtils.runCmdByDevice(installCommand, true)
        if ("Success" in result) {
            TestReport.i("成功安装" + apkFile.name + "！")
        } else {
            TestReport.e("安装" + apkFile.name + "时出错！")
        }
    }

    /**
     * 卸载apk
     */
    fun uninstallApk(pkgName: String) {
        val installCommand = "pm uninstall $pkgName"
        val result = CMDUtils.runCmdByDevice(installCommand, false)
        if ("Success" in result) {
            TestReport.i("成功卸载$pkgName！")
        } else {
            TestReport.e("卸载" + pkgName + "时出错！")
        }
    }

    /**
     * 判断某个应用是否已经安装
     *
     * @param packageName 应用包名
     */
    fun isAppInstalled(packageName: String): Boolean {
        val packageManager = context.getPackageManager()
        // 获取所有已安装程序的包信息
        val pinfo = packageManager.getInstalledPackages(0)
        for (i in pinfo.indices) {
            if (pinfo.get(i).packageName.equals(packageName, ignoreCase = true))
                return true
        }
        return false
    }

    /**
     * 处理系统及弹窗
     * @param activity 当前activity
     */
    fun handleSystemWindow(activity: String) : Boolean{
        TestReport.i("AppManager_handleSystemWindow:进入方法，当前activity：$activity")
        if (activity == "com.android.packageinstaller.permission.ui.GrantPermissionsActivity") {
            TestReport.i("AppManager_handleSystemWindow:系统级权限弹窗事件")
            try {
                val x = uiDevice.findObject(By.text("确定"))
                if (x != null) {
                    x.click()
                    TestReport.i("AppManager_handleSystemWindow:系统级权限弹窗事件，点击确定")
                    return true
                }
                val y = uiDevice.findObject(By.text("允许"))
                if (y != null) {
                    y.click()
                    TestReport.i("AppManager_handleSystemWindow:系统级权限弹窗事件，点击允许")
                    return true
                }
                val z = uiDevice.findObject(By.text("始终允许"))
                if (z != null) {
                    z.click()
                    TestReport.i("AppManager_handleSystemWindow:系统级权限弹窗事件，点击始终允许")
                    return true
                }
                val o = uiDevice.findObject(By.text("同意"))
                if (o != null) {
                    o.click()
                    TestReport.i("AppManager_handleSystemWindow:系统级权限弹窗事件，点击同意")
                    return true
                }
            } catch (e : Exception) {
                TestReport.e("AppManager_handleSystemWindow:重复点击")
                e.printStackTrace()
            }

        }
        try {
            val anr = uiDevice.findObject(By.text("美柚没有响应")) //米8手机
            if (anr != null) {
                val x = uiDevice.findObject(By.text("确定"))
                x.click()
                TestReport.i("AppManager_handleSystemWindow:系统级弹窗事件ANR，[美柚没有响应] 点击确定")
                return true
            }

            val x = uiDevice.findObject(By.text("关闭应用"))
            if (x != null) {
                x.click()
                TestReport.i("AppManager_handleSystemWindow:系统级弹窗事件ANR，点击关闭应用")
                return true
            }
            val y = uiDevice.findObject(By.text("等待"))
            if (y != null) {
                y.click()
                TestReport.i("AppManager_handleSystemWindow:系统级弹窗事件ANR，点击等待")
                return true
            }
            val wait = uiDevice.findObject(By.text("稍后"))
            if (wait != null) {
                wait.click()
                TestReport.i("AppManager_handleSystemWindow:系统级弹窗-系统更新，点击稍后")
                return true
            }
        } catch (e :Exception) {
            TestReport.e("AppManager_handleSystemWindow:>>重复点击")
        }
        return false
    }
}
