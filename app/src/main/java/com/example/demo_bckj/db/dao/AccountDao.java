package com.example.demo_bckj.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.demo_bckj.db.MyDBHelper;
import com.example.demo_bckj.db.entity.AccountEntity;

/**
 * @author ZJL
 * @date 2023/1/11 15:14
 * @des
 * @updateAuthor
 * @updateDes
 */
public class AccountDao {
    private MyDBHelper mHelper;
    private SQLiteDatabase mDatabase;
    private static AccountDao instance;

    public static AccountDao getInstance(Context context) {
        if (instance == null) {
            instance = new AccountDao(context);
        }
        return instance;
    }

    public AccountDao(Context context) {
        mHelper = MyDBHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertData(AccountEntity account) {
        delete();
        ContentValues values = new ContentValues();
        values.put("account", account.getAccount());
        values.put("tel", account.getTel());
        values.put("slug", account.getSlug());
        values.put("nickName", account.getNickName());
        values.put("isAuthenticated", account.getAuthenticated());
        values.put("realName", account.getRealName());
        values.put("birthday", account.getBirthday());
        values.put("age", account.getAge());
        values.put("password", account.getPassword());
        mDatabase.insert("account", null, values);
    }

    public void delete() {
        mDatabase.delete("account", "id > 0", new String[]{});
    }

    public AccountEntity query() {
        String sql = "select * from account";
        Cursor cursor = mDatabase.rawQuery(sql, null);

        if (cursor == null) {
            return null;
        }

        while (cursor.moveToNext()) {
            AccountEntity account = new AccountEntity.Builder()
                    .account(cursor.getString(1))
                    .tel(cursor.getString(2))
                    .slug(cursor.getString(3))
                    .nickName(cursor.getString(4))
                    .isAuthenticated(cursor.getInt(5) == 1)
                    .realName(cursor.getString(6))
                    .birthday(cursor.getString(7))
                    .age(cursor.getInt(8))
                    .password(cursor.getString(9))
                    .build();
            return account;
        }

        return null;
    }
}
