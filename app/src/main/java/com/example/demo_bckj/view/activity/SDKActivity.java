package com.example.demo_bckj.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo_bckj.R;
import com.example.demo_bckj.base.BaseActivity;
import com.example.demo_bckj.listener.ClickListener;
import com.example.demo_bckj.listener.PlayInterface;
import com.example.demo_bckj.model.PayBus;
import com.example.demo_bckj.model.utility.CountDownTimerUtils;
import com.example.demo_bckj.model.utility.DeviceIdUtil;
import com.example.demo_bckj.model.utility.FileUtil;
import com.example.demo_bckj.model.utility.SPUtils;
import com.example.demo_bckj.presenter.DemoPresenter;
import com.example.demo_bckj.view.Constants;
import com.example.demo_bckj.view.dialog.PrivacyDialog;
import com.example.demo_bckj.view.dialog.UserAgreeDialog;
import com.example.demo_bckj.view.fragment.CServiceFragment;
import com.example.demo_bckj.view.fragment.PersonFragment;
import com.example.demo_bckj.view.fragment.WelfareFragment;
import com.example.demo_bckj.view.pop.PopupTel;
import com.example.demo_bckj.view.round.RoundView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.example.demo_bckj.model.utility.DeviceIdUtil.getDeviceId;

public class SDKActivity extends BaseActivity<DemoPresenter> implements ClickListener {

    private final String TAG = "MainActivity";

    private androidx.drawerlayout.widget.DrawerLayout DrawerLayout;
    private Button welfareBtn, cServiceBtn, personBtn;
    private android.widget.LinearLayout leftLayout;
    private androidx.appcompat.widget.LinearLayoutCompat welfareLin, cServiceLin, PersonLin;
    private ImageView userHead;
    private ImageView userMorePh;
    private ImageView userMorePw;
    private TextView userMore, welfareTxt, cServiceTxt, personTxt;
    private ImageView userMoreAgreement;
    private ImageView userMorePrivacy;
    private TextView userQuitLogin;
    private String string;
    private JSONObject jsonObject;
    private AlertDialog.Builder LoginBuilder = null;
    private AlertDialog.Builder RegisterBuilder = null;
    private AlertDialog.Builder LoginPwBuilder = null;
    private AlertDialog.Builder ForgetPwBuilder = null;
    private AlertDialog.Builder ResetPwBuilder = null;
    private AlertDialog.Builder AutoBuilder = null;
    private AlertDialog loginDialog, registerDialog, loginPwDialog, forgetDialog, resetDialog, autoDialog;
    private PrivacyDialog mPrivacyDialog;
    private UserAgreeDialog mUserAgreeDialog;


    private CServiceFragment cs;
    private PersonFragment pf;
    private WelfareFragment wp;

    public final static int REQUEST_READ_PHONE_STATE = 1;
    private int targetSdkVersion = 0;

    private HandlerThread mHandlerThread;
    private Handler handler;

    private String tel = null;
    SPUtils bcSP;
    SPUtils deviceSP;
    EventBus eventBus;

    @Override
    protected void initData() {
        Log.d("tag", getDeviceId());
        eventBus=EventBus.getDefault();
        eventBus.register(this);

        bcSP = SPUtils.getInstance(this, "bcSP");
        deviceSP = SPUtils.getInstance(this, "open");
        mHandlerThread=new HandlerThread("loginHandler");
        mHandlerThread.start();
        handler=new Handler(mHandlerThread.getLooper());
        handler.postDelayed(runnable, 500);

        //       //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }



        //设备型号
        String model = Build.MODEL;
        Log.d("TAG", "model==" + model);
        //设备名称
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        String AndroidName = defaultAdapter.getName();
        Log.d("TAG", "AndroidName==" + AndroidName);

        //设备网络运营商代码
        //        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //        String operator = DeviceIdUtil.getSimOperator(telManager.getSimOperator());
        //        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
        //            tel = telManager.getLine1Number();//手机号码
        //        }
        //        Log.e("tel  ", operator + "   " + tel);

        //接口请求
        presenter.getSdk(this);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //用户协议弹窗
            boolean isFirstRun = deviceSP.getBoolean("isFirst", false);
            if (!isFirstRun || !checkPermissionAllGranted(Constants.PermissionString)) {
                deviceSP.put("isFirst", true);
                popupAgreement();
            } else {
                String password = bcSP.getString("password", "");
                String account = bcSP.getString("account", "");
                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(account)) {
                    popupLoginAuto(account, password);
                }else {
                    boolean refresh=false;
                    try {
                        refresh=presenter.refreshToken(SDKActivity.this, SDKActivity.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!refresh){
                        popupLoginCode();
                    }
                }
            }
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    //获取网络名称
    private String getConnectWifiSsid() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("wifiInfo------", wifiInfo.toString());
        Log.d("SSID------", wifiInfo.getSSID());
        return wifiInfo.getSSID();

    }

