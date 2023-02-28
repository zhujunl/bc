package com.bc.sdk.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bc.sdk.db.entity.AccountEntity;
import com.bc.sdk.model.utility.Encryptutility;
import com.bc.sdk.db.MyDBHelper;

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
        values.put("account", Encryptutility.Base64Encode(account.getAccount()));
        values.put("tel", Encryptutility.Base64Encode(account.getTel()));
        values.put("slug", Encryptutility.Base64Encode(account.getSlug()));
        values.put("nickName", Encryptutility.Base64Encode(account.getNickName()));
        values.put("isAuthenticated", account.getAuthenticated());
        values.put("realName", Encryptutility.Base64Encode(account.getRealName()));
        values.put("birthday", Encryptutility.Base64Encode(account.getBirthday()));
        values.put("age", account.getAge());
        values.put("password", Encryptutility.Base64Encode(account.getPassword()));
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
                    .account(Encryptutility.Base64Decode(cursor.getString(1)))
                    .tel(Encryptutility.Base64Decode(cursor.getString(2)))
                    .slug(Encryptutility.Base64Decode(cursor.getString(3)))
                    .nickName(Encryptutility.Base64Decode(cursor.getString(4)))
                    .isAuthenticated(cursor.getInt(5) == 1)
                    .realName(Encryptutility.Base64Decode(cursor.getString(6)))
                    .birthday(Encryptutility.Base64Decode(cursor.getString(7)))
                    .age(cursor.getInt(8))
                    .password(Encryptutility.Base64Decode(cursor.getString(9)))
                    .build();
            return account;
        }

        return null;
    }
}
