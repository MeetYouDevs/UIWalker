package com.autotools.uiwalker.event

import com.autotools.uiwalker.data.InputData
import com.autotools.uiwalker.data.TestData
import com.autotools.uiwalker.data.TestReport
import com.autotools.uiwalker.expection.UnexpectedUiException
import com.autotools.uiwalker.model.UiObjectModel
import com.autotools.uiwalker.utils.CMDUtils


import java.util.ArrayList

/**
 * 对应控件级的操作
 */

internal class UiObjectEvent(private val uiObjectModel: UiObjectModel?) : EventController(true, true) {

    /**
     * 根据控件的属性，获取一个滑动的方向
     *
     * @return 方向
     */
    //把方向放在一个列表中，在这个列表中随机抽取
    // 横向列表
    //水平：垂直=1：1
    // 横向滑动概率增加
    //水平：垂直=8：2
    // 纵向滑动概率增加
    //水平：垂直=2：8
    val direction: SwipeDirection
        get() {
            val directionList = ArrayList<SwipeDirection>()
            val classStr = uiObjectModel!!.className!!.toLowerCase()
            if (classStr.contains("horizontal") || classStr.contains("viewpager")) {
                directionList.clear()
                directionList.add(SwipeDirection.UP)
                directionList.add(SwipeDirection.DOWN)
            } else if (uiObjectModel.bounds.width > uiObjectModel.bounds.height) {
                directionList.clear()
                for (i in 0..9) {
                    if (i < 8) {
                        directionList.add(SwipeDirection.UP)
                        directionList.add(SwipeDirection.DOWN)
                    } else {
                        directionList.add(SwipeDirection.LEFT)
                        directionList.add(SwipeDirection.RIGHT)
                    }
                }
            } else {
                directionList.clear()
                for (i in 0..9) {
                    if (i < 2) {
                        directionList.add(SwipeDirection.UP)
                        directionList.add(SwipeDirection.DOWN)
                    } else {
                        directionList.add(SwipeDirection.LEFT)
                        directionList.add(SwipeDirection.RIGHT)
                    }
                }
            }
            return directionList[TestData.random.nextInt(directionList.size)]
        }

    enum class SwipeDirection {
        UP, DOWN, LEFT, RIGHT
    }

    init {
        if (uiObjectModel == null) {
            TestReport.i("UiObjectEvent_UiObjectEvent: 目标控件为null，不响应任何操作！")
            isActive = false
            isReport = false
        }
    }

    /**
     * 点击
     */
    fun click_Event() {
        val x = uiObjectModel!!.bounds.centerX
        val y = uiObjectModel!!.bounds.centerY
        BaseEvents.click(x, y)
        report("【点击】[$x,$y]")
    }

    /**
     * 长按
     */
    fun longClick_Event() {
        val x = uiObjectModel!!.bounds.centerX
        val y = uiObjectModel!!.bounds.centerY
        BaseEvents.longClick(x, y)
        report("【长按】")
    }

    /**
     * 输入内容，内容随机从[InputData]中获取
     */
    fun inputRandomText() {

        val text: String = InputData.randomText
        val x = uiObjectModel!!.bounds.centerX
        val y = uiObjectModel.bounds.centerY
        if (TestData.random.nextBoolean()) {
            clearText(x,y)
        }
        BaseEvents.inputText(x, y, text)
        report("【输入】--> $text")
    }

    /**
     * 输入内容，内容随机从[InputData]中获取
     */
    fun inputText(text: String) {
        val x = uiObjectModel!!.bounds.centerX
        val y = uiObjectModel.bounds.centerY
        clearText(x,y)
        BaseEvents.inputText(x, y, text)
        report("【输入】--> $text")
    }

    fun inputLoginText(text: String) {
        val x = uiObjectModel!!.bounds.centerX
        val y = uiObjectModel.bounds.centerY
        BaseEvents.inputText(x, y, text)
        report("【输入】--> $text")
    }

