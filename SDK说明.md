## 依赖引入

本SDK以aar包形式提供，需要拷贝到项目的libs目录中，在build.gradle中添加相关引用。

```java
// 将aar包放入libs文件夹下，并在App build.gradle的dependencies中添加依赖
implementation fileTree(dir: 'libs', include: ['*.aar'])
```

添加外部依赖

```java
// OkHttp的依赖库
    implementation 'com.squareup.okhttp3:okhttp:4.9.0'
//OkHttp日志拦截器依赖
    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.0'
/* Retrofit 依赖 */
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
/* Gson转换器 依赖 */
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    //今日头条适配
    implementation 'me.jessyan:autosize:1.2.1'

    implementation 'com.alibaba:fastjson:1.2.48'

    // 支付宝 SDK AAR 包所需的配置
    api 'com.alipay.sdk:alipaysdk-android:+@aar'
```

## 初始化前的准备

在app的AndroidManifest.xml中做如下修改:

```java
1. 在manifest节点加上：xmlns:tools="http://schemas.android.com/tools"
2. 在application 节点加上：tools:replace="android:icon, android:theme" 
3. 修改application节点中theme内容，将style parent属性改为：Theme.MaterialComponents.DayNight.NoActionBar.Bridge
4. Activity节点中添加可旋转 android:configChanges="orientation|screenSize|smallestScreenSize|screenLayout"
```



1. # **初始化**

1. ## 修改AndroidManifest.xml:

   ```Java
   1. 在manifest节点加上：xmlns:tools="http://schemas.android.com/tools"
   2. 在application 节点加上：tools:replace="android:icon, android:theme" 
   3. 修改application节点中theme内容，将style parent属性改为  Theme.MaterialComponents.DayNight.NoActionBar.Bridge
   4. Activity节点中添加可旋转 android:configChanges="orientation|screenSize|smallestScreenSize|screenLayout"
   ```

1. ## 修改activity_main.xml文件：

   > MainActivity的布局XML

   ```XML
   <?xml version="1.0" encoding="utf-8"?>
   <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
       xmlns:app="http://schemas.android.com/apk/res-auto"
       xmlns:tools="http://schemas.android.com/tools"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <FrameLayout
           android:id="@+id/home"
           android:layout_width="match_parent"
           android:layout_height="match_parent"/>
   </androidx.constraintlayout.widget.ConstraintLayout>
   ```

1. ## 在MainActivity中完成初始化：

   > MainActivity为应用默认打开activity

   ####  初始化接口

   ```Java
   /**
    * SDK初始化接口
    *
    * @param activity    调用初始化接口的ACtivity
    * @param gameId 游戏id。
    * @param loginCallBack 登录回调
    * @param loginOutCallBack 退出回调
    *
    */
   init(Activity activity, String gameId, LoginCallBack loginCallBack, LoginOutCallBack loginOutCallBack)
   ```

   ```Java
   SDKManager.getInstance().init(MainActivity.this, "gameId", new LoginCallBack() {
       @Override
       public void onSuccess(User user) {
           Log.d(TAG, "init login success" );
       }
   
       @Override
       public void onFail(String message) {
           Log.d(TAG, "init login fail" );
       }
   }, new LoginOutCallBack() {
       @Override
       public void onSuccess() {
           Log.d(TAG, "init login out success" );
       }
   
       @Override
       public void onFail(String message) {
           Log.d(TAG, "init login out fail" );
       }
   });
   ```

1. # SDK集成

1. ## 登录操作

   ```Java
   /**
    * 游戏主动登录
    *
    * @param context  上下文
    * @param callBack 登录回调监听
    */
   Login(Context context, LoginCallBack callBack) 
   ```

    登录回调监听`callBack`可以传`null`值，在未调用该登录接口或登录回调监听参数为`null`值时，将实现SDK初始化时注册的登录回调

1. ### 使用示例

```Java
SDKManager.getInstance().Login(MainActivity.this, new LoginCallBack() {
    @Override
    public void onSuccess(User user) {
        Log.d(TAG, "Login Success" );
    }

    @Override
    public void onFail(String message) {
        Log.d(TAG, "Login Fail" );
    }
})
```

1. ### 登录成功User字段说明

| 字段            | 类型    | 含义          | 说明 |
| --------------- | ------- | ------------- | ---- |
| account         | string  | 用户账号      |      |
| slug            | string  | 用户唯一标识  |      |
| isAuthenticated | boolean | 是否实名      |      |
| age             | int     | 年龄          |      |
| token           | string  | 账号登录token |      |

1. ## 退出操作

   ```Java
   /**
    * 游戏主动退出登录
    *
    * @param isDestroy  退出登录是否退出应用 
    * @param isLoginShow  退出登录是否弹出登录 
    * @param loginOutListener 退出回调监听
    */
   LoginOut(boolean isDestroy, boolean isLoginShow, LoginOutCallBack callBack)
   ```

    退出回调监听`callBack`可以传`null`值，在未调用该退出接口或退出回调监听参数为`null`值时，将实现SDK初始化时注册的退出回调

