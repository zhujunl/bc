package com.example.demo_bckj.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.demo_bckj.db.MyDBHelper;
import com.example.demo_bckj.db.entity.TelEntity;
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
public class TelDao {
    private MyDBHelper mHelper;
    private SQLiteDatabase mDatabase;
    private static TelDao instance;

    public static TelDao getInstance(Context context) {
        if (instance == null) {
            instance = new TelDao(context);
        }
        return instance;
    }

    public TelDao(Context context) {
        mHelper = MyDBHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertData(String tel) {
        if (isExist(tel)) {
            update(tel);
            return;
        }
        ContentValues values = new ContentValues();
        values.put("telNumber", Encryptutility.Base64Encode(tel));
        values.put("time", System.currentTimeMillis());
        mDatabase.insert("tel", null, values);
    }

    public void delete() {
        mDatabase.delete("tel", "id > 0", new String[]{});
    }

    public void delete(TelEntity entity) {
        mDatabase.delete("tel", "id = ?", new String[]{String.valueOf(entity.getId())});
    }

    public List<TelEntity> query() {
        String sql = "select * from tel order by time desc";
        Cursor cursor = mDatabase.rawQuery(sql, null);
        List<TelEntity> lists = new ArrayList<>();
        if (cursor == null) {
            return null;
        }
        while (cursor.moveToNext()) {
            TelEntity account = new TelEntity();
            account.setId(cursor.getInt(0));
            account.setTelNumber(Encryptutility.Base64Decode(cursor.getString(1)));
            account.setTime(cursor.getLong(2));
            lists.add(account);
        }
        return lists;
    }

    public void update(String tel) {
        ContentValues values = new ContentValues();
        values.put("time", System.currentTimeMillis());
        mDatabase.update("tel", values, " telNumber = '" + Encryptutility.Base64Encode(tel) + "'", null);
    }

    public boolean isExist(String tel) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM tel WHERE telNumber= ? ", new String[]{Encryptutility.Base64Encode(tel)});
        if (cursor == null) {
            return false;
        }
        while (cursor.moveToNext()) {
            TelEntity a = new TelEntity();
            a.setId(cursor.getInt(0));
            a.setTelNumber(Encryptutility.Base64Decode(cursor.getString(1)));
            a.setTime(cursor.getLong(2));
            return a != null;
        }
        return false;
    }
}
