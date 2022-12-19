package com.example.demo_bckj.view.fragment;

import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demo_bckj.R;
import com.example.demo_bckj.base.BaseFragment;
import com.example.demo_bckj.presenter.PersonPresenter;
import com.example.demo_bckj.view.dialog.BindNewPhoneDialog;
import com.example.demo_bckj.view.dialog.ModifyPWDialog;
import com.example.demo_bckj.view.dialog.PrivacyDialog;
import com.example.demo_bckj.view.dialog.RealNameDialog;
import com.example.demo_bckj.view.dialog.RechargeDialog;
import com.example.demo_bckj.view.dialog.UserAgreeDialog;

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

    private String phoneNumber="18762742750";

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

        String s = phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, phoneNumber.length());
        account.setText(s);
        phone.setText(s);

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

    }

    @Override
    public void onError(String msg) {

    }

    private void click() {
        phoneRe.setOnClickListener(v -> {
            ////验证手机号
            //            VerifyPhoneDialog verifyPhoneDialog = new VerifyPhoneDialog(getActivity());
            //            verifyPhoneDialog.show();
            //绑定手机号
            BindNewPhoneDialog bindNewPhoneDialog = new BindNewPhoneDialog(getActivity());
            bindNewPhoneDialog.show();
        });
        modifyPw.setOnClickListener(v -> {
            //修改密码
            ModifyPWDialog modifyPWDialog=new ModifyPWDialog(getActivity());
            modifyPWDialog.show();
        });
        realName.setOnClickListener(v -> {
            RealNameDialog realNameDialog=new RealNameDialog(getActivity());
            realNameDialog.show();
        });
        userAgree.setOnClickListener(v -> {
            UserAgreeDialog userAgreeDialog=new UserAgreeDialog(getActivity());
            userAgreeDialog.show();
        });
         privacy.setOnClickListener(v -> {
             PrivacyDialog privacyDialog=new PrivacyDialog(getActivity());
             privacyDialog.show();
        });
         quit.setOnClickListener(v->{
             RechargeDialog rechargeDialog=new RechargeDialog(getActivity());
             rechargeDialog.show();
         });
    }
}
