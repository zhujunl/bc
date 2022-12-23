package com.example.demo_bckj.view.fragment;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demo_bckj.R;
import com.example.demo_bckj.base.BaseFragment;
import com.example.demo_bckj.model.utility.SPUtils;
import com.example.demo_bckj.presenter.PersonPresenter;
import com.example.demo_bckj.view.dialog.BindNewPhoneDialog;
import com.example.demo_bckj.view.dialog.ModifyPWDialog;
import com.example.demo_bckj.view.dialog.PrivacyDialog;
import com.example.demo_bckj.view.dialog.RealNameDialog;
import com.example.demo_bckj.view.dialog.UserAgreeDialog;
import com.example.demo_bckj.view.dialog.VerifyPhoneDialog;

/**
 * @author ZJL
 * @date 2022/12/14 14:43
 * @des
 * @updateAuthor
 * @updateDes
 */
public class PersonFragment extends BaseFragment<PersonPresenter> {

    public static PersonFragment instance;
    private String TAG = "PersonFragment";
    private TextView account, phone, userMore;
    private RelativeLayout modifyPw, phoneRe, realName, userAgree, privacy;
    private Button quit;
    private SPUtils sp;
    private String tel, nickName;

    public static PersonFragment getInstance() {
        if (instance == null) {
            instance = new PersonFragment();
        }
        return instance;
    }

    public PersonFragment() {
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

        click();
    }


    @Override
    protected int initLayoutID() {
        return R.layout.fragment_person;
    }

    @Override
    protected PersonPresenter initPresenter() {
        return new PersonPresenter();
    }

    @Override
    public void onSuccess(Object o) {
        init();
    }

    @Override
    public void onError(String msg) {

    }

    private void init(){
        sp = SPUtils.getInstance(getContext(), "bcSP");
        nickName = sp.getString("nick_name", "");
        tel = sp.getString("tel", "");
        if (TextUtils.isEmpty(tel)) {
            account.setText(nickName);
            phone.setText("绑定");
            phone.setTextColor(0xFF5293FF);
        } else {
            String s = tel.substring(0, 3) + "****" + tel.substring(7, tel.length());
            account.setText(s);
            phone.setText(s);
        }
        boolean isAuthenticated = sp.getBoolean("is_authenticated", false);
        userMore.setText(isAuthenticated?"已认证":"未认证");
        userMore.setTextColor(isAuthenticated?0x999999:0xFF5293FF);
    }
//未成年充值提醒
//    RechargeDialog rechargeDialog = new RechargeDialog(getActivity());
//            rechargeDialog.show();

    private void click() {
        phoneRe.setOnClickListener(v -> {
            if (TextUtils.isEmpty(tel)) {
                //绑定手机号
                BindNewPhoneDialog bindNewPhoneDialog = new BindNewPhoneDialog(getActivity(),presenter);
                bindNewPhoneDialog.show();
            } else {
                //验证手机号
                VerifyPhoneDialog verifyPhoneDialog = new VerifyPhoneDialog(getActivity(),presenter);
                verifyPhoneDialog.show();
            }


        });
        modifyPw.setOnClickListener(v -> {
            if (TextUtils.isEmpty(tel)) {
                //绑定手机号
                BindNewPhoneDialog bindNewPhoneDialog = new BindNewPhoneDialog(getActivity(),presenter);
                bindNewPhoneDialog.show();
                return;
            }
            //修改密码
            ModifyPWDialog modifyPWDialog = new ModifyPWDialog(getActivity(),presenter);
            modifyPWDialog.show();
        });
        realName.setOnClickListener(v -> {
            //实名认证
            RealNameDialog realNameDialog = new RealNameDialog(getActivity(),presenter);
            realNameDialog.show();
        });
        userAgree.setOnClickListener(v -> {
            UserAgreeDialog userAgreeDialog = new UserAgreeDialog(getActivity());
            userAgreeDialog.show();
        });
        privacy.setOnClickListener(v -> {
            PrivacyDialog privacyDialog = new PrivacyDialog(getActivity());
            privacyDialog.show();
        });
        quit.setOnClickListener(v -> {
            presenter.loginOut(getContext());
        });
    }
}
