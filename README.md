## uiwalker  
一种安卓UI遍历工具，通过将apk包安装到手机，启动instrument，dump并解析页面ui元素，进行深度遍历操作。uiwalker旨在一次安装可以遍历任意app，支持运行在安卓系统4.3+（API level 18+）的手机上。

#### 主要特点
1. 直接调用安卓底层uiautomator操作，速度快。
2. 基本控件操作：控件点击、上下左右滑动、随机中英文数字符号输入、长按。
3. 系统操作：home，pressback，menu，随机坐标点击，随机上下滑动。
4. 监听app是否被测app，跳出app后，能时刻重回app。
5. 处理系统弹窗：允许、确定、始终允许、同意、稍等等按钮点击。

## 遍历原理  
![image](%E9%81%8D%E5%8E%86%E5%8E%9F%E7%90%86.png)  
###### 几个特殊处理点  
* 在深度遍历过程中，随机选到的控件可能是个返回按键，这时候会导致整个页面没有全部遍历完成返回出去，大家可以自行处理
* 登录相关控件主要适配了美柚的登录页面，其他app可以自行适配  
* 为了防止点到vivo和oppo手机顶部黄栏关闭USB调试，坐标y<90的点统一处理成y=90再点击
* 中文输入参考：[uiautomator-unicode-input-helper](https://github.com/sumio/uiautomator-unicode-input-helper)

## 安装uiwalker和准备工作

1. uiwalker打包和安装  
    * 在根目录下使用gradlew.bat clean assembleDebugAndroidTest打包（linux使用./gradlew clean assembleDebugAndroidTest），  
        打包后在uiwalker\uiwalker\build\outputs\apk\androidTest\debug下，或使用已有包
    * 将包安装到手机，在手机应用->uiwalker->权限下将存储权限改为“允许”

2. 准备ui_walker.json文件，并写入如下信息  
      ```
    {'pkg': 'com.lingan.seeyou', 'act': 'com.lingan.seeyou.ui.activity.main.WelcomeActivity', 'whiteActivityList': 'com.lingan.seeyou.ui.activity.main.SeeyouActivity', 'blackActivityList': 'com.meiyou.minivideo.ui.bgm.BgmActivity', 'max_cycle': '2', 'run_time': 120, 'seed': 1, 'system_rate': 0.2, 'loginActivity': 'com.lingan.seeyou.ui.activity.user.login.LoginAccountActivity', 'userNameId': 'com.lingan.seeyou:id/login_et_email', 'userName': '', 'passwordId': 'com.lingan.seeyou:id/login_et_password', 'password': '', 'loginId': 'com.lingan.seeyou:id/login_btn_account'}
      ```
      <b>参数说明：
    
      ```
      pkg: 待测app的package，可以使用pm list package获取到
      act: app启动页activity
      whiteActivityList: activity白名单，不受遍历循环数限制，可以无限遍历
      blackActivityList: activity黑名单，完全不遍历，另外涉及到WebViewActivity页面也是不会遍历的，直接pressBack返回
      max_cycle: 遍历最大循环数（必填），参数记录在xml的<overTraveledCount>节点里
      run_time: 运行时长，单位分钟
      seed: 随机种子，不带默认取值java时间戳
      system_rate: 系统操作比例(0~1间的double值)
      loginActivity: 登录页面的activity
      userNameId: 登录用户名输入框控件id
      userName: 登录用户名
      passwordId: 登录密码输入框控件id
      password: 登录密码
      loginId: 登录完成控件id
      ```  

    
3. 将ui_walker.json文件push到手机/sdcard/目录下

4. 启动uiwalker进行遍历  
    `adb shell am instrument -w -r   -e debug false -e class 'com.autotools.uiwalker.testcase.TravelByCmdArgs' com.autotools.uiwalker.test/android.support.test.runner.AndroidJUnitRunner`  
    入口：运行TravelByCmdArgs类的所有用例，这个类就一个方法startTravel

    正常运行打印日志：  
    INSTRUMENTATION_STATUS: class=com.autotools.uiwalker.testcase.TravelByCmdArgs  
    INSTRUMENTATION_STATUS: current=1  
    INSTRUMENTATION_STATUS: id=AndroidJUnitRunner  
    INSTRUMENTATION_STATUS: numtests=1  
    INSTRUMENTATION_STATUS: stream=  
    com.autotools.uiwalker.testcase.TravelByCmdArgs:  
    INSTRUMENTATION_STATUS: test=startTravel  
    INSTRUMENTATION_STATUS_CODE: 1  
    之后会一直停在这，直到运行时间到，按ctrl+c可以停止进程，如果还是没停止掉，可以用``` adb shell am force-stop com.autotools.uiwalker.test ```停止运行

5. 遍历日志在/sdcard/auto_tools/uiwalker下，  
    temp文件夹：存放dump.xml临时文件  
    xml文件夹：遍历的控件操作结果xml，以每个activity作为一个xml存放  
    ui_walker_Error20200706.txt：错误日志  
    ui_walker_Info20200706.txt：info日志  
    walker_result.txt：遍历结束才会打印出所有遍历到的activity  


### FQA  
1. 时间还没到，uiwalker就停了？（错误日志会打印UiAutomation not connected!）  
    有些手机高耗电下，进程会被杀掉，所以需要在设置的耗电下修改，允许uiwalker后台高耗电运行

2. 启动报错：java.lang.IllegalStateException: UiAutomationService android.accessibilityservice.IAccessibilityServiceClient$Stub$Proxy@2b52030already registered!  
    uiwalker底层也是调用uiautomator运行，会和appium/atx冲突，运行之前，需要将这些相关的进程kill掉

3. java.io.FileNotFoundException: /sdcard/ui_walker.json: open failed: EACCES (Permission denied)  
    在设置->应用里将uiwalker的存储权限改为“允许”
    