    @Subscribe()
    public void PayBus(PayBus payBus){
        String url = payBus.getUrl();
    }

    @Override
    protected void initView() {
        DrawerLayout = (androidx.drawerlayout.widget.DrawerLayout) findViewById(R.id.DrawerLayout);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        userHead = (ImageView) findViewById(R.id.user_head);
        userMorePh = (ImageView) findViewById(R.id.user_morePh);
        userMorePw = (ImageView) findViewById(R.id.user_morePw);
        userMore = (TextView) findViewById(R.id.user_more);
        userMoreAgreement = (ImageView) findViewById(R.id.user_more_agreement);
        userMorePrivacy = (ImageView) findViewById(R.id.user_more_privacy);
        userQuitLogin = (TextView) findViewById(R.id.user_quit_login);

        welfareLin = (androidx.appcompat.widget.LinearLayoutCompat) findViewById(R.id.welfare_lin);
        cServiceLin = (androidx.appcompat.widget.LinearLayoutCompat) findViewById(R.id.cservice_lin);
        PersonLin = (androidx.appcompat.widget.LinearLayoutCompat) findViewById(R.id.person_lin);

        welfareTxt = (TextView) findViewById(R.id.welfare_txt);
        cServiceTxt = (TextView) findViewById(R.id.cservice_txt);
        personTxt = (TextView) findViewById(R.id.person_txt);

        welfareBtn = (Button) findViewById(R.id.welfare_btn);
        cServiceBtn = (Button) findViewById(R.id.cservice_btn);
        personBtn = (Button) findViewById(R.id.person_btn);

        welfareBtn.setOnClickListener(view -> {
            Welfare(false);
        });
        cServiceBtn.setOnClickListener(view -> {
            CService(false);
        });
        personBtn.setOnClickListener(view -> {
            Personal(false);
        });
    }

    @Override
    protected DemoPresenter initPresenter() {
        return new DemoPresenter();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_sdk;
    }

    @Override
    public void onSuccess(Object o) {

    }

    @Override
    public void onError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void CService(boolean show) {
        Log.d(TAG, "CService");
        cs = CServiceFragment.getInstance();

        if (show)
            DrawerLayout.openDrawer(Gravity.LEFT);
        changeStyle(1);
        nvTo(cs);
    }

    @Override
    public void Personal(boolean show) {
        Log.d(TAG, "Personal");
        pf = PersonFragment.getInstance();
        if (show)
            DrawerLayout.openDrawer(Gravity.LEFT);
        changeStyle(0);
        nvTo(pf);
    }

    @Override
    public void Welfare(boolean isShow) {
        Log.d(TAG, "Welfare");
        wp = WelfareFragment.getInstance();
        changeStyle(2);
        nvTo(wp);
    }

