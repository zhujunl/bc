package com.example.demo_bckj.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.demo_bckj.db.MyDBHelper;
import com.example.demo_bckj.db.entity.AccountLoginEntity;
import com.example.demo_bckj.model.utility.Encryptutility;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZJL
 * @date 2023/1/31 10:12
 * @des
 * @updateAuthor
 * @updateDes
 */
public class AccountLoginDao {
    private MyDBHelper mHelper;
    private SQLiteDatabase mDatabase;
    private static AccountLoginDao instance;

    public static AccountLoginDao getInstance(Context context) {
        if (instance == null) {
            instance = new AccountLoginDao(context);
        }
        return instance;
    }

    public AccountLoginDao(Context context) {
        mHelper = MyDBHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertData(String account, String password) {
        if (isExist(account, password)) {
            update(account, password);
            return;
        }
        ContentValues values = new ContentValues();
        values.put("account", Encryptutility.Base64Encode(account));
        values.put("password", Encryptutility.Base64Encode(password));
        values.put("time", System.currentTimeMillis());
        mDatabase.insert("accountLogin", null, values);
    }

    public void delete() {
        mDatabase.delete("accountLogin", "id > 0", new String[]{});
    }

    public void delete(AccountLoginEntity accountLoginEntity) {
        mDatabase.delete("accountLogin", "id = ?", new String[]{String.valueOf(accountLoginEntity.getId())});
    }

    public List<AccountLoginEntity> query() {
        String sql = "select * from accountLogin order by time desc";
        Cursor cursor = mDatabase.rawQuery(sql, null);
        List<AccountLoginEntity> lists = new ArrayList<>();
        if (cursor == null) {
            return null;
        }
        while (cursor.moveToNext()) {
            AccountLoginEntity account = new AccountLoginEntity.Builder()
                    .id(cursor.getInt(0))
                    .account(Encryptutility.Base64Decode(cursor.getString(1)))
                    .password(Encryptutility.Base64Decode(cursor.getString(2)))
                    .time(cursor.getLong(3))
                    .build();
            lists.add(account);
        }
        return lists;
    }

    private boolean isExist(String account, String password) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM accountLogin WHERE account= ? ",
                new String[]{Encryptutility.Base64Encode(account)});
        if (cursor == null) {
            return false;
        }
        while (cursor.moveToNext()) {
            AccountLoginEntity a = new AccountLoginEntity.Builder()
                    .id(cursor.getInt(0))
                    .account(Encryptutility.Base64Decode(cursor.getString(1)))
                    .password(Encryptutility.Base64Decode(cursor.getString(2)))
                    .time(cursor.getLong(3))
                    .build();
            return a != null;
        }
        return false;
    }

    public void update(String account, String password) {
        ContentValues values = new ContentValues();
        values.put("password", Encryptutility.Base64Encode(password));
        values.put("time", System.currentTimeMillis());
        mDatabase.update("accountLogin", values, " account = '" + Encryptutility.Base64Encode(account) + "'", null);
    }
}
