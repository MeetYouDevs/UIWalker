package com.autotools.uiwalker.data

import java.util.HashMap

object InputData {
    /**
     * 输入数据，用于对EditText的输入数据控制
     */
    private var textValue: MutableMap<String, String> = HashMap()


    val randomText: String
        get() {
            val keys = textValue.keys.toTypedArray()
            val randomKey = keys[TestData.random.nextInt(keys.size)]
            return textValue[randomKey] ?: return "1234567890"
        }

    init {
        textValue["Num"] = "1234567890"
        textValue["Mix"] = "123abc一二三！@#￥%……&*()_+"
        textValue["Letters"] = "beautiful"
        textValue["Web"] = "http://www.baidu.com/"
        textValue["Chinese"] = "今天天天真好呀haha"
        textValue["Tel"] = "100861001"
        textValue["Adrress"] = "福建厦门"
        textValue["Email"] = "beautiful@hello.com"

    }
}