    @Override
    public void Switch() {
        Log.d(TAG, "Switch");
        DrawerLayout.closeDrawers();
        RoundView.getInstance().closeRoundView(this);
        bcSP.clear();
        popupLoginCode();
        if (pf!=null)
            pf.onDestroy();
        if (wp!=null)
            wp.onDestroy();
        if (cs!=null)
            cs.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        RoundView.getInstance().removeSmallWindow(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RoundView.getInstance().closeRoundView(this);
    }

    //用户协议弹窗
    private void popupAgreement() {
        View inflate = LayoutInflater.from(SDKActivity.this).inflate(R.layout.popup_agreement, null);
        Button popup_agree = inflate.findViewById(R.id.popup_agree);
        TextView popup_disagree = inflate.findViewById(R.id.popup_disagree);
        TextView privacyTxt = inflate.findViewById(R.id.txt_privacy);
        privacyTxt.setText("我们希望通过 ");
        SpannableString agreeTxt = new SpannableString("《用户协议》");
        SpannableString privacy = new SpannableString("《隐私政策》");
        agreeTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.sky_blue));//设置颜色
            }
        }, 0, agreeTxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        privacy.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.sky_blue));//设置颜色
            }
        }, 0, privacy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        privacyTxt.append(agreeTxt);
        privacyTxt.append("和");
        privacyTxt.append(privacy);
        privacyTxt.append("来帮助您了解我们为您提供的服务，及收集、处理您个人信息的相应规则。为向您提供特定服务或功能，我们会以弹窗形式、在征得您同意后获取特定权限和信息。拒绝授权仅会使得您无法使用其对应的特定服务或功能，但不影响您使用我们的其他服务。");
        PopupWindow popupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //        设置点击外部区域不可取消popupWindow
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        //显示popupWindow
        findViewById(R.id.main_layout).post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(inflate, Gravity.CENTER, 0, 0);
            }
        });
        //同意
        popup_agree.setOnClickListener(v -> {
            popupWindow.dismiss();
            ActivityCompat.requestPermissions(this, Constants.PermissionString, 1);
        });
        //不同意协议
        popup_disagree.setOnClickListener(v -> finish());
    }

    //手机号验证码登录
    private void popupLoginCode() {
        if (LoginBuilder != null) {
            return;
        }
        View inflate = LayoutInflater.from(SDKActivity.this).inflate(R.layout.popup_code, null);
        EditText popupLogin = inflate.findViewById(R.id.popup_login);
        EditText popupEtCode = inflate.findViewById(R.id.popup_Et_code);
        Button spinnerImg = inflate.findViewById(R.id.spinnerImg);
        TextView popupTvCode = inflate.findViewById(R.id.popup_Tv_code);
        CheckBox popupRb = inflate.findViewById(R.id.popup_Rb);
        TextView popupUser = inflate.findViewById(R.id.popup_user);
        TextView popup_register = inflate.findViewById(R.id.popup_register);
        ImageView popup_remove = inflate.findViewById(R.id.popup_remove);
        ImageView popup_remove_code = inflate.findViewById(R.id.popup_remove_code);
        TextView popupPrivacy = inflate.findViewById(R.id.popup_privacy);
        Button popupSubmit = inflate.findViewById(R.id.popup_submit);
        TextView popupLoginPw = inflate.findViewById(R.id.popup_loginPw);
        TextView play = inflate.findViewById(R.id.try_play);
        List<String> telLists = deviceSP.getList("tel", "");
        View v = LayoutInflater.from(SDKActivity.this).inflate(R.layout.pop_tel_list, null);
        spinnerImg.setOnClickListener(view -> {
            PopupTel popupTel = new PopupTel(this, telLists, popupLogin, v, inflate.getWidth(), 200, true);
            popupLogin.post(() -> popupTel.showAsDropDown(popupLogin, 0, 0));
        });
        LoginBuilder = new AlertDialog.Builder(this);
        LoginBuilder.setView(inflate);
        LoginBuilder.setCancelable(false);
        loginDialog = LoginBuilder.create();
        loginDialog.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() { // dialog 外部监听
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) loginDialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(loginDialog.getWindow().getDecorView().getWindowToken(), 0); // 解决键盘无法关闭问题
                return true;
            }
        });
        loginDialog.show();
        loginDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                LoginBuilder = null;
            }
        });
        //跳转注册
        popup_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog.dismiss();
                popupNumberRegister("", "", false);
            }
        });
        //输入框监听
        popupLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popupLogin.getText().toString() != null && !popupLogin.getText().toString().equals("")) {
                    popup_remove.setVisibility(View.VISIBLE);
                    popup_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupLogin.setText("");
                        }
                    });

                } else {
                    popup_remove.setVisibility(View.INVISIBLE);
                }
            }
        });
        //验证码输入监听
        popupEtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popupEtCode.getText().toString() != null && !popupEtCode.getText().toString().equals("")) {
                    popup_remove_code.setVisibility(View.VISIBLE);
                    popup_remove_code.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupEtCode.setText("");
                        }
                    });

                } else {
                    popup_remove_code.setVisibility(View.INVISIBLE);
                }
            }
        });
        //获取验证码
        popupTvCode.setOnClickListener(view -> {
            if (DeviceIdUtil.isMobileNO(popupLogin.getText().toString().trim())) {
                presenter.getPhoneLoginCode(this, popupLogin.getText().toString().trim());
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(popupTvCode, 60000, 1000);
                countDownTimerUtils.start();
            } else {
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            }
        });
        //用户协议
        popupUser.setOnClickListener(view -> UserAgreement());
        //隐私协议
        popupPrivacy.setOnClickListener(view -> PrivacyAgreement());
        popupSubmit.setOnClickListener(view -> {
            if (popupRb.isChecked()) {
                String number = popupLogin.getText().toString().trim();
                String code = popupEtCode.getText().toString().trim();
                presenter.getPhoneLogin(this, this, number, code, telLists, loginDialog);
            } else {
                Toast.makeText(SDKActivity.this, "请先勾选用户协议", Toast.LENGTH_SHORT).show();
            }
        });

        //跳转
        popupLoginPw.setOnClickListener(view -> {
            loginDialog.dismiss();
            popupLoginPw("", "", false);
        });

        play.setOnClickListener(view -> {
            if (!popupRb.isChecked()) {
                Toast.makeText(SDKActivity.this, "请先勾选用户协议", Toast.LENGTH_SHORT).show();
            } else {
                presenter.getDemoAccount(this, new PlayInterface() {
                    @Override
                    public void onSuccess(String account, String password) {
                        loginDialog.dismiss();
                        popupNumberRegister(account, password, true);
                    }

                    @Override
                    public void onError(String msg) {

                    }
                });
            }
        });
    }

    //账号密码登录弹窗
    private void popupLoginPw(String account, String password, boolean isChecked) {
        if (LoginPwBuilder != null) {
            return;
        }
        View inflate = LayoutInflater.from(SDKActivity.this).inflate(R.layout.popup_pw_login, null);
        EditText popupLogin = inflate.findViewById(R.id.popup_login);
        ImageView popup_back = inflate.findViewById(R.id.popup_back);
        EditText popup_et_pw = inflate.findViewById(R.id.popup_et_pw);
        CheckBox popupRb = inflate.findViewById(R.id.popup_Rb);
        TextView popupUser = inflate.findViewById(R.id.popup_user);
        ImageView popup_remove = inflate.findViewById(R.id.popup_remove);
        ImageView popup_remove_pw = inflate.findViewById(R.id.popup_remove_pw);
        TextView popupPrivacy = inflate.findViewById(R.id.popup_privacy);
        TextView popupRegister = inflate.findViewById(R.id.popup_register);
        Button popupSubmit = inflate.findViewById(R.id.popup_submit);
        TextView popup_forget_pw = inflate.findViewById(R.id.popup_forget_pw);
        LoginPwBuilder = new AlertDialog.Builder(this);
        LoginPwBuilder.setView(inflate);
        LoginPwBuilder.setCancelable(false);
        loginPwDialog = LoginPwBuilder.create();
        loginPwDialog.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() { // dialog 外部监听
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) loginPwDialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(loginPwDialog.getWindow().getDecorView().getWindowToken(), 0); // 解决键盘无法关闭问题
                return true;
            }
        });
        loginPwDialog.show();
        loginPwDialog.setOnDismissListener(dialogInterface -> LoginPwBuilder = null);
        popupLogin.setText(account);
        popup_et_pw.setText(password);
        popupRb.setChecked(isChecked);
        //返回
        popup_back.setOnClickListener(view -> {
            loginPwDialog.dismiss();
            popupLoginCode();
        });
        //立即注册
        popupRegister.setOnClickListener(view -> popupNumberRegister("", "", false));
        //忘记密码
        popup_forget_pw.setOnClickListener(view -> {
            loginPwDialog.hide();
            popupForgetPassword();
        });
        //账号密码登录
        popupSubmit.setOnClickListener(view -> {
            if (popupRb.isChecked()) {
                HashMap<Object, Object> map = new HashMap<>();
                presenter.getLoginPwLo(this, popupLogin.getText().toString().trim(), popup_et_pw.getText().toString().trim(), loginPwDialog, this);
            } else {
                Toast.makeText(SDKActivity.this, "请先勾选用户协议", Toast.LENGTH_SHORT).show();
            }
        });
        popupLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popupLogin.getText().toString() != null && !popupLogin.getText().toString().equals("")) {
                    popup_remove.setVisibility(View.VISIBLE);
                    popup_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupLogin.setText("");
                        }
                    });

                } else {
                    popup_remove.setVisibility(View.INVISIBLE);
                }
            }
        });
        //密码输入监听
        popup_et_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popup_et_pw.getText().toString() != null && !popup_et_pw.getText().toString().equals("")) {
                    popup_remove_pw.setVisibility(View.VISIBLE);
                    popup_remove_pw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup_et_pw.setText("");
                        }
                    });

                } else {
                    popup_remove_pw.setVisibility(View.INVISIBLE);
                }
            }
        });
        //用户协议
        popupUser.setOnClickListener(view -> UserAgreement());
        //隐私协议
        popupPrivacy.setOnClickListener(view -> PrivacyAgreement());
    }

    //验证手机号
    private void popupForgetPassword() {
        if (ForgetPwBuilder != null) {
            return;
        }
        View inflate = LayoutInflater.from(SDKActivity.this).inflate(R.layout.popup_code_phone, null);
        EditText popupLogin = inflate.findViewById(R.id.popup_login);
        EditText popupEtCode = inflate.findViewById(R.id.popup_Et_code);
        TextView popupTvCode = inflate.findViewById(R.id.popup_Tv_code);
        //        TextView popup_register =  inflate.findViewById(R.id.popup_register);
        ImageView popup_remove = inflate.findViewById(R.id.popup_remove);
        ImageView popup_back = inflate.findViewById(R.id.popup_back);
        ImageView popup_remove_code = inflate.findViewById(R.id.popup_remove_code);
        CheckBox popupRb = inflate.findViewById(R.id.popup_Rb);
        TextView popupUser = inflate.findViewById(R.id.popup_user);
        TextView popupPrivacy = inflate.findViewById(R.id.popup_privacy);
        Button popupSubmit = inflate.findViewById(R.id.popup_submit);
        TextView popup_service = inflate.findViewById(R.id.popup_service);
        TextView popup_loginPw = inflate.findViewById(R.id.popup_loginPw);
        ForgetPwBuilder = new AlertDialog.Builder(this);
        ForgetPwBuilder.setView(inflate);
        ForgetPwBuilder.setCancelable(false);
        forgetDialog = ForgetPwBuilder.create();
        forgetDialog.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() { // dialog 外部监听
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) forgetDialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(forgetDialog.getWindow().getDecorView().getWindowToken(), 0); // 解决键盘无法关闭问题
                return true;
            }
        });
        forgetDialog.show();
        forgetDialog.setOnDismissListener(dialogInterface -> ForgetPwBuilder = null);
        //验证
        popupSubmit.setOnClickListener(view -> {
            if (popupRb.isChecked()) {
                String trim1 = popupLogin.getText().toString().trim();
                String trim2 = popupEtCode.getText().toString().trim();
                if (TextUtils.isEmpty(trim1) || TextUtils.isEmpty(trim2)) {
                    Toast.makeText(this, "请输入手机号与验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!DeviceIdUtil.isMobileNO(trim1)) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                forgetDialog.hide();
                popupResetPassword(trim1, trim2);
            } else {
                Toast.makeText(SDKActivity.this, "请先勾选用户协议", Toast.LENGTH_SHORT).show();
            }
        });
        //返回上一级
        popup_back.setOnClickListener(view -> {
            loginPwDialog.show();
            forgetDialog.dismiss();
        });
        //跳转联系客服
        popup_service.setOnClickListener(view -> Toast.makeText(SDKActivity.this, "尽请期待", Toast.LENGTH_SHORT).show());
        //输入框监听
        popupLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popupLogin.getText().toString() != null && !popupLogin.getText().toString().equals("")) {
                    popup_remove.setVisibility(View.VISIBLE);
                    popup_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupLogin.setText("");
                        }
                    });

                } else {
                    popup_remove.setVisibility(View.INVISIBLE);
                }
            }
        });
        //验证码输入监听
        popupEtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popupEtCode.getText().toString() != null && !popupEtCode.getText().toString().equals("")) {
                    popup_remove_code.setVisibility(View.VISIBLE);
                    popup_remove_code.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupEtCode.setText("");
                        }
                    });

                } else {
                    popup_remove_code.setVisibility(View.INVISIBLE);
                }
            }
        });
        popup_loginPw.setOnClickListener(view -> popupLoginCode());
        popupTvCode.setOnClickListener(view -> {
            String trim1 = popupLogin.getText().toString().trim();
            if (TextUtils.isEmpty(trim1)) {
                Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!DeviceIdUtil.isMobileNO(trim1)) {
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            presenter.forgetPwd(this, trim1, popupTvCode);
        });
    }

    //重置密码
    private void popupResetPassword(String n, String c) {
        if (ResetPwBuilder != null) {
            return;
        }
        View inflate = LayoutInflater.from(SDKActivity.this).inflate(R.layout.popup_reset_password, null);
        ImageView popup_back = inflate.findViewById(R.id.popup_back);
        EditText popup_new_password = inflate.findViewById(R.id.popup_new_password);
        EditText popup_password_pw = inflate.findViewById(R.id.popup_password_pw);
        ImageView popup_remove = inflate.findViewById(R.id.popup_remove);
        ImageView popup_remove_pw_pw = inflate.findViewById(R.id.popup_remove_pw_pw);
        Button popupSubmit = inflate.findViewById(R.id.popup_submit);
        TextView popup_loginPw = inflate.findViewById(R.id.popup_loginPw);
        ResetPwBuilder = new AlertDialog.Builder(this);
        ResetPwBuilder.setView(inflate);
        ResetPwBuilder.setCancelable(false);
        resetDialog = ResetPwBuilder.create();
        // dialog 外部监听
        resetDialog.getWindow().getDecorView().setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) resetDialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(resetDialog.getWindow().getDecorView().getWindowToken(), 0); // 解决键盘无法关闭问题
            return true;
        });
        resetDialog.show();
        resetDialog.setOnDismissListener(dialog -> {
            ResetPwBuilder = null;
        });
        //确认重置密码
        popupSubmit.setOnClickListener(view -> {
            String trim1 = popup_new_password.getText().toString().trim();
            String trim2 = popup_password_pw.getText().toString().trim();
            presenter.resetPwd(this, n, c, trim1, trim2, resetDialog, forgetDialog,loginPwDialog);
        });
        //返回上一级
        popup_back.setOnClickListener(view -> {
            forgetDialog.show();
            resetDialog.dismiss();
        });
        //输入框监听
        popup_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popup_new_password.getText().toString() != null && !popup_new_password.getText().toString().equals("")) {
                    popup_remove.setVisibility(View.VISIBLE);
                    popup_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup_new_password.setText("");
                        }
                    });

                } else {
                    popup_remove.setVisibility(View.INVISIBLE);
                }
            }
        });

        popup_password_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popup_password_pw.getText().toString() != null && !popup_password_pw.getText().toString().equals("")) {
                    popup_remove_pw_pw.setVisibility(View.VISIBLE);
                    popup_remove_pw_pw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup_password_pw.setText("");
                        }
                    });

                } else {
                    popup_remove_pw_pw.setVisibility(View.INVISIBLE);
                }
            }
        });
        popup_loginPw.setOnClickListener(view -> {
            resetDialog.dismiss();
            popupLoginCode();
        });
    }

    //账号密码注册弹窗
    private void popupNumberRegister(String user, String password, boolean isChecked) {
        if (RegisterBuilder != null) {
            return;
        }
        View inflate = LayoutInflater.from(SDKActivity.this).inflate(R.layout.popup_number_register, null);
        ImageView popup_back = inflate.findViewById(R.id.popup_back);
        EditText popup_number = inflate.findViewById(R.id.popup_number);
        ImageView popup_remove_number = inflate.findViewById(R.id.popup_remove_number);
        ImageView popup_remove_pw_pw = inflate.findViewById(R.id.popup_remove_pw_pw);
        ImageView popup_remove_pw = inflate.findViewById(R.id.popup_remove_pw);
        EditText popup_password = inflate.findViewById(R.id.popup_password);
        EditText popup_password_pw = inflate.findViewById(R.id.popup_password_pw);
        CheckBox popupRb = inflate.findViewById(R.id.popup_Rb);
        TextView popupUser = inflate.findViewById(R.id.popup_user);
        TextView popupPrivacy = inflate.findViewById(R.id.popup_privacy);
        Button popupSubmit = inflate.findViewById(R.id.popup_submit);
        RegisterBuilder = new AlertDialog.Builder(this);
        RegisterBuilder.setView(inflate);
        RegisterBuilder.setCancelable(false);
        registerDialog = RegisterBuilder.create();
        registerDialog.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() { // dialog 外部监听
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) registerDialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(registerDialog.getWindow().getDecorView().getWindowToken(), 0); // 解决键盘无法关闭问题
                return true;
            }
        });
        registerDialog.show();
        registerDialog.setOnDismissListener(dialogInterface -> RegisterBuilder = null);
        popup_number.setText(user);
        popup_password.setText(password);
        popup_password_pw.setText(password);
        popupRb.setChecked(isChecked);

        inflate.post(new Runnable() {
            @Override
            public void run() {
                getScreenView(registerDialog);
            }
        });
        //返回
        popup_back.setOnClickListener(view -> {
            popupLoginCode();
            registerDialog.dismiss();
        });
        popupSubmit.setOnClickListener(view -> {
            String number = popup_number.getText().toString().trim();
            String pass = popup_password.getText().toString().trim();
            String pass2 = popup_password_pw.getText().toString().trim();
            if (popupRb.isChecked()) {
                if (TextUtils.equals(pass, pass2)) {
                    presenter.getLoginPwRe(this, number, pass, pass2, registerDialog, SDKActivity.this);
                } else {
                    Toast.makeText(SDKActivity.this, "两次密码不正确", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SDKActivity.this, "请先勾选用户协议", Toast.LENGTH_SHORT).show();
            }
        });
        popup_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popup_number.getText().toString() != null && !popup_number.getText().toString().equals("")) {
                    popup_remove_number.setVisibility(View.VISIBLE);
                    popup_remove_number.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup_number.setText("");
                        }
                    });

                } else {
                    popup_remove_number.setVisibility(View.INVISIBLE);
                }
            }
        });
        //密码输入监听
        popup_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popup_password.getText().toString() != null && !popup_password.getText().toString().equals("")) {
                    popup_remove_pw.setVisibility(View.VISIBLE);
                    popup_remove_pw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup_password.setText("");
                        }
                    });

                } else {
                    popup_remove_pw.setVisibility(View.INVISIBLE);
                }
            }
        });
        //确认密码输入监听
        popup_password_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popup_password_pw.getText().toString() != null && !popup_password_pw.getText().toString().equals("")) {
                    popup_remove_pw_pw.setVisibility(View.VISIBLE);
                    popup_remove_pw_pw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup_password_pw.setText("");
                        }
                    });

                } else {
                    popup_remove_pw_pw.setVisibility(View.INVISIBLE);
                }
            }
        });
        //用户协议
        popupUser.setOnClickListener(view -> UserAgreement());
        //隐私协议
        popupPrivacy.setOnClickListener(view -> PrivacyAgreement());
    }

    //自动登录弹窗
    private void popupLoginAuto(String account, String password) {
        if (AutoBuilder != null) {
            return;
        }
        AutoBuilder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(SDKActivity.this).inflate(R.layout.pop_login_tittle, null);
        TextView txt = inflate.findViewById(R.id.login_account);
        TextView btn = inflate.findViewById(R.id.login_switch);
        String tel = bcSP.getString("tel", "");
        txt.setText(TextUtils.isEmpty(tel) ? account.substring(0, 3) + "****" + account.substring(7, account.length()) :
                tel.substring(0, 3) + "****" + tel.substring(7, tel.length()));
        AutoBuilder.setView(inflate);
        autoDialog = AutoBuilder.create();
        Window window = autoDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setGravity(Gravity.TOP | Gravity.CENTER);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        params.windowAnimations = R.style.popwindowAnimStyle;
        params.width = 0;
        params.y = 80;
        autoDialog.getWindow().setAttributes(params);
        autoDialog.show();
        autoDialog.setOnDismissListener(dialogInterface -> AutoBuilder = null);
        presenter.getLoginPwLo(SDKActivity.this, account, password, autoDialog, SDKActivity.this);
        btn.setOnClickListener(v -> {
            autoDialog.dismiss();
            Switch();
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (RoundView.getInstance().winStatus == RoundView.WIN_BIG) {
            RoundView.getInstance().createSmallWindow(SDKActivity.this, this);
            RoundView.getInstance().removeBigWindow(this);
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, event)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 检查是否获取所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PackageManager packageManager = this.getPackageManager();
        PermissionInfo permissionInfo = null;
        for (int i = 0; i < permissions.length; i++) {
            try {
                permissionInfo = packageManager.getPermissionInfo(permissions[i], 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            CharSequence permissionName = permissionInfo.loadLabel(packageManager);
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                if (i == permissions.length - 1) {
                    popupLoginCode();
                }
                Log.i(TAG, "您同意了【" + permissionName + "】权限");
            } else {
                Log.i(TAG, "您拒绝了【" + permissionName + "】权限");
            }
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void changeStyle(int style) {
        switch (style) {
            case 0:
                personBtn.setBackgroundResource(R.mipmap.tabbar_me_highlight);
                personTxt.setTextColor(getResources().getColor(R.color.selected));
                cServiceBtn.setBackgroundResource(R.mipmap.personal_nor);
                cServiceTxt.setTextColor(getResources().getColor(R.color.nor));
                welfareBtn.setBackgroundResource(R.mipmap.welfare_nor);
                welfareTxt.setTextColor(getResources().getColor(R.color.nor));
                break;
            case 1:
                personBtn.setBackgroundResource(R.mipmap.tabbar_me_default);
                personTxt.setTextColor(getResources().getColor(R.color.nor));
                cServiceBtn.setBackgroundResource(R.mipmap.personal);
                cServiceTxt.setTextColor(getResources().getColor(R.color.selected));
                welfareBtn.setBackgroundResource(R.mipmap.welfare_nor);
                welfareTxt.setTextColor(getResources().getColor(R.color.nor));
                break;
            case 2:
                personBtn.setBackgroundResource(R.mipmap.tabbar_me_default);
                personTxt.setTextColor(getResources().getColor(R.color.nor));
                cServiceBtn.setBackgroundResource(R.mipmap.personal_nor);
                cServiceTxt.setTextColor(getResources().getColor(R.color.nor));
                welfareBtn.setBackgroundResource(R.mipmap.welfare);
                welfareTxt.setTextColor(getResources().getColor(R.color.selected));
                break;
            default:
                break;
        }
    }

    /**
     * 截屏
     *
     * @param registerDialog
     */
    public void getScreenView(AlertDialog registerDialog) {
        //获取窗口管理类,获取窗口的宽度和高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        //创建一个Bitmap内存区
        /*
         * Config.ARGB_8888:规定每一个像素占4个字节的存储空间
         *
         */
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //获取屏幕
        View screenView = registerDialog.getWindow().getDecorView();
        //开启绘图缓存
        screenView.setDrawingCacheEnabled(true);
        //返回屏幕View的视图缓存
        bitmap = screenView.getDrawingCache();
        FileUtil.saveImg(this, bitmap);
    }

    /**
     * 弹出用户协议
     */
    public void UserAgreement() {
        if (mUserAgreeDialog != null) {
            mUserAgreeDialog.show();
            return;
        }
        mUserAgreeDialog = new UserAgreeDialog(this);
        mUserAgreeDialog.show();
    }

    /**
     * 弹出隐私协议
     */
    public void PrivacyAgreement() {
        if (mPrivacyDialog != null) {
            mPrivacyDialog.show();
            return;
        }
        mPrivacyDialog = new PrivacyDialog(this);
        mPrivacyDialog.show();
    }

    public boolean shouldOverrideUrlLoading(final WebView view, String url) {
        // 获取上下文, H5PayDemoActivity为当前页面
        final Activity context = SDKActivity.this;
        // ------  对alipays:相关的scheme处理 -------
        if(url.startsWith("alipays:") || url.startsWith("alipay")) {
            try {
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
            } catch (Exception e) {
                new AlertDialog.Builder(context)
                        .setMessage("未检测到支付宝客户端，请安装后重试。")
                        .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri alipayUrl = Uri.parse("https://d.alipay.com");
                                context.startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
                            }
                        }).setNegativeButton("取消", null).show();
            }
            return true;
        }
        // ------- 处理结束 -------
        if (!(url.startsWith("http") || url.startsWith("https"))) {
            return true;
        }
        view.loadUrl(url);
        return true;
    }
}