    /**
     * 随机滑动
     */
    fun swipeRandomDirec_Event() {

        val direction = direction
        val scrollPosition = 0.7f
        val uiRect = uiObjectModel!!.bounds
        val left: Float = (uiRect.left + 10).toFloat()
        val top: Float = (uiRect.top + 10).toFloat()
        val centerY: Float = uiRect.centerY.toFloat()
        val centerX: Float = uiRect.centerX.toFloat()
        val x = left + uiRect.width * scrollPosition
        val y = top + uiRect.height * scrollPosition
        var info = ""
        when (direction) {
            UiObjectEvent.SwipeDirection.UP -> {
                BaseEvents.swipe(centerX, y, centerX, top)
                info = "【滑动 ↑】"
            }
            UiObjectEvent.SwipeDirection.DOWN -> {
                BaseEvents.swipe(centerX, top, centerX, y)
                info = "【滑动 ↓】"
            }
            UiObjectEvent.SwipeDirection.LEFT -> {
                BaseEvents.swipe(x, centerY, left, centerY)
                info = "【滑动 ←】"
            }
            UiObjectEvent.SwipeDirection.RIGHT -> {
                BaseEvents.swipe(left, centerY, x, centerY)
                info = "【滑动 →】"
            }
        }
        report(info)
    }

    /**
     * 随机操作
     */
    fun injectRandom() {
        val myClazz = UiObjectEvent::class.java
        val methods = myClazz.methods
        //使用反射随机调用一个event
        for (method in methods) {
            if (method.name.endsWith("_Event") && TestData.random.nextBoolean()) {
                try {
                    val event = this
                    method.invoke(event)
                    break

                } catch (e: Exception) {
                    val from = "UiObjectEvent_injectRandom"
                    val info = "反射调用方法" + method.name + "出错"
                    val ue = UnexpectedUiException(from, info, e, false)
                    ue.report()
                }

            }
        }
    }

    /**
     * 清空旧的内容
     */
    fun clearText(x: Int, y: Int) {
        BaseEvents.click(x,y)
        val oldText = uiObjectModel!!.text
        CMDUtils.runCmdByDevice("input keyevent KEYCODE_MOVE_END", false)
        for (i in 0 until oldText!!.length) {
            CMDUtils.runCmdByDevice("input keyevent KEYCODE_DEL", false)
        }
        report("【清空text】$oldText")
    }

    /**
     * 根据控件的属性，调用不同的响应方式
     */
    override fun trigger() {
//        // 路径屏蔽过滤
//        var uiInfo: String
//        if (!uiObjectModel!!.text!!.isEmpty()) {
//            uiInfo = uiObjectModel.text!!
//        } else if (!uiObjectModel.id!!.isEmpty()) {
//            uiInfo = uiObjectModel.id
//            uiInfo = uiInfo.substring(uiInfo.indexOf("/") + 1)
//        } else {
//            uiInfo = uiObjectModel.className!!
//        }
//        //屏蔽特定路径
//        if (PathManager.isRejectPath(uiInfo))
//            return

        //判断是否响应
        if (!isActive)
            return
        val uiClassName = uiObjectModel!!.className
        if (swipeList.contains(uiClassName) || uiObjectModel.isScrollable) {
            swipeRandomDirec_Event()
        } else if (editList.contains(uiClassName)) {
            inputRandomText()
        } else if (uiObjectModel.isClickable) {
            click_Event()
        } else if (uiObjectModel.isLongClickable) {
            longClick_Event()
        } else {
            injectRandom()
        }
    }

    override fun report(event: String) {
        if (isReport) {
            TestReport.i("UiObjectEvent_report:  页面" + uiObjectModel!!.actName + " ------- 控件" + uiObjectModel.className + uiObjectModel.bounds + " " + event + uiObjectModel.id)
        }
    }

    companion object {
        private val swipeList = ArrayList<String>()// 滑动操作的控件列表
        private val editList = ArrayList<String>()// 输入操作的控件列表

        /**
         * 初始化数据
         */
        init {

            // 初始化需要滑动的控件列表
            swipeList.add("android.widget.ListView")
            swipeList.add("android.widget.GridView")
            swipeList.add("android.widget.ScrollView")
            swipeList.add("android.widget.ImageSwitcher")
            swipeList.add("android.widget.AdapterViewFlipper")
            swipeList.add("android.widget.StackView")
            swipeList.add("android.widget.TextSwitcher")
            swipeList.add("android.widget.ViewAnimator")
            swipeList.add("android.widget.ViewFlipper")
            swipeList.add("android.widget.ViewSwitcher")
            swipeList.add("android.webkit.WebView")
            swipeList.add("android.support.v7.widget.RecyclerView")
            // 横向
            swipeList.add("android.widget.HorizontalScrollView")
            swipeList.add("android.support.v4.view.ViewPager")

            // 初始化输入信息列表
            editList.add("android.widget.EditText")
            editList.add("android.widget.MultiAutoCompleteTextView")
            editList.add("android.widget.AutoCompleteTextView")
            editList.add("android.widget.SearchView")
        }
    }
}
