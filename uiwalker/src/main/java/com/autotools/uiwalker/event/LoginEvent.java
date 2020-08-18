package com.autotools.uiwalker.event;

import com.autotools.uiwalker.data.LoginData;
import com.autotools.uiwalker.data.TestData;
import com.autotools.uiwalker.data.TestReport;
import com.autotools.uiwalker.model.UiObjectModel;
import com.autotools.uiwalker.utils.UiFinder;
import com.autotools.uiwalker.utils.UiUtils;
import java.util.HashMap;
import java.util.Map;

public class LoginEvent {

    /**
     * 用户登录的功能
     */
    public static void checkLogin() {
        TestReport.INSTANCE.i("LoginEvent_checkLogin:into chekLogin()");
        LoginData loginData = TestData.INSTANCE.getLoginData();

        // 用户名控件过滤
        Map user = new HashMap();
        user.put("resource-id", loginData.getUserId());
        UiObjectModel userNameObject = UiFinder.INSTANCE.getByAttrs(user);
        if (userNameObject == null) {
            return;
        }
        new UiObjectEvent(userNameObject).inputText(loginData.getUser());
        TestReport.INSTANCE.i("<loginManager> ------ 【输入用户名】" + loginData.getUser() + " ,控件：" + userNameObject.getClassName());


        // 密码控件过滤
        Map psd = new HashMap();
        psd.put("resource-id", loginData.getPsdId());
        UiObjectModel psdObject = UiFinder.INSTANCE.getByAttrs(psd);
        if (psdObject == null) {
            return;
        }
        new UiObjectEvent(psdObject).inputText(loginData.getPsd());
        TestReport.INSTANCE.i("<loginManager> ------ 【输入密码】" + loginData.getPsd() + " ,控件：" + psdObject.getClassName());
        UiUtils.Companion.getUiDevice().pressBack();
        TestReport.INSTANCE.i("<loginManager> ------ 【输入密码后】 按press back.");

        //勾选框： 我已阅读并同意 美柚用户服务协议 和 隐私政策
        Map cb = new HashMap();
        cb.put("resource-id", "com.lingan.seeyou:id/cb");
        UiObjectModel cbObject = UiFinder.INSTANCE.getByAttrs(cb);

        if (cbObject != null && !cbObject.isChecked()) {
            new UiObjectEvent(cbObject).click_Event();
            TestReport.INSTANCE.i("<loginManager> ------ 【单选框_我已阅读】" + "com.lingan.seeyou:id/cb" + " ,控件：" + cbObject.getClassName());
        }

        // 登录控件过滤
        Map login = new HashMap();
        login.put("resource-id", loginData.getLoginId());
        UiObjectModel loginObject = UiFinder.INSTANCE.getByAttrs(login);
        if (loginObject == null) {
            return;
        }

        new UiObjectEvent(loginObject).click_Event();
        TestReport.INSTANCE.i("<loginManager> ------ 【点击登录】 ,控件：" + loginObject.getClassName());

    }
}
