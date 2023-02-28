package com.bc.sdk.manager;

import android.content.Context;
import android.text.TextUtils;

import com.bc.sdk.db.dao.AccountDao;
import com.bc.sdk.db.dao.AccountLoginDao;
import com.bc.sdk.db.dao.ConfigDao;
import com.bc.sdk.db.dao.TelDao;
import com.bc.sdk.db.entity.AccountEntity;
import com.bc.sdk.db.entity.AccountLoginEntity;
import com.bc.sdk.db.entity.ConfigEntity;
import com.bc.sdk.db.entity.TelEntity;
import com.bc.sdk.model.bean.AccountPwBean;

import java.util.List;

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
    private AccountLoginDao accountLoginDao;
    private TelDao telDao;

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }

    public DBManager(Context context) {
        dao = AccountDao.getInstance(context);
        configDao = ConfigDao.getInstance(context);
        accountLoginDao = AccountLoginDao.getInstance(context);
        telDao = TelDao.getInstance(context);
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

    public void insertAccount(String account, String password) {
        accountLoginDao.insertData(account, password);
    }

    public List<AccountLoginEntity> query() {
        return accountLoginDao.query();
    }

    public void deleteAccountLogin(AccountLoginEntity accountLoginEntity) {
        accountLoginDao.delete(accountLoginEntity);
    }

    public void insertTel(String tel) {
        telDao.insertData(tel);
    }

    public List<TelEntity> queryTel() {
        return telDao.query();
    }

    public void deleteTel(TelEntity entity) {
        telDao.delete(entity);
    }
}
