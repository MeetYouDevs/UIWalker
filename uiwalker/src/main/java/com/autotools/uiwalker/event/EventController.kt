package com.autotools.uiwalker.event

/**
 *
 * 用于控制事件
 *  * 1. 是否被响应
 *  * 2. 是否记录在log中
 */

abstract class EventController(var isActive: Boolean//操作是否被响应
                                        , var isReport: Boolean//是否输出到报告中
) {


    /**
     * 触发响应
     */
    abstract fun trigger()


    /**
     * 记录事件
     *
     * @param event 事件的具体描述，点击或是长按等
     */
    abstract fun report(event: String)
}
