package com.example.demo_bckj.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo_bckj.R;
import com.example.demo_bckj.base.BaseFragment;
import com.example.demo_bckj.control.SDKListener;
import com.example.demo_bckj.db.entity.AccountEntity;
import com.example.demo_bckj.listener.ClickListener;
import com.example.demo_bckj.listener.PlayInterface;
import com.example.demo_bckj.manager.DBManager;
import com.example.demo_bckj.manager.HttpManager;
import com.example.demo_bckj.model.utility.CountDownTimerUtils;
import com.example.demo_bckj.model.utility.DeviceIdUtil;
import com.example.demo_bckj.model.utility.SPUtils;
import com.example.demo_bckj.presenter.HomePresenter;
import com.example.demo_bckj.view.Constants;
import com.example.demo_bckj.view.dialog.PrivacyDialog;
import com.example.demo_bckj.view.dialog.RealNameDialog;
import com.example.demo_bckj.view.dialog.UserAgreeDialog;
import com.example.demo_bckj.view.pop.PopupTel;
import com.example.demo_bckj.view.round.MyWebView;
import com.example.demo_bckj.view.round.RoundView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import static com.example.demo_bckj.model.utility.DeviceIdUtil.getDeviceId;

/**
 * @author ZJL
 * @date 2023/1/3 10:38
 * @des 主页
 * @updateAuthor
 * @updateDes
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements ClickListener {
    private final String TAG = "HomeFragment";

    public static HomeFragment instance;

    public static HomeFragment getInstance(SDKListener sdkListener) {
        if (instance == null) {
            instance = new HomeFragment(sdkListener);
        }
        return instance;
    }

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
    private MyWebView myWebView, webView;
    private String string;
    private JSONObject jsonObject;
    private AlertDialog alertDialog, AgreementDialog;
    AlertDialog.Builder AutoBuilder;
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
    private SDKListener sdkListener;
    private List<String> accountLists, telLists;

    public HomeFragment(SDKListener sdkListener) {
        this.sdkListener = sdkListener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        AccountEntity account = DBManager.getInstance(getActivity()).getAccount();
        if (account != null && !account.getAuthenticated()) {
            DBManager.getInstance(getActivity()).delete();
        }
    }


    @Override
    protected void initData() {
        //        CrashReport.initCrashReport(getContext().getApplicationContext(), "4a65560b7d", true);
        HttpManager.getInstance().setListener(sdkListener, this, getActivity());
        Log.d("tag==", getDeviceId());
        new Thread(() -> {
            try {
                boolean init = presenter.init(getActivity());
                Log.d(TAG, "init==" + init);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        bcSP = SPUtils.getInstance(getActivity(), "bcSP");
        accountLists = bcSP.getList("account", "");
        telLists = bcSP.getList("tel", "");
        AutoBuilder = new AlertDialog.Builder(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() { // dialog 外部监听
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) alertDialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(alertDialog.getWindow().getDecorView().getWindowToken(), 0); // 解决键盘无法关闭问题
                return true;
            }
        });
        HttpManager.getInstance().setData(accountLists, telLists);

        mHandlerThread = new HandlerThread("loginHandler");
        mHandlerThread.start();
        handler = new Handler(mHandlerThread.getLooper());
        handler.postDelayed(runnable, 50);

        //       //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        DrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //用户协议弹窗
            boolean isFirstRun = bcSP.getBoolean("isFirst", false);
            if (!isFirstRun || !checkPermissionAllGranted(Constants.PermissionString)) {
                bcSP.put("isFirst", true);
                getActivity().runOnUiThread(() -> popupAgreement());
                presenter.getSdk(getActivity());
            } else {
                presenter.getSdk(getActivity(), bcSP.getInt("id"));
                AccountEntity account = DBManager.getInstance(getActivity()).getAccount();
                if (account != null && !TextUtils.isEmpty(account.getAccount()) && !TextUtils.isEmpty(account.getPassword())) {
                   getActivity().runOnUiThread(()-> presenter.getLoginPwLo(getActivity(), account.getAccount(), account.getPassword()));
                } else {
                    boolean refresh = false;
                    try {
                        refresh = presenter.refreshToken(getActivity());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!refresh) {
                        getActivity().runOnUiThread(() -> popupLoginCode());
                    }
                }
            }
        }
    };

    @Override
    protected void initView() {
        DrawerLayout = (androidx.drawerlayout.widget.DrawerLayout) v.findViewById(R.id.DrawerLayout);
        leftLayout = (LinearLayout) v.findViewById(R.id.left_layout);
        userHead = (ImageView) v.findViewById(R.id.user_head);
        userMorePh = (ImageView) v.findViewById(R.id.user_morePh);
        userMorePw = (ImageView) v.findViewById(R.id.user_morePw);
        userMore = (TextView) v.findViewById(R.id.user_more);
        userMoreAgreement = (ImageView) v.findViewById(R.id.user_more_agreement);
        userMorePrivacy = (ImageView) v.findViewById(R.id.user_more_privacy);
        userQuitLogin = (TextView) v.findViewById(R.id.user_quit_login);

        welfareLin = (androidx.appcompat.widget.LinearLayoutCompat) v.findViewById(R.id.welfare_lin);
        cServiceLin = (androidx.appcompat.widget.LinearLayoutCompat) v.findViewById(R.id.cservice_lin);
        PersonLin = (androidx.appcompat.widget.LinearLayoutCompat) v.findViewById(R.id.person_lin);

        welfareTxt = (TextView) v.findViewById(R.id.welfare_txt);
        cServiceTxt = (TextView) v.findViewById(R.id.cservice_txt);
        personTxt = (TextView) v.findViewById(R.id.person_txt);

        welfareBtn = (Button) v.findViewById(R.id.welfare_btn);
        cServiceBtn = (Button) v.findViewById(R.id.cservice_btn);
        personBtn = (Button) v.findViewById(R.id.person_btn);
        myWebView = v.findViewById(R.id.myWebView);
        webView = v.findViewById(R.id.WebView);

        welfareBtn.setOnClickListener(view -> {
            Welfare(false);
        });
        cServiceBtn.setOnClickListener(view -> {
            CService(false);
        });
        personBtn.setOnClickListener(view -> {
            Personal(false, true);
        });
    }

    @Override
    protected int initLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    protected HomePresenter initPresenter() {
        return new HomePresenter(sdkListener);
    }

    @Override
    public void onSuccess(Object o) {

    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
    public void Personal(boolean show, boolean isAuthenticated) {
        Log.d(TAG, "Personal");
        pf = PersonFragment.getInstance(sdkListener);
        if (pf.getListener() == null) {
            pf.setListener(new PersonFragment.privacyListener() {
                @Override
                public void user() {
                    DrawerLayout.closeDrawers();
                    UserAgreement(alertDialog, false);
                }

                @Override
                public void privacy() {
                    DrawerLayout.closeDrawers();
                    PrivacyAgreement(alertDialog, false);
                }
            });
        }
        if (show)
            DrawerLayout.openDrawer(Gravity.LEFT);
        if (!isAuthenticated) {
            RealNameDialog realNameDialog = new RealNameDialog(getActivity(), false, pf);
            realNameDialog.show();
        }
        if (alertDialog.isShowing())
            alertDialog.dismiss();
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
        RoundView.getInstance().closeRoundView(getActivity());
        HttpManager.getInstance().loginOut(getActivity(), false);
        popupLoginCode();
        if (pf != null)
            pf.onDestroy();
        if (wp != null)
            wp.onDestroy();
        if (cs != null)
            cs.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        RoundView.getInstance().removeSmallWindow(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        RoundView.getInstance().closeRoundView(getActivity());
        if (alertDialog != null)
            alertDialog.dismiss();
    }

    //用户协议弹窗
    private void popupAgreement() {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.popup_agreement, null);
        Button popup_agree = inflate.findViewById(R.id.popup_agree);
        TextView popup_disagree = inflate.findViewById(R.id.popup_disagree);
        TextView privacyTxt = inflate.findViewById(R.id.txt_privacy);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(inflate);
        builder.setCancelable(false);
        AgreementDialog = builder.create();
        privacyTxt.setMovementMethod(LinkMovementMethod.getInstance());
        privacyTxt.setText("我们希望通过 ");
        SpannableString agreeTxt = new SpannableString("《用户协议》");
        SpannableString privacy = new SpannableString("《隐私政策》");
        agreeTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                UserAgreement(AgreementDialog, true);
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
                PrivacyAgreement(AgreementDialog, true);
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
        //显示popupWindow

        AgreementDialog.show();
        //同意
        popup_agree.setOnClickListener(v -> {
            AgreementDialog.dismiss();
            requestPermissions(Constants.PermissionString, 1);
        });
        //不同意协议
        popup_disagree.setOnClickListener(v -> System.exit(0));
    }

    //手机号验证码登录
    private void popupLoginCode() {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.popup_code, null);
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

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.pop_tel_list, null);
        spinnerImg.setOnClickListener(view -> {
            if (telLists.size() == 0)
                return;
            PopupTel popupTel = PopupTel.getInstance(getActivity(), telLists, popupLogin, v, inflate.getWidth(), 200, true, 1);
            popupLogin.post(() -> popupTel.showAsDropDown(popupLogin, 0, 0));
        });
        alertDialog.setContentView(inflate);
        if (!alertDialog.isShowing()) {
            alertDialog.setView(inflate);
            alertDialog.show();
        }
        //跳转注册
        popup_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupNumberRegister("", "");
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
                presenter.getPhoneLoginCode(getActivity(), popupLogin.getText().toString().trim());
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(popupTvCode, 60000, 1000);
                countDownTimerUtils.start();
            } else {
                Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            }
        });
        //用户协议
        popupUser.setOnClickListener(view -> UserAgreement(alertDialog, true));
        //隐私协议
        popupPrivacy.setOnClickListener(view -> PrivacyAgreement(alertDialog, true));
        popupSubmit.setOnClickListener(view -> {
            if (popupRb.isChecked()) {
                String number = popupLogin.getText().toString().trim();
                String code = popupEtCode.getText().toString().trim();
                presenter.getPhoneLogin(getActivity(), number, code);
            } else {
                Toast.makeText(getActivity(), "请先勾选用户协议", Toast.LENGTH_SHORT).show();
            }
        });

        //跳转
        popupLoginPw.setOnClickListener(view -> {
            popupLoginPw("", "");
        });


        play.setOnClickListener(view -> {
            presenter.getDemoAccount(getActivity(), new PlayInterface() {
                @Override
                public void onSuccess(String account, String password) {
                    popupNumberRegister(account, password);
                }

                @Override
                public void onError(String msg) {

                }
            });
        });
    }

    //账号密码登录弹窗
    private void popupLoginPw(String account, String password) {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.popup_pw_login, null);
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
        Button spinnerImg = inflate.findViewById(R.id.spinnerImg);
        TextView popup_forget_pw = inflate.findViewById(R.id.popup_forget_pw);
        alertDialog.setContentView(inflate);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.pop_tel_list, null);
        spinnerImg.setOnClickListener(view -> {
            if (accountLists.size() == 0)
                return;
            PopupTel popupTel = PopupTel.getInstance(getActivity(), accountLists, popupLogin, v, inflate.getWidth(), 200, true, 2);
            popupLogin.post(() -> popupTel.showAsDropDown(popupLogin, 0, 0));
        });
        popupLogin.setText(account);
        popup_et_pw.setText(password);
        //返回
        popup_back.setOnClickListener(view -> {
            popupLoginCode();
        });
        //立即注册
        popupRegister.setOnClickListener(view -> popupNumberRegister("", ""));
        //忘记密码
        popup_forget_pw.setOnClickListener(view -> {
            popupForgetPassword();
        });
        //账号密码登录
        popupSubmit.setOnClickListener(view -> {
            if (popupRb.isChecked()) {
                presenter.getLoginPwLo(getActivity(), popupLogin.getText().toString().trim(), popup_et_pw.getText().toString().trim());
            } else {
                Toast.makeText(getActivity(), "请先勾选用户协议", Toast.LENGTH_SHORT).show();
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
        popupUser.setOnClickListener(view -> UserAgreement(alertDialog, true));
        //隐私协议
        popupPrivacy.setOnClickListener(view -> PrivacyAgreement(alertDialog, true));
    }

    //验证手机号
    private void popupForgetPassword() {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.popup_code_phone, null);
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
        alertDialog.setContentView(inflate);
        //验证
        popupSubmit.setOnClickListener(view -> {
            if (popupRb.isChecked()) {
                String trim1 = popupLogin.getText().toString().trim();
                String trim2 = popupEtCode.getText().toString().trim();
                if (TextUtils.isEmpty(trim1) || TextUtils.isEmpty(trim2)) {
                    Toast.makeText(getActivity(), "请输入手机号与验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!DeviceIdUtil.isMobileNO(trim1)) {
                    Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                popupResetPassword(trim1, trim2);
            } else {
                Toast.makeText(getActivity(), "请先勾选用户协议", Toast.LENGTH_SHORT).show();
            }
        });
        //返回上一级
        popup_back.setOnClickListener(view -> {
            popupLoginPw("", "");
        });
        //跳转联系客服
        popup_service.setOnClickListener(view -> Toast.makeText(getActivity(), "尽请期待", Toast.LENGTH_SHORT).show());
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
                Toast.makeText(getActivity(), "请输入手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!DeviceIdUtil.isMobileNO(trim1)) {
                Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            presenter.forgetPwd(getActivity(), trim1, popupTvCode);
        });
    }

    //重置密码
    private void popupResetPassword(String n, String c) {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.popup_reset_password, null);
        ImageView popup_back = inflate.findViewById(R.id.popup_back);
        EditText popup_new_password = inflate.findViewById(R.id.popup_new_password);
        EditText popup_password_pw = inflate.findViewById(R.id.popup_password_pw);
        ImageView popup_remove = inflate.findViewById(R.id.popup_remove);
        ImageView popup_remove_pw_pw = inflate.findViewById(R.id.popup_remove_pw_pw);
        Button popupSubmit = inflate.findViewById(R.id.popup_submit);
        TextView popup_loginPw = inflate.findViewById(R.id.popup_loginPw);
        alertDialog.setContentView(inflate);

        //确认重置密码
        popupSubmit.setOnClickListener(view -> {
            String trim1 = popup_new_password.getText().toString().trim();
            String trim2 = popup_password_pw.getText().toString().trim();
            presenter.resetPwd(getActivity(), n, c, trim1, trim2);
        });
        //返回上一级
        popup_back.setOnClickListener(view -> {

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
            popupLoginCode();
        });
    }

    //账号密码注册弹窗
    private void popupNumberRegister(String user, String password) {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.popup_number_register, null);
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
        alertDialog.setContentView(inflate);
        popup_number.setText(user);
        popup_password.setText(password);
        popup_password_pw.setText(password);


        //返回
        popup_back.setOnClickListener(view -> {
            popupLoginCode();
        });
        popupSubmit.setOnClickListener(view -> {
            String number = popup_number.getText().toString().trim();
            String pass = popup_password.getText().toString().trim();
            String pass2 = popup_password_pw.getText().toString().trim();
            if (popupRb.isChecked()) {
                if (TextUtils.equals(pass, pass2)) {
                    presenter.getLoginPwRe(getActivity(), number, pass, pass2, alertDialog, getActivity());
                } else {
                    Toast.makeText(getActivity(), "两次密码不正确", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "请先勾选用户协议", Toast.LENGTH_SHORT).show();
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
        popupUser.setOnClickListener(view -> UserAgreement(alertDialog, true));
        //隐私协议
        popupPrivacy.setOnClickListener(view -> PrivacyAgreement(alertDialog, true));
    }

    //自动登录弹窗
    private void popupLoginAuto(String account, String password) {

        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.pop_login_tittle, null);
        TextView txt = inflate.findViewById(R.id.login_account);
        TextView btn = inflate.findViewById(R.id.login_switch);
        String tel = DBManager.getInstance(getActivity()).getAccount().getTel();
        txt.setText(TextUtils.isEmpty(tel) ? account.substring(0, 3) + "****" + account.substring(7, account.length()) :
                tel.substring(0, 3) + "****" + tel.substring(7, tel.length()));
        AutoBuilder.setView(inflate);
        AlertDialog autoDialog = AutoBuilder.create();
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
        presenter.getLoginPwLo(getActivity(), account, password);
        btn.setOnClickListener(v -> {
            autoDialog.dismiss();
            Switch();
        });
    }


    /**
     * 检查是否获取所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PackageManager packageManager = getActivity().getPackageManager();
        PermissionInfo permissionInfo = null;
        for (int i = 0; i < permissions.length; i++) {
            try {
                permissionInfo = packageManager.getPermissionInfo(permissions[i], 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            CharSequence permissionName = permissionInfo.loadLabel(packageManager);
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "您同意了【" + permissionName + "】权限");
            } else {
                Log.i(TAG, "您拒绝了【" + permissionName + "】权限");
            }
            if (i == permissions.length - 1) {
                popupLoginCode();
            }
        }
    }

    private void changeStyle(int style) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });

    }


    /**
     * 弹出用户协议
     */
    public void UserAgreement(AlertDialog dialog, boolean isShow) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        webView.setTittle("无限游戏用户协议");
        webView.setVisibility(View.VISIBLE);
        webView.btnListener(view -> {
            webView.setVisibility(View.INVISIBLE);
            if (isShow && !dialog.isShowing())
                dialog.show();
        });
        webView.setOnKey((view, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_BACK) {
                webView.setVisibility(View.INVISIBLE);
                if (isShow && !dialog.isShowing())
                    dialog.show();
                return true;
            }
            return false;
        });
        webView.show(Constants.REGISTER);
    }

    /**
     * 弹出隐私协议
     */
    public void PrivacyAgreement(AlertDialog dialog, boolean isShow) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        myWebView.setTittle("无限游戏用户隐私政策");
        myWebView.setVisibility(View.VISIBLE);
        myWebView.btnListener(view -> {
            myWebView.setVisibility(View.INVISIBLE);
            if (isShow && !dialog.isShowing())
                dialog.show();
        });
        myWebView.setOnKey((view, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_BACK) {
                myWebView.setVisibility(View.INVISIBLE);
                if (isShow && !dialog.isShowing())
                    dialog.show();
                return true;
            }
            return false;
        });
        myWebView.show(Constants.PRIVACY);
    }
}
