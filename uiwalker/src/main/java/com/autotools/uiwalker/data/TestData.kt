package com.autotools.uiwalker.data

import android.text.TextUtils
import com.autotools.uiwalker.manager.AppManager


import java.util.ArrayList
import java.util.Random
import java.io.*
import org.json.JSONObject
import org.json.JSONArray


/**
 *
 * 测试相关数据
 * **注意：**只提供get方法，set只能通过构造方法赋值
 */
object TestData {
    var testPkg: String = ""
    var act: String = ""
    var seed: Long = 0
    var random: Random = Random(seed)
    var runTime_ms: Long = 0
    var runTime_min: Int = 0
    var maxCycle: Int = 0
    var isCovg: Boolean = false
    var isNeedLogin: Boolean = false
    var loginActivity: String = ""
    var deleteRate: Double = 0.toDouble()
    var systemEventRate: Double = 0.toDouble()
    var pkg_reg: String = ""
    var activityReg: String = ""
    var blackActReg: String = ""
    var appName: String = ""
    //登录数据
    var loginData: LoginData = LoginData("", "", "", "", "")
    //白名单应用
    var whiteApps: ArrayList<String> = ArrayList()
    //白名单Activity (被测应用下不受深度影响)
    var whiteActivitys: ArrayList<String> = ArrayList()
    var blackActivitys: ArrayList<String> = ArrayList()

    /**
     * 初始化测试信息
     *
     * @param t_pkg       被测应用包名
     * @param t_seed      seed值
     * @param t_runTime   测试时长，单位为分钟
     * @param t_loginData 用户登录相关数据
     */
    fun init(t_pkg: String,t_act: String, t_seed: Long, t_runTime: Int, t_deleteRate: Double, t_systemRate: Double, t_maxCycle: Int, t_loginData: LoginData?,t_loginActivity: String, t_whiteApps: ArrayList<String>, t_whiteActivitys: ArrayList<String>, t_blackActivitys: ArrayList<String>) {
        TestData.testPkg = t_pkg
        TestData.seed = t_seed
        TestData.runTime_min = t_runTime
        TestData.systemEventRate = t_systemRate
        TestData.act = t_act
        TestData.loginActivity = t_loginActivity
        maxCycle = t_maxCycle
        //转化为ms
        runTime_ms = (TestData.runTime_min * 1000 * 60).toLong()
        random = Random(seed)
        deleteRate = t_deleteRate
        if (t_loginData != null) {
            loginData = t_loginData
        }
        whiteApps = t_whiteApps
        whiteActivitys = t_whiteActivitys
        pkg_reg = t_pkg
        TestData.appName = AppManager.getAppName(t_pkg)
        for (pkg in whiteApps) {
            TestData.pkg_reg += "|$pkg"
        }
        for (act in whiteActivitys) {
            TestData.activityReg += "$act|"
        }
        blackActivitys = t_blackActivitys
        for (blackAct in blackActivitys) {
            TestData.blackActReg += "$blackAct|"
        }
        isNeedLogin = t_loginData != null
        showTestDataInfo()
    }

    fun showTestDataInfo() {
        TestReport.i("------------------测试参数------------------")
        TestReport.i("被测应用：$appName")
        TestReport.i("Act最大遍历次数：$maxCycle")
        TestReport.i("travelLib版本：" + AppManager.versionName)
        TestReport.i("测试时长：$runTime_min min")
        TestReport.i("遍历顺序seed值：$seed")
        TestReport.i("删除时长比：$deleteRate")
        TestReport.i("系统事件比：$systemEventRate")
        TestReport.i("最大循环数：$maxCycle")
        if (isNeedLogin) {
            TestReport.i("用户ID：" + loginData!!.userId + " 用户名："
                    + loginData!!.user)
            TestReport.i("密码ID：" + loginData!!.psdId + " 密码："
                    + loginData!!.psd)
            TestReport.i("登录ID：" + loginData!!.loginId)
        }
        if (whiteApps.size>0) {
            TestReport.i("白名单应用：" + pkg_reg!!)
        }
        if (whiteActivitys.size>0) {
            TestReport.i("白名单Activity：" + activityReg!!)
        }
        if (blackActivitys.size >0) {
            TestReport.i("黑名单Activity：" + blackActReg!!)
        }
        TestReport.i("--------------------------------------------")
    }

    fun getRunTime_min(): Long {
        return runTime_min.toLong()
    }


