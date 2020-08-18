package com.autotools.uiwalker.event

import com.autotools.uiwalker.data.TestData
import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.manager.ActManager
import com.autotools.uiwalker.manager.AppManager
import com.autotools.uiwalker.model.ActivityModel
import com.autotools.uiwalker.model.UiObjectModel
import com.autotools.uiwalker.utils.UiUtils.Companion.uiDevice


import java.util.ArrayList


/**
 * 遍历过程中可能用到的事件
 */

/**
 * 遍历过程中的事件，分为系统级和应用级两种
 */
class TravelEvent {
    private val systemEvent = SystemEvent()
    private var act: ActivityModel? = null

    fun setTravelAct(activityModel: ActivityModel) {
        this.act = activityModel
    }

    /**
     * 根据情况调用不同的响应方式
     */
    fun trigger() {
        if (!AppManager.isInWhiteApps) {
            TestReport.i("TravelEvent_trigger: 【黑名单】当前在黑名单界面，不响应本轮操作，重新进入应用。")
            uiDevice.pressBack()
            uiDevice.pressBack()
            AppManager.reopenApp(TestData.testPkg)
            return
        }
        if (act == null) {
            TestReport.i("TravelEvent_trigger: 【信息获取异常】获取不到当前act信息，不响应本轮操作，重新进入应用。")
            AppManager.reopenApp(TestData.testPkg)
            return
        }
        //如果当前Act已经过度遍历，则不响应
        if (act!!.overTraveled) {
            TestReport.i("TravelEvent.trigger: 【过度遍历】${act!!.actName}，不响应本轮操作，返回上级界面。")
            systemEvent.overTravelEvent()
            return
        }

        //根据seed得到的random获取0~1之间的伪随机数
        val r = TestData.random.nextDouble()
        //若随机数小于系统时间占比则触发系统随机事件
        if (r < TestData.systemEventRate) {
            triggerOnSystem()
        } else {
            triggerOnApp()
        }
    }


    /**
     *
     * **system级别的操作**
     */
    fun triggerOnSystem() {
        systemEvent.trigger()
    }

    /**
     *
     * **app级别的操作**
     * 在页面中选取一个控件，并且响应控件
     */
    private fun triggerOnApp() {
        val currentUiList = ActManager.currentUiList
        if (currentUiList.isEmpty()) {
            TestReport.i("TravelEvent_triggerOnApp: 当前获取不到控件信息，进行系统级的随机操作。")
            triggerOnSystem()
            return
        }
        //选取有效的控件
        val targetUiObject = getTargetUiObject(currentUiList)

        if (targetUiObject == null) {
            uiDevice.pressBack()
            TestReport.i("TravelEvent_triggerOnApp:返回上级界面")
            return
        }
        //保存遍历UI以及当前发现控件数
        //        UiChooser.saveUiData(targetUiObject);
        //触发控件的响应
        val uiObjectEvent = UiObjectEvent(targetUiObject)
        uiObjectEvent.trigger()
        //将响应过的控件持久化存入
        act!!.addOperatedUiObject(targetUiObject)
    }

    /**
     * 随机选取到的控件是否被点击过
     *
     * @param model
     * @param currentUiList 当前的控件
     * @return true 被点击过
     */
    private fun isTargetUiObject(model: UiObjectModel, currentUiList: ArrayList<UiObjectModel>): Boolean {
        if (act == null || act!!.getOperatedUiList() == null) {
            return true
        }
        if (act!!.getOperatedUiList()!!.contains(model)) {
            //如果已经被点击过了，则在currentUiList中将其移除
            currentUiList.remove(model)
            return false
        }
        return true
    }

    /**
     *
     * 从当前页面中随机选取符合点击条件的控件，需满足：
     *  * 1. 未被点击过，也就是不存在xml文件中operateUiList中
     *
     * @param currentUiList 当前页面控件列表
     * @return null 如果当前控件全部均被点击过
     */
    private fun getTargetUiObject(currentUiList: ArrayList<UiObjectModel>): UiObjectModel? {
        var targetUiObject: UiObjectModel
        var randomIndex: Int
        do {
            if (currentUiList.size < 1) {
                act!!.travelFinished()
                return null
            }
            randomIndex = TestData.random.nextInt(currentUiList.size)
            targetUiObject = currentUiList[randomIndex]
        } while (!isTargetUiObject(targetUiObject, currentUiList))
        return targetUiObject
    }
}
