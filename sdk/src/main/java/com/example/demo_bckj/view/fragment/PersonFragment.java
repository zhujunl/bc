package com.example.demo_bckj.view.fragment;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demo_bckj.R;
import com.example.demo_bckj.base.BaseFragment;
import com.example.demo_bckj.control.SDKListener;
import com.example.demo_bckj.control.SdkControl;
import com.example.demo_bckj.db.dao.AccountDao;
import com.example.demo_bckj.db.entity.AccountEntity;
import com.example.demo_bckj.manager.DBManager;
import com.example.demo_bckj.model.bean.RechargeOrder;
import com.example.demo_bckj.model.bean.RoleBean;
import com.example.demo_bckj.presenter.PersonPresenter;
import com.example.demo_bckj.view.dialog.BindNewPhoneDialog;
import com.example.demo_bckj.view.dialog.ModifyPWDialog;
import com.example.demo_bckj.view.dialog.RealNameDialog;
import com.example.demo_bckj.view.dialog.VerifyPhoneDialog;
import com.example.demo_bckj.view.round.MyWebView;

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

    public static PersonFragment getInstance(SDKListener sdkListener) {
        if (instance == null) {
            instance = new PersonFragment(sdkListener);
        }
        return instance;
    }

    public PersonFragment(SDKListener sdkListener) {
        this.sdkListener = sdkListener;
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

        Button p = v.findViewById(R.id.pay);
        Button l = v.findViewById(R.id.loginServer);
        Button c = v.findViewById(R.id.createRole);

        p.setOnClickListener(v -> {
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
        l.setOnClickListener(v -> {
            SdkControl.getInstance(getActivity()).CreateRole(getActivity(), new RoleBean.Builder()
                    .serverID("builder.serverID")
                    .serverName("builder.serverName")
                    .roleId("builder.roleId")
                    .roleName("builder.roleName").bulid());
        });
        c.setOnClickListener(v -> {
            SdkControl.getInstance(getActivity()).LoginServer(getActivity(), new RoleBean.Builder()
                    .serverID("builder.serverID")
                    .serverName("builder.serverName")
                    .roleId("builder.roleId")
                    .roleName("builder.roleName").bulid());
        });
        Button b = v.findViewById(R.id.payException);
        b.setOnClickListener(v -> {
            SdkControl.getInstance(getActivity()).Recharge(getActivity(), sdkListener, true);
        });
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
                verifyPhoneDialog.show();
            }
        });
        modifyPw.setOnClickListener(v -> {
            if (TextUtils.isEmpty(tel)) {
                //绑定手机号
                BindNewPhoneDialog bindNewPhoneDialog = new BindNewPhoneDialog(getActivity(), presenter);
                bindNewPhoneDialog.show();
                return;
            }
            //修改密码
            ModifyPWDialog modifyPWDialog = new ModifyPWDialog(getActivity(), presenter);
            modifyPWDialog.show();
        });
        realName.setOnClickListener(v -> {
            //实名认证
            RealNameDialog realNameDialog = new RealNameDialog(getActivity(), true, this);
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

    public void setListener(privacyListener listener) {
        this.listener = listener;
    }

    public privacyListener getListener() {
        return listener;
    }

    public interface privacyListener {
        void user();

        void privacy();
    }

}
