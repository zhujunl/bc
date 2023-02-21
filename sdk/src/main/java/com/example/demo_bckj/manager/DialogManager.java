package com.example.demo_bckj.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.PixelFormat;
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
import android.util.TypedValue;
import android.view.Gravity;
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
import android.widget.TextView;

import com.example.demo_bckj.R;
import com.example.demo_bckj.db.entity.AccountEntity;
import com.example.demo_bckj.db.entity.AccountLoginEntity;
import com.example.demo_bckj.db.entity.TelEntity;
import com.example.demo_bckj.listener.ClickListener;
import com.example.demo_bckj.listener.DrawGoListener;
import com.example.demo_bckj.listener.LoginCallback;
import com.example.demo_bckj.listener.LogoutListener;
import com.example.demo_bckj.listener.PlayInterface;
import com.example.demo_bckj.listener.privacyListener;
import com.example.demo_bckj.model.utility.CountDownTimerUtils;
import com.example.demo_bckj.model.utility.DeviceIdUtil;
import com.example.demo_bckj.model.utility.SPUtils;
import com.example.demo_bckj.model.utility.ToastUtil;
import com.example.demo_bckj.presenter.HomePresenter;
import com.example.demo_bckj.view.Constants;
import com.example.demo_bckj.view.dialog.RealNameDialog;
import com.example.demo_bckj.view.fragment.AgreementActivity;
import com.example.demo_bckj.view.pop.PopupTel;
import com.example.demo_bckj.view.round.MyDrawerLayout;
import com.example.demo_bckj.view.round.RoundView;
import com.yanzhenjie.permission.AndPermission;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

/**
 * @author ZJL
 * @date 2023/2/15 17:25
 * @des
 * @updateAuthor
 * @updateDes
 */
public class DialogManager implements ClickListener, LogoutListener, LoginCallback, privacyListener {
    final String TAG = "DialogManager";
    private AlertDialog alertDialog, AgreementDialog;

    private HomePresenter presenter;

    private Activity activity;

    private SPUtils bcSP;

    private HandlerThread mHandlerThread;
    private Handler handler;

    private static DialogManager instance;

    public static DialogManager getInstance() {
        if (instance == null) {
            instance = new DialogManager();
        }
        return instance;
    }

    public DialogManager() {
    }

