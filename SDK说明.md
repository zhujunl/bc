## 依赖引入

本SDK以aar包形式提供，需要拷贝到项目的libs目录中，在build.gradle中添加相关引用。

```java
// 在App build.gradle的dependencies中添加依赖
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
```



## 初始化

修改activity_main.xml文件：

```java
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

在MainActivity中完成初始化：

```java
  FragmentManager fm = getSupportFragmentManager();
        HomeFragment homeFragment=HomeFragment.getInstance(new SDKListener() {
            @Override
            public void Login(User user) {
                Log.d(TAG, "登录=="+user );
            }

            @Override
            public void SignOut() {
                Log.d(TAG, "退出" );
            }

            @Override
            public void RechargeSuccess(String orderNum) {
                Log.d(TAG, orderNum+"充值成功");
            }

            @Override
            public void RechargeFail(String message) {
                Log.d(TAG, "充值失败=="+message);
            }
        });
        fm.beginTransaction()
                .replace(R.id.home,homeFragment)
                .addToBackStack(null)
                .commit();
```

## 接口说明

```java
public interface SDKListener {

    /**
     * 登录
     *
     * @param user
     */
    void Login(User user);

    /**
     * 退出
     */
    void SignOut();

    /**
     * 充值成功
     */
    void RechargeSuccess(String orderNum);

    /**
     * 充值失败
     *
     * @param message
     */
    void RechargeFail(String message);
}

```

## 支付操作

```java
     SdkControl.getInstance(getActivity()).Recharge(getActivity(), sdkListener,new RechargeOrder.Builder()
                    .number_game("游戏订单号")
                    .props_name("物品名称")
                    .server_id("区服 ID")
                    .server_name("区服名称")
                    .role_id("角色 ID")
                    .role_name("角色名称")
                    .callback_url("https://apitest.infinite-game.cn/ping")
                    .money(1)
                    .extend_data("")
                    .build());
        });
```

