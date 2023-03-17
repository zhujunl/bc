package com.bc.sdk.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.sdk.base.BasePresenter;
import com.bc.sdk.listener.PfRefreshCallBack;
import com.bc.sdk.manager.HttpManager;
import com.bc.sdk.model.utility.DeviceIdUtil;
import com.bc.sdk.model.utility.StrUtil;
import com.bc.sdk.model.utility.ToastUtil;
import com.bc.sdk.view.Constants;
import com.bc.sdk.view.dialog.BindNewPhoneDialog;
import com.bc.sdk.view.dialog.ModifyPWDialog;
import com.bc.sdk.view.dialog.RealNameDialog;
import com.bc.sdk.view.dialog.VerifyPhoneDialog;

/**
 * @author ZJL
 * @date 2022/12/14 14:45
 * @des
 * @updateAuthor
 * @updateDes
 */
public class PersonPresenter extends BasePresenter {

    //验证手机
        public void checkCode(Context context,TextView phone,TextView code,VerifyPhoneDialog verifyPhoneDialog,BindNewPhoneDialog bindDialog){
        if (Constants.isFastDoubleClick(context)){
            return;
        }
        if (TextUtils.isEmpty(phone.getText().toString().trim())||TextUtils.isEmpty(code.getText().toString().trim())
                ||!DeviceIdUtil.isMobileNO(phone.getText().toString().trim())){
            Toast.makeText(context, "请输入手机号与验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bindDialog==null){
            bindDialog = new BindNewPhoneDialog(context, verifyPhoneDialog, this);
        }
        bindDialog.set(code.getText().toString().trim());
        verifyPhoneDialog.hide();
        bindDialog.show();
    }

    //绑定手机提交
    public void submitBindPhone(Context context,TextView phone,TextView code,String codeOld,VerifyPhoneDialog dialog,BindNewPhoneDialog bindNewPhoneDialog){
        if (Constants.isFastDoubleClick(context)){
            return;
        }
        if (TextUtils.isEmpty(code.getText().toString().trim())){
            Toast.makeText(context, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dialog==null){
            HttpManager.getInstance().BindPhone(context, phone.getText().toString().trim(), code.getText().toString().trim(), bindNewPhoneDialog, this);
        }else {
            HttpManager.getInstance().modifyBindPhone(context, codeOld, code.getText().toString().trim(), phone.getText().toString().trim() ,dialog, bindNewPhoneDialog, this);
        }
    }

    //获取手机验证码
    public void getPhoneCode(Context context,TextView phone,TextView TCode,VerifyPhoneDialog dialog){
        String tel = phone.getText().toString().trim();
        boolean mobileNO = DeviceIdUtil.isMobileNO(tel);
        if (!mobileNO) {
            Toast.makeText(context, "请输入合法手机号", Toast.LENGTH_SHORT).show();
        } else {
            //获取短信
            if (dialog==null) {
                HttpManager.getInstance().BindPhoneCode(context, tel, TCode);
            }else {
                HttpManager.getInstance().modifyBindCode(context, tel, TCode);
            }
        }
    }

    //修改密码提交
    public void submitModifyPW(Context context, EditText pw,EditText newPw,EditText newPwA,ModifyPWDialog dialog){
        if (Constants.isFastDoubleClick(context)){
            return;
        }
        String trim = pw.getText().toString().trim();
        String trim1 = newPw.getText().toString().trim();
        String trim2 = newPwA.getText().toString().trim();
        if (!TextUtils.equals(trim1, trim2)) {
            ToastUtil.show(context, "两次密码不一致");
        } else {
            HttpManager.getInstance().modifyPwd(context, trim, trim1, trim2, dialog);
        }
    }

    //忘记密码
    public void forgetPW(Context context,EditText popupLogin,TextView popupTvCode){
        String trim1 = popupLogin.getText().toString().trim();
        if (TextUtils.isEmpty(trim1)) {
            ToastUtil.show(context, "请输入手机号");
            return;
        }
        if (!DeviceIdUtil.isMobileNO(trim1)) {
            ToastUtil.show(context, "请输入正确的手机号");
            return;
        }
        HttpManager.getInstance().forgetPwd(context, trim1, popupTvCode);
    }

    //实名认证提交
    public void submitRealName(Context context, EditText name, EditText code, RealNameDialog dialog, PfRefreshCallBack callBack){
        String n = name.getText().toString().trim();
        String c = code.getText().toString().trim();
        String s = StrUtil.extractChinese(n);
        if (TextUtils.isEmpty(s)) {
            ToastUtil.show(context, "请输入中文");
            return;
        }
        if (!StrUtil.isCard(c)) {
            ToastUtil.show(context, "请输入正确身份证号码");
            return;
        }
        HttpManager.getInstance().setRealName(context, c, n,dialog,callBack);
    }

    //换绑手机-发送验证码到原手机
    public void modifyBind(Context context, TextView phone, TextView tCode) {
        boolean mobileNO = DeviceIdUtil.isMobileNO(phone.getText().toString().trim());
        if (!mobileNO) {
            ToastUtil.show(context, "请输入合法手机号");
        } else {
            //获取短信
            HttpManager.getInstance().modifyBind(context, tCode);
        }

    }

    public void loginOut(Context context) {
        HttpManager.getInstance().loginOut(context, false, true);
    }
}
