package taro.rikkeisoft.com.assignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by VjrutNAT on 10/29/2017.
 */

public class NoteDataBase extends SQLiteOpenHelper {

    private static NoteDataBase mInstance;

    public static NoteDataBase getInstance(Context mContext){
        if (mInstance == null){
            mInstance = new NoteDataBase(mContext);
        }
        return mInstance;
    }

    private static final String DB_NAME = "NoteManagement.db";
    private static final int DB_VERSION = 1;

    public static final String TBL_NOTE = "NoteTable";
    public static final String TBL_NOTE_COLUMN_ID = "Id";
    public static final String TBL_NOTE_COLUMN_TITLE = "Title";
    public static final String TBL_NOTE_COLUMN_CONTENT = "Content";
    public static final String TBL_NOTE_COLUMN_COLOR = "Color";
    public static final String TBL_NOTE_COLUMN_TIME = "Time";
    public static final String TBL_NOTE_COLUMN_NOTIFY = "Notify";

    public static final String TBL_IMAGE = "ImageTable";
    public static final String TBL_IMAGE_COLUMN_ID = "ImageId";
    public static final String TBL_IMAGE_COLUMN_NOTE_ID = "Id";
    public static final String TBL_IMAGE_COLUMN_PATH = "ImagePath";

    public static final String SQL_DATE_FORMAT = "yyyy-MM-dd HH:mm";

    private static final String CREATE_TABLE_NOTE = "create table " + TBL_NOTE + "("
            + TBL_NOTE_COLUMN_ID + " integer primary key autoincrement,"
            + TBL_NOTE_COLUMN_TITLE + " text,"
            + TBL_NOTE_COLUMN_CONTENT + " text,"
            + TBL_NOTE_COLUMN_COLOR + " integer,"
            + TBL_NOTE_COLUMN_TIME + " integer ,"
            + TBL_NOTE_COLUMN_NOTIFY + " integer);";

    private static final String CREATE_TABLE_IMAGE = "create table " + TBL_IMAGE + "("
            + TBL_IMAGE_COLUMN_ID + " integer primary key autoincrement,"
            + TBL_IMAGE_COLUMN_NOTE_ID + " int,"
            + TBL_IMAGE_COLUMN_PATH + " text,"
            + "foreign key (" + TBL_IMAGE_COLUMN_NOTE_ID + ") references " + TBL_NOTE + "(" + TBL_NOTE_COLUMN_ID + ") "
            + "on update cascade on delete cascade);";

    public static final String QUERY_GET_ALL_NOTE = "select * from " + TBL_NOTE + " order by " + TBL_NOTE_COLUMN_ID + " asc;";
    public static final String QUERY_GET_IMAGE_WITH_NOTE_ID = "select * from " + TBL_IMAGE
            + " where " + TBL_IMAGE_COLUMN_NOTE_ID + "=";


    public NoteDataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTE);
        db.execSQL(CREATE_TABLE_IMAGE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public Cursor rawQuery(String sql){
        return getReadableDatabase().rawQuery(sql, null);
    }

    public long insertRecord(String tableName,ContentValues contentValues){
        return getWritableDatabase().insert(tableName, null, contentValues);
    }

    public long updateRecord(String tableName, ContentValues values, String columnId, String [] id){
        return getWritableDatabase().update(tableName, values,columnId + " =? ", id);
    }

    public long deletRecord(String tableName, String columnId, String [] id){
        return getWritableDatabase().delete(tableName, columnId + " =? ", id);
    }
}