1. ### 使用示例

   ```Java
   SDKManager.getInstance().LoginOut(false, false, new LoginOutCallBack() {
       @Override
       public void onSuccess() {
           Log.d(TAG, "Login Out Success" );
       }
   
       @Override
       public void onFail(String message) {
           Log.d(TAG, "Login Out Fail" );
       }
   })
   ```

1. ## **支付操作**

   ```Java
   /**
    * 创建订单
    *
    * @param activity      调用支付订单的Activity
    * @param rechargeOrder 订单实体类
    * @param callBack      订单支付回调监听
    */
   Recharge(Activity activity, RechargeOrder rechargeOrder, RechargeCallBack callBack)
   ```

   1. ###  使用示例

   ```Java
   SDKManager.getInstance().Recharge(MainActivity.this, new RechargeOrder.Builder()
               .number_game("游戏订单号")
               .props_name("物品名称")
               .server_id("区服 ID")
               .server_name("区服名称")
               .role_id("角色 ID")
               .role_name("角色名称")
               .callback_url("https://apitest.infinite-game.cn/ping")
               .money(1)
               .extend_data("")
               .build(), new RechargeCallBack() {
           @Override
           public void onSuccess(String orderNum) {
               Log.d(TAG, "Pay Success" );
           }
   
           @Override
           public void onFail(String message) {
               Log.d(TAG, "Pay Fail" );
           }
       });
   ```

   1. ###  字段说明

   | 字段         | 必填  | 类型   | 含义       | 说明                                                         |
   | ------------ | ----- | ------ | ---------- | ------------------------------------------------------------ |
   | number_game  | true  | string | 游戏订单号 | 64 位以内字符串                                              |
   | money        | true  | string | 订单金额   | 订单金额（单位：分）                                         |
   | props_name   | true  | string | 物品名称   | 64 位以内字符串                                              |
   | server_id    | true  | string | 区服 ID    | 32 位以内字符串                                              |
   | server_name  | true  | string | 区服名称   | 64 位以内字符串                                              |
   | role_id      | true  | string | 角色 ID    | 32 位以内字符串                                              |
   | role_name    | true  | string | 角色名称   | 64 位以内字符串                                              |
   | callback_url | true  | string | 回调地址   | 长度 255 位以内的支付成功后通知 CP 下发物品的 url 地址       |
   | extend_data  | false | string | 扩展字段   | 长度 255 位以内的扩展字段，如果存在，通知回调时会原样回调给 CP |

1. ## **登录区服**

   ```Java
   /**
    * 角色登录区服
    *
    * @param context  上下文
    * @param roleBean 角色类
    * @param callback 回调
    */
   LoginServer(Context context, RoleBean roleBean, GameCallBack callback)
   ```

    `callback`可以传`null`值，将对接口返回不做处理

   1. ##  使用示例

   ```Java
    SDKManager.getInstance().LoginServer(MainActivity.this, new RoleBean.Builder()
               .serverID("builder.serverID")
               .serverName("builder.serverName")
               .roleId("builder.roleId")
               .roleName("builder.roleName").bulid(), new GameCallBack() {
           @Override
           public void onSuccess() {
               Log.d(TAG, "LoginServer Success");
           }
   
           @Override
           public void onFail(String message) {
               Log.d(TAG, "LoginServer Fail");
           }
       });
   ```

   1. ###  字段说明

   | 字段        | 必填 | 类型   | 含义    | 说明            |
   | ----------- | ---- | ------ | ------- | --------------- |
   | server_id   | true | string | 区服 ID | 32 位以内字符串 |
   | server_name | true | string | 区服名  | 64 位以内字符串 |
   | role_id     | true | string | 角色 ID | 32 位以内字符串 |
   | role_name   | true | string | 角色名  | 64 位以内字符串 |

1. ## **创建角色** 

   ```Java
   /**
    * 用户创建角色
    *
    * @param context  上下文
    * @param roleBean 角色类
    * @param callback 回调
    * /
   CreateRole(Context context, RoleBean roleBean, GameCallBack callback)
   ```

    `callback`可以传`null`值，将对接口返回不做处理

   1. ###  使用示例

      ```Java
      SDKManager.getInstance().CreateRole(MainActivity.this, new RoleBean.Builder()
                  .serverID("builder.serverID")
                  .serverName("builder.serverName")
                  .roleId("builder.roleId")
                  .roleName("builder.roleName").bulid(), new GameCallBack() {
              @Override
              public void onSuccess() {
                  Log.d(TAG, "CreateRole Success");
              }
      
              @Override
              public void onFail(String message) {
                  Log.d(TAG, "CreateRole Fail");
              }
          });
      ```

   1. ###  字段说明

   | 字段        | 必填 | 类型   | 含义    | 说明            |
   | ----------- | ---- | ------ | ------- | --------------- |
   | server_id   | true | string | 区服 ID | 32 位以内字符串 |
   | server_name | true | string | 区服名  | 64 位以内字符串 |
   | role_id     | true | string | 角色 ID | 32 位以内字符串 |
   | role_name   | true | string | 角色名  | 64 位以内字符串 |
