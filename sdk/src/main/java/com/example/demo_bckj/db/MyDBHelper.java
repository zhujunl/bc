package com.example.demo_bckj.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author ZJL
 * @date 2023/1/11 15:01
 * @des
 * @updateAuthor
 * @updateDes
 */
public class MyDBHelper extends SQLiteOpenHelper {
    private final static String DB_TABLE = "account";
    // 数据库文件名
    public static final String DB_NAME = "bc.db";

    // 数据库表名
    // 数据库版本号
    public static final int DB_VERSION = 1;

    private static MyDBHelper instance;

    public static MyDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MyDBHelper(context);
        }
        return instance;
    }

    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库表
        createDbTable(db, DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //数据库升级操作
        updateDatabase(db, oldVersion, newVersion, DB_TABLE);
    }

    private void updateDatabase(SQLiteDatabase db, int fromVersion, int toVersion, String tableName) {
        Log.d("updateDatabase: ", " tableName: " + tableName + " fromVersion: " + fromVersion + " toVersion: " + toVersion);
        if (fromVersion < toVersion) {
            //往表中增加一列
        }
    }

    /**
     * 创建数据库表
     *
     * @param db        数据实例
     * @param tableName 表名
     */
    private void createDbTable(SQLiteDatabase db, String tableName) {
        Log.d("createDatabase: ", "创建数据库表");
        db.execSQL("CREATE TABLE " + "account" + "(id integer PRIMARY KEY AUTOINCREMENT" +
                ",account varchar" +
                ",tel varchar" +
                ",slug varchar" +
                ",nickName varchar" +
                ",isAuthenticated integer" +
                ",realName varchar" +
                ",birthday varchar" +
                ",age integer" +
                ",password varchar" +
                ")");

        db.execSQL("CREATE TABLE " + "config" + "(id integer PRIMARY KEY AUTOINCREMENT" +
                ",authorization varchar" +
                ")");

        db.execSQL("CREATE TABLE "+"accountLogin" +"(id integer PRIMARY KEY AUTOINCREMENT "+
                ",account varchar"+
                ",password varchar"+
                ",time varchar"+
                ")");

        db.execSQL("CREATE TABLE "+"tel" +"(id integer PRIMARY KEY AUTOINCREMENT "+
                ",telNumber varchar"+
                ",time varchar"+
                ")");
    }
}

