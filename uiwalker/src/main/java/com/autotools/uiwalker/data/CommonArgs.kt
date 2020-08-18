package com.autotools.uiwalker.data

import android.os.Bundle

/**
 *
 * 用于进程间的传参
 * **注意：**需要两个工程保持一致
 */

class CommonArgs(internal var bundle: Bundle) {

    class MyArg(var type: String, var name: String)




    companion object {
        var travelLib_pkg = "com.autotools.uiwalker.test"
        var travelLib_apk_pkg = "com.autotools.uiwalker"

        var divider = "--"//uiautomator传参时候用的pkg之间的分隔符

        var default_runTime = 1
        var default_seed = System.currentTimeMillis()
        var default_maxCycle = 50
        var default_deleteRate = 0.8
        var default_systemEventRate = 0.1
        var default_pkg = ""
        var default_act = ""
        var default_userNameID = ""
        var default_psdID = ""
        var default_userName = ""
        var default_password = ""
        var default_loginBtnID = ""

        var pkg = "pkg"// 被测应用包名
        var act = "act"
        var runtime = "run_time"// 测试时长
        var whitePkgList = "whitePkgList"// 白名单列表
        var whiteActivityList = "whiteActivityList"
        var blackActivityList = "blackActivityList"
        var loginActivity = "loginActivity"
        var userNameId = "userNameId"// 用户名ID
        var userName = "userName"// 用户名
        var passwordId = "passwordId"// 密码ID
        var password = "password"// 密码
        var loginId = "loginId"// 登录控件ID
        var seed = "seed"//随机值
        var deleteRate = "deleteRate"//随机值
        var systemEventRate = "system_rate"//系统事件比例
        var maxCycle = "max_cycle"//随机值
        var targetActs = "targetActs"//随机值
    }

}