    fun initDataByJson(file_path: String) {
        val jsonText = File(file_path).readLines()[0]
        val json = JSONObject(jsonText)

        val mPkg = if (TextUtils.isEmpty(json.getString(CommonArgs.pkg))) CommonArgs.default_pkg else json.getString(CommonArgs.pkg)
        val mSeed = if (json.isNull(CommonArgs.seed)) CommonArgs.default_seed else java.lang.Long.parseLong(json.getString(CommonArgs.seed)!!)
        val mRunTime = if (json.getString(CommonArgs.runtime) == null) CommonArgs.default_runTime else Integer.parseInt(json.getString(CommonArgs.runtime)!!)
        val mAct = if (TextUtils.isEmpty(json.getString(CommonArgs.act))) CommonArgs.default_act else json.getString(CommonArgs.act)
        val mWhitePkgList = if (json.isNull(CommonArgs.whitePkgList)) null else json.getString(CommonArgs.whitePkgList)
        val mWhiteActivityList = if (json.isNull(CommonArgs.whiteActivityList)) null else json.getString(CommonArgs.whiteActivityList)
        val mBlackActivityList = if (json.isNull(CommonArgs.blackActivityList)) null else json.getString(CommonArgs.blackActivityList)
        val loginActivity = if (json.isNull(CommonArgs.loginActivity)) null else json.getString(CommonArgs.loginActivity)
        val mUserNameId = if (json.isNull(CommonArgs.userNameId)) null else json.getString(CommonArgs.userNameId)
        val mUserName = if (json.isNull(CommonArgs.userName)) null else json.getString(CommonArgs.userName)
        val mPasswordId = if (json.isNull(CommonArgs.passwordId)) null else json.getString(CommonArgs.passwordId)
        val mPassword = if (json.isNull(CommonArgs.password)) null else json.getString(CommonArgs.password)
        val mLoginId = if (json.isNull(CommonArgs.loginId)) null else json.getString(CommonArgs.loginId)

        val mDeleteRate = if (json.isNull(CommonArgs.deleteRate)) CommonArgs.default_deleteRate else java.lang.Double.parseDouble(json.getString(CommonArgs.deleteRate)!!)
        val mSystemEventRate = if (json.isNull(CommonArgs.systemEventRate)) CommonArgs.default_systemEventRate else java.lang.Double.parseDouble(json.getString(CommonArgs.systemEventRate)!!)

        val mMaxCycle = if (json.isNull(CommonArgs.maxCycle)) CommonArgs.default_maxCycle else Integer.parseInt(json.getString(CommonArgs.maxCycle)!!)
        val targetActsTemp = if (json.isNull(CommonArgs.targetActs)) null else json.getJSONArray(CommonArgs.targetActs)
        val targetActs = ArrayList<String>()
        if (targetActsTemp != null) {
            for (i in 0 until targetActsTemp.length()) {
                targetActs.add(targetActsTemp.getString(i))
            }
        }

        var loginData: LoginData? = null
        if (loginActivity != null && mUserNameId != null && mUserName != null && mPasswordId != null && mPassword != null && mLoginId != null) {
            loginData = LoginData( mUserNameId, mUserName, mPasswordId, mPassword, mLoginId)
        }

        val whiteActivityList = ArrayList<String>()
        if (mWhiteActivityList != null) {
            val srts = mWhiteActivityList.split(CommonArgs.divider.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            for (temp in srts) {
                if (!TextUtils.isEmpty(temp))
                    whiteActivityList.add(temp.trim())
            }
        }
        val blackActivityList = ArrayList<String>()
        if (mBlackActivityList != null) {
            val srts = mBlackActivityList.split(CommonArgs.divider.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            for (temp in srts) {
                if (!TextUtils.isEmpty(temp))
                    blackActivityList.add(temp.trim())
            }
        }
        val whitePkgs = ArrayList<String>()
        if (mWhitePkgList != null) {
            val srts = mWhitePkgList.split(CommonArgs.divider.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            for (temp in srts) {
                if (!TextUtils.isEmpty(temp))
                    whitePkgs.add(temp.trim())
            }
        }
        TestData.init(mPkg!!,mAct!!, mSeed, mRunTime, mDeleteRate, mSystemEventRate, mMaxCycle, loginData, loginActivity!!, whitePkgs, whiteActivityList, blackActivityList)

    }
}
