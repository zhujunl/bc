package com.example.demo_bckj.manager;

import android.content.Context;
import android.text.TextUtils;

import com.example.demo_bckj.db.dao.AccountDao;
import com.example.demo_bckj.db.dao.ConfigDao;
import com.example.demo_bckj.db.entity.AccountEntity;
import com.example.demo_bckj.db.entity.ConfigEntity;
import com.example.demo_bckj.model.bean.AccountPwBean;

/**
 * @author ZJL
 * @date 2023/1/11 14:46
 * @des
 * @updateAuthor
 * @updateDes
 */
public class DBManager {
    private String TAG = "DBManager";
    private static DBManager instance;
    private AccountDao dao;
    private ConfigDao configDao;

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }

    public DBManager(Context context) {
        dao = AccountDao.getInstance(context);
        configDao = ConfigDao.getInstance(context);
    }

    public AccountDao getDao() {
        return dao;
    }

    public void insertAccount(AccountPwBean data, String password) {
        AccountEntity account = new AccountEntity.Builder()
                .account(data.getData().getAccount())
                .tel(data.getData().getTel())
                .slug(data.getData().getSlug())
                .nickName(data.getData().getNickName())
                .isAuthenticated(data.getData().getAuthenticated())
                .realName(data.getData().getRealname())
                .birthday(data.getData().getBirthday())
                .build();
        if (data.getData().getAge() != null)
            account.setAge(data.getData().getAge());
        if (!TextUtils.isEmpty(password))
            account.setPassword(password);
        dao.insertData(account);
    }

    public void deleteAccount() {
        dao.delete();
    }

    public AccountEntity getAccount() {
        return dao.query();
    }

    public void BindPhone(String tel) {
        AccountEntity account = dao.query();
        account.setTel(tel);
        dao.insertData(account);
    }

    public void update(AccountEntity val) {
        AccountEntity account = getAccount();
        account.setAccount(val.getAccount());
        account.setTel(val.getTel());
        account.setSlug(val.getSlug());
        account.setNickName(val.getNickName());
        account.setAuthenticated(val.getAuthenticated());
        account.setRealName(val.getRealName());
        account.setBirthday(val.getBirthday());
        account.setAge(val.getAge());
        account.setPassword(val.getPassword());
        account.setAuthenticated(val.getAuthenticated());
        dao.insertData(account);
    }

    public void insertAuthorization(String authorization) {
        configDao.insertAuthorization(authorization);
    }

    public void deleteAuthorization() {
        configDao.deleteAuthorization();
    }


    public ConfigEntity getAuthorization() {
        return configDao.queryAuthorization();
    }

    public void delete() {
        dao.delete();
        configDao.deleteAuthorization();
    }
}
