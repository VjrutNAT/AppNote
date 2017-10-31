package taro.rikkeisoft.com.assignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import taro.rikkeisoft.com.assignment.interfaces.IDAOHandle;

/**
 * Created by KHOE on 31/10/2017.
 */

public class ImageDAO implements IDAOHandle<String, Integer> {

    private static ImageDAO mInstance;
    private final NoteDataBase mNoteDataBase;

    public static ImageDAO getInstance(Context mContext){
        if (mInstance == null){
            mInstance = new ImageDAO(mContext);
        }
        return mInstance;
    }

    private ImageDAO(Context mContext){
        mNoteDataBase = NoteDataBase.getInstance(mContext);
    }

    @Override
    public List<String> getAllElement() {
        return null;
    }

    @Override
    public List<String> getListById(Integer id) {
        Cursor result = mNoteDataBase.rawQuery(NoteDataBase.QUERY_GET_IMAGE_WITH_NOTE_ID + id);
        ArrayList<String> list = new ArrayList<>();
        if (result != null && result.moveToFirst()){
            do {
                String path = result.getString(result.getColumnIndex(NoteDataBase.TBL_IMAGE_COLUMN_PATH));
                list.add(path);
            }while (result.moveToNext());
        }
        return list;
    }

    @Override
    public boolean insert(String path, Integer noteId) {
        ContentValues cv = new ContentValues();
        boolean isSuccess;
        cv.put(NoteDataBase.TBL_IMAGE_COLUMN_NOTE_ID, noteId);
        cv.put(NoteDataBase.TBL_IMAGE_COLUMN_PATH, path);
        long result = mNoteDataBase.insertRecord(NoteDataBase.TBL_IMAGE, cv);
        isSuccess = result > -1;
        return isSuccess;
    }

    @Override
    public boolean update(String obj, Integer id) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        boolean isSuccess;
        long result = mNoteDataBase.deletRecord(NoteDataBase.TBL_IMAGE, NoteDataBase.TBL_IMAGE_COLUMN_NOTE_ID, new String[] {String.valueOf(id)});
        isSuccess = result > 0;
        return false;
    }
}