    public void init(Activity activity) {
        this.activity = activity;
        presenter = new HomePresenter(this);
        HttpManager.getInstance().setListener(this, this, activity);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        bcSP = SPUtils.getInstance(activity, "bcSP");
        createDrawLayout(activity);
        new Thread(() -> {
            try {
                boolean init = presenter.init(activity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        mHandlerThread = new HandlerThread("loginHandler");
        mHandlerThread.start();
        handler = new Handler(mHandlerThread.getLooper());
        handler.postDelayed(runnable, 50);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //用户协议弹窗
            boolean isFirstRun = bcSP.getBoolean("isFirst", false);
            if (!isFirstRun) {
                activity.runOnUiThread(() -> popupAgreement());
                presenter.getSdk(activity, false);
            } else {
                AccountEntity account = DBManager.getInstance(activity).getAccount();
                if (account != null && !TextUtils.isEmpty(account.getAccount()) && !TextUtils.isEmpty(account.getPassword())) {
                    activity.runOnUiThread(() -> presenter.getLoginPwLo(activity, account.getAccount(), account.getPassword()));
                } else {
                    boolean refresh = false;
                    try {
                        refresh = presenter.refreshToken(activity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!refresh) {
                        activity.runOnUiThread(() -> loginSelect(bcSP.getBoolean("isAccount")));
                    }
                }
            }
        }
    };

    private void loginSelect(boolean isAccount) {
        if (isAccount) {
            popupLoginPw("", "", true);
        } else {
            popupLoginCode(false);
        }
    }

    //用户协议弹窗
    public void popupAgreement() {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.popup_agreement, null);
        Button popup_agree = inflate.findViewById(R.id.popup_agree);
        TextView popup_disagree = inflate.findViewById(R.id.popup_disagree);
        TextView privacyTxt = inflate.findViewById(R.id.txt_privacy);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
                ds.setColor(activity.getResources().getColor(R.color.sky_blue));//设置颜色
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
                ds.setColor(activity.getResources().getColor(R.color.sky_blue));//设置颜色
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
            bcSP.put("isFirst", true);
            AgreementDialog.dismiss();
            AndPermission.with(activity)
                    .runtime()
                    .permission(Constants.PermissionString)
                    .onGranted(permissions -> {
                        // Storage permission are allowed.
                        Log.d("AndPermission", "onGranted");
                        PackageManager packageManager = activity.getPackageManager();
                        PermissionInfo permissionInfo = null;
                        for (int i = 0; i < permissions.size(); i++) {
                            try {
                                permissionInfo = packageManager.getPermissionInfo(permissions.get(i), 0);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            CharSequence permissionName = permissionInfo.loadLabel(packageManager);
                            if (i == permissions.size() - 1) {
                                presenter.getSdk(activity, true);
                                popupLoginCode(bcSP.getBoolean("isAccount"));
                            }
                        }
                    })
                    .onDenied(permissions -> {
                        // Storage permission are not allowed.
                        Log.d("AndPermission", "onGranted");
                    })
                    .start();
        });
        //不同意协议
        popup_disagree.setOnClickListener(v -> System.exit(0));
    }

    //手机号验证码登录
    private void popupLoginCode(boolean isAccount) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.popup_code, null);
        EditText popupLogin = inflate.findViewById(R.id.popup_login);
        EditText popupEtCode = inflate.findViewById(R.id.popup_Et_code);
        ImageView back = inflate.findViewById(R.id.popup_back);
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
        back.setVisibility(isAccount ? View.VISIBLE : View.INVISIBLE);
        View v = LayoutInflater.from(activity).inflate(R.layout.pop_tel_list, null);
        List<TelEntity> telEntities = DBManager.getInstance(activity).queryTel();
        if (telEntities.size() != 0) {
            popupLogin.setText(telEntities.get(0).getTelNumber());
        }
        spinnerImg.setOnClickListener(view -> {
            if (telEntities.size() == 0)
                return;
            PopupTel popupTel = new PopupTel(activity, telEntities, popupLogin, popupEtCode, v, inflate.getWidth(), 200, true);
            popupLogin.post(() -> popupTel.showAsDropDown(popupLogin, 0, 0));
        });
        alertDialog.setContentView(inflate);
        if (!alertDialog.isShowing()) {
            alertDialog.setView(inflate);
            alertDialog.show();
        }
        setDialogSize(alertDialog, 320, 295);
        back.setOnClickListener(v1 -> popupForgetPassword());

        //跳转注册
        popup_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                presenter.getPhoneLoginCode(activity, popupLogin.getText().toString().trim());
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(popupTvCode, 60000, 1000);
                countDownTimerUtils.start();
            } else {
                ToastUtil.show(activity, "请输入正确的手机号");
            }
        });
        //用户协议
        popupUser.setOnClickListener(view -> UserAgreement(alertDialog, true));
        //隐私协议
        popupPrivacy.setOnClickListener(view -> PrivacyAgreement(alertDialog, true));
        popupSubmit.setOnClickListener(view -> {
            if (Constants.isFastDoubleClick(activity)) {
                return;
            }
            if (popupRb.isChecked()) {
                String number = popupLogin.getText().toString().trim();
                String code = popupEtCode.getText().toString().trim();
                presenter.getPhoneLogin(activity, number, code);
            } else {
                ToastUtil.show(activity, "请先勾选用户协议");
            }
        });

        //跳转
        popupLoginPw.setOnClickListener(view -> {
            popupLoginPw("", "", isAccount);
        });


        play.setOnClickListener(view -> {
            presenter.getDemoAccount(activity, new PlayInterface() {
                @Override
                public void onSuccess(String account, String password) {
                    popupNumberRegister(account, password, false);
                }

                @Override
                public void onError(String msg) {

                }
            });
        });
    }

    //账号密码登录弹窗
    private void popupLoginPw(String account, String password, boolean isAccount) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.popup_pw_login, null);
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
        if (!alertDialog.isShowing()) {
            alertDialog.setView(inflate);
            alertDialog.show();
        }
        setDialogSize(alertDialog, 320, 295);
        popup_back.setVisibility(isAccount ? View.INVISIBLE : View.VISIBLE);
        View v = LayoutInflater.from(activity).inflate(R.layout.pop_tel_list, null);
        List<AccountLoginEntity> query = DBManager.getInstance(activity).query();
        spinnerImg.setOnClickListener(view -> {
            if (query.size() == 0)
                return;
            PopupTel popupTel = new PopupTel(activity, query,
                    popupLogin, popup_et_pw, v, inflate.getWidth(), 200, true);
            popupLogin.post(() -> popupTel.showAsDropDown(popupLogin, 0, 0));
        });
        if (query.size() != 0) {
            popupLogin.setText(query.get(0).getAccount());
            popup_et_pw.setText(query.get(0).getPassword());
        }
        //返回
        popup_back.setOnClickListener(view -> {
            popupLoginCode(bcSP.getBoolean("isAccount"));
        });
        //立即注册
        popupRegister.setOnClickListener(view -> popupNumberRegister("", "", true));
        //忘记密码
        popup_forget_pw.setOnClickListener(view -> {
            popupForgetPassword();
        });
        //账号密码登录
        popupSubmit.setOnClickListener(view -> {
            if (Constants.isFastDoubleClick(activity)) {
                return;
            }
            if (popupRb.isChecked()) {
                presenter.getLoginPwLo(activity, popupLogin.getText().toString().trim(), popup_et_pw.getText().toString().trim());
            } else {
                ToastUtil.show(activity, "请先勾选用户协议");
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
        View inflate = LayoutInflater.from(activity).inflate(R.layout.popup_code_phone, null);
        EditText popupLogin = inflate.findViewById(R.id.popup_login);
        EditText popupEtCode = inflate.findViewById(R.id.popup_Et_code);
        TextView popupTvCode = inflate.findViewById(R.id.popup_Tv_code);
        //        TextView popup_register =  inflate.findViewById(R.id.popup_register);
        ImageView popup_remove = inflate.findViewById(R.id.popup_remove);
        ImageView popup_back = inflate.findViewById(R.id.popup_back);
        ImageView popup_remove_code = inflate.findViewById(R.id.popup_remove_code);
        Button popupSubmit = inflate.findViewById(R.id.popup_submit);
        TextView popup_service = inflate.findViewById(R.id.popup_service);
        TextView popup_loginPw = inflate.findViewById(R.id.popup_loginPw);
        alertDialog.setContentView(inflate);
        setDialogSize(alertDialog, 320, 285);
        //验证
        popupSubmit.setOnClickListener(view -> {
            if (Constants.isFastDoubleClick(activity)) {
                return;
            }
            String trim1 = popupLogin.getText().toString().trim();
            String trim2 = popupEtCode.getText().toString().trim();
            if (TextUtils.isEmpty(trim1) || TextUtils.isEmpty(trim2)) {
                ToastUtil.show(activity, "请输入手机号与验证码");
                return;
            }
            if (!DeviceIdUtil.isMobileNO(trim1)) {
                ToastUtil.show(activity, "请输入正确的手机号");
                return;
            }
            popupResetPassword(trim1, trim2);
        });
        //返回上一级
        popup_back.setOnClickListener(view -> {
            popupLoginPw("", "", bcSP.getBoolean("isAccount"));
        });
        //跳转联系客服
        popup_service.setOnClickListener(view -> CustomerServer(alertDialog, true));
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
        popup_loginPw.setOnClickListener(view -> popupLoginCode(bcSP.getBoolean("isAccount")));
        popupTvCode.setOnClickListener(view -> {
            String trim1 = popupLogin.getText().toString().trim();
            if (TextUtils.isEmpty(trim1)) {
                ToastUtil.show(activity, "请输入手机号");
                return;
            }
            if (!DeviceIdUtil.isMobileNO(trim1)) {
                ToastUtil.show(activity, "请输入正确的手机号");
                return;
            }
            presenter.forgetPwd(activity, trim1, popupTvCode);
        });
    }

    //重置密码
    private void popupResetPassword(String n, String c) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.popup_reset_password, null);
        ImageView popup_back = inflate.findViewById(R.id.popup_back);
        EditText popup_new_password = inflate.findViewById(R.id.popup_new_password);
        EditText popup_password_pw = inflate.findViewById(R.id.popup_password_pw);
        ImageView popup_remove = inflate.findViewById(R.id.popup_remove);
        ImageView popup_remove_pw_pw = inflate.findViewById(R.id.popup_remove_pw_pw);
        Button popupSubmit = inflate.findViewById(R.id.popup_submit);
        TextView popup_loginPw = inflate.findViewById(R.id.popup_loginPw);
        alertDialog.setContentView(inflate);
        setDialogSize(alertDialog, 320, 270);
        //确认重置密码
        popupSubmit.setOnClickListener(view -> {
            if (Constants.isFastDoubleClick(activity)) {
                return;
            }
            String trim1 = popup_new_password.getText().toString().trim();
            String trim2 = popup_password_pw.getText().toString().trim();
            presenter.resetPwd(activity, n, c, trim1, trim2);
        });
        //返回上一级
        popup_back.setOnClickListener(view -> {
            popupForgetPassword();
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
            popupLoginCode(bcSP.getBoolean("isAccount"));
        });
    }

    //账号密码注册弹窗
    private void popupNumberRegister(String user, String password, boolean isAccount) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.popup_number_register, null);
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
        setDialogSize(alertDialog, 320, 320);
        popup_number.setText(user);
        popup_password.setText(password);
        popup_password_pw.setText(password);


        //返回
        popup_back.setOnClickListener(view -> {
            if (isAccount) {
                popupLoginPw("", "", bcSP.getBoolean("isAccount"));
            } else {
                popupLoginCode(bcSP.getBoolean("isAccount"));
            }
        });
        popupSubmit.setOnClickListener(view -> {
            if (Constants.isFastDoubleClick(activity)) {
                return;
            }
            String number = popup_number.getText().toString().trim();
            String pass = popup_password.getText().toString().trim();
            String pass2 = popup_password_pw.getText().toString().trim();
            if (popupRb.isChecked()) {
                if (TextUtils.equals(pass, pass2)) {
                    presenter.getLoginPwRe(activity, number, pass, pass2, alertDialog, activity);
                } else {
                    ToastUtil.show(activity, "两次密码不正确");
                }
            } else {
                ToastUtil.show(activity, "请先勾选用户协议");
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

    private void setDialogSize(AlertDialog dialog, int width, int height) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        lp.width = dp2px(activity, width);
        lp.height = dp2px(activity, height);
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
    }

    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 弹出用户协议
     */
    public void UserAgreement(AlertDialog dialog, boolean isDialog) {
        showWeb(dialog, isDialog, "user");
    }

    /**
     * 弹出隐私协议
     */
    public void PrivacyAgreement(AlertDialog dialog, boolean isDialog) {
        showWeb(dialog, isDialog, "privacy");
    }

    /**
     * 客服网页
     */
    public void CustomerServer(Dialog dialog, boolean isDialog) {
        showWeb(dialog, isDialog, "customer");
    }


    public void showWeb(Dialog dialog, boolean isDialog, String tag) {

        Intent intent = new Intent(activity, AgreementActivity.class);
        intent.putExtra("style", tag);
        activity.startActivity(intent);
    }

    private boolean isAdd = false;

    @Override
    public void CService(boolean isShow) {
        Log.d(TAG, "CService");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    if (!isAdd) {
                        windowManager.addView(myDrawerLayout, mLayoutParams);
                    }
                    isAdd = true;
                    myDrawerLayout.changeStyle(1);
                }
            }
        });
    }

    @Override
    public void Personal(boolean isShow, boolean isAuthenticated) {
        Log.d(TAG, "Personal");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing())
                    alertDialog.dismiss();
                if (!isAuthenticated) {
                    RealNameDialog realNameDialog = new RealNameDialog(activity, false, myDrawerLayout, DialogManager.this);
                    realNameDialog.show();
                }
                if (isShow) {
                    if (!isAdd) {
                        windowManager.addView(myDrawerLayout, mLayoutParams);
                    }
                    isAdd = true;
                    myDrawerLayout.changeStyle(0);
                }
            }
        });

    }

    @Override
    public void Welfare(boolean isShow) {

    }

    @Override
    public void Switch() {
        Log.d(TAG, "Switch");
        activity.runOnUiThread(() -> {
            RoundView.getInstance().closeRoundView(activity);
            HttpManager.getInstance().loginOut(activity, false, true);
            loginSelect(bcSP.getBoolean("isAccount"));
        });
    }

    @Override
    public void out(boolean isLoginShow) {
        mDrawGoListener.go();
        RoundView.getInstance().closeRoundView(activity);
        if (isLoginShow)
            activity.runOnUiThread(() -> loginSelect(bcSP.getBoolean("isAccount")));
    }

    @Override
    public void login(boolean isAccount) {
        loginSelect(isAccount);
    }

    MyDrawerLayout myDrawerLayout;
    WindowManager windowManager;
    WindowManager.LayoutParams mLayoutParams;
    DrawerLayout drawerLayout;

    DrawGoListener mDrawGoListener=new DrawGoListener() {
        @Override
        public void go() {
            if (isAdd){
                windowManager.removeView(myDrawerLayout);
                isAdd = false;
            }
        }
    };

    private void createDrawLayout(Activity activity) {
        windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.format = PixelFormat.RGBA_8888;// 解决带Alpha的32位png图片失真问题
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.gravity = Gravity.LEFT;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG; //设置悬浮窗的层次
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.x = 0;
        myDrawerLayout = new MyDrawerLayout(activity);
        drawerLayout = myDrawerLayout.getDrawerLayout();
        myDrawerLayout.setListener(this);
        myDrawerLayout.setGoListener(mDrawGoListener);
        //        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                Log.d(TAG, "drawerLayout==onDrawerSlide");
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                Log.d(TAG, "drawerLayout==onDrawerOpened");
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                Log.d(TAG, "drawerLayout==onDrawerClosed");
                windowManager.removeView(myDrawerLayout);
                isAdd = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.d(TAG, "drawerLayout==onDrawerStateChanged newState==" + newState);
            }
        });

    }

    @Override
    public void user() {
        Log.d(TAG, "user");
        Intent intent = new Intent(activity, AgreementActivity.class);
        intent.putExtra("style", "user");
        activity.startActivity(intent);
    }

    @Override
    public void privacy() {
        Log.d(TAG, "privacy");
        Intent intent = new Intent(activity, AgreementActivity.class);
        intent.putExtra("style", "privacy");
        activity.startActivity(intent);
    }

    @Override
    public void cs(Dialog dialog) {
        Log.d(TAG, "cs");
        Intent intent = new Intent(activity, AgreementActivity.class);
        intent.putExtra("style", "customer");
        activity.startActivity(intent);
    }

    public void cancellation() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        RoundView.getInstance().closeRoundView(activity);
    }
}
