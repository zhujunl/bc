package com.example.demo_bckj.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demo_bckj.R;
import com.example.demo_bckj.base.BaseFragment;
import com.example.demo_bckj.control.SDKListener;
import com.example.demo_bckj.db.dao.AccountDao;
import com.example.demo_bckj.db.entity.AccountEntity;
import com.example.demo_bckj.manager.DBManager;
import com.example.demo_bckj.presenter.PersonPresenter;
import com.example.demo_bckj.view.dialog.BindNewPhoneDialog;
import com.example.demo_bckj.view.dialog.ModifyPWDialog;
import com.example.demo_bckj.view.dialog.RealNameDialog;
import com.example.demo_bckj.view.dialog.VerifyPhoneDialog;
import com.example.demo_bckj.view.round.MyWebView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

/**
 * @author ZJL
 * @date 2022/12/14 14:43
 * @des 个人中心Fragment
 * @updateAuthor
 * @updateDes
 */
public class PersonFragment extends BaseFragment<PersonPresenter> {

    public static PersonFragment instance;
    private String TAG = "PersonFragment";
    private TextView account, phone, userMore;
    private RelativeLayout modifyPw, phoneRe, realName, userAgree, privacy;
    private Button quit;
    private String tel, nickName;
    private ImageView header;
    private SDKListener sdkListener;
    private MyWebView myWebView, webView;
    private privacyListener listener;
    private AccountDao dao;
    private DrawerLayout drawerLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    public static PersonFragment getInstance(SDKListener sdkListener, DrawerLayout drawerLayout) {
        return new PersonFragment(sdkListener, drawerLayout);
    }

    public PersonFragment(SDKListener sdkListener, DrawerLayout drawerLayout) {
        this.sdkListener = sdkListener;
        this.drawerLayout=drawerLayout;
    }

    @Override
    protected void initData() {
        init();
    }

    @Override
    protected void initView() {
        account = v.findViewById(R.id.userAccount);
        phone = v.findViewById(R.id.userPhone);
        userMore = v.findViewById(R.id.user_more);
        modifyPw = v.findViewById(R.id.modifyPwRe);
        realName = v.findViewById(R.id.RealNameRe);
        userAgree = v.findViewById(R.id.userAgreeRe);
        privacy = v.findViewById(R.id.PrivacyRe);
        quit = v.findViewById(R.id.user_quit_login);
        phoneRe = v.findViewById(R.id.phoneRe);
        header = v.findViewById(R.id.user_head);

        click();

    }

    @Override
    protected int initLayoutID() {
        return R.layout.fragment_person;
    }

    @Override
    protected PersonPresenter initPresenter() {
        return new PersonPresenter(sdkListener);
    }

    @Override
    public void onSuccess(Object o) {
        Log.d(TAG, "onSuccess");
        init();
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void Login(boolean isAccount) {

    }

    public void init() {
        dao = DBManager.getInstance(getActivity()).getDao();
        AccountEntity query = dao.query();
        nickName = query.getNickName();
        tel = query.getTel();
        if (TextUtils.isEmpty(tel)) {
            account.setText(nickName);
            phone.setText("绑定");
            phone.setTextColor(0xFF5293FF);
        } else {
            String s = tel.substring(0, 3) + "****" + tel.substring(7, tel.length());
            account.setText(s);
            phone.setText(s);
        }
        boolean isAuthenticated = query.getAuthenticated();
        if (isAuthenticated) {
            userMore.setText("已认证");
            userMore.setTextColor(0xFF999999);
            realName.setClickable(false);
        } else {
            userMore.setText("未认证");
            userMore.setTextColor(0xFF5293FF);
            realName.setClickable(true);
        }
    }

    private void click() {
        phoneRe.setOnClickListener(v -> {
            if (TextUtils.isEmpty(tel)) {
                //绑定手机号
                BindNewPhoneDialog bindNewPhoneDialog = new BindNewPhoneDialog(getActivity(), presenter);
                bindNewPhoneDialog.show();
            } else {
                //验证手机号
                VerifyPhoneDialog verifyPhoneDialog = new VerifyPhoneDialog(getActivity(), presenter);
                verifyPhoneDialog.setListener(listener);
                verifyPhoneDialog.show();
            }
        });
        modifyPw.setOnClickListener(v -> {
//            if (TextUtils.isEmpty(tel)) {
//                //绑定手机号
//                BindNewPhoneDialog bindNewPhoneDialog = new BindNewPhoneDialog(getActivity(), presenter);
//                bindNewPhoneDialog.show();
//                return;
//            }
            //修改密码
            ModifyPWDialog modifyPWDialog = new ModifyPWDialog(getActivity(), presenter);
            modifyPWDialog.show();
        });
        realName.setOnClickListener(v -> {
            //实名认证
            RealNameDialog realNameDialog = new RealNameDialog(getActivity(), true, this,null);
            realNameDialog.show();
        });
        userAgree.setOnClickListener(v -> {
            listener.user();
        });
        privacy.setOnClickListener(v -> {
            listener.privacy();
        });
        quit.setOnClickListener(v -> {
            presenter.loginOut(getContext());
        });
        header.setOnClickListener(v -> {

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "fragment  onDestroy");
        instance = null;
    }

    protected void finish() {
        if (fm.getBackStackEntryCount()>1){
            drawerLayout.closeDrawers();
            for (int i = 0; i < fm.getBackStackEntryCount()-1; i++) {
                fm.popBackStack("CService", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.popBackStack("Personal", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            return;
        }
        System.exit(0);
    }

    public void setListener(privacyListener listener) {
        this.listener = listener;
    }

    public privacyListener getListener() {
        return listener;
    }

    public interface privacyListener {
        void user();

        void privacy();

        void cs(Dialog dialog);
    }

}
