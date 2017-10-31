package taro.rikkeisoft.com.assignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import taro.rikkeisoft.com.assignment.interfaces.IDAOHandle;
import taro.rikkeisoft.com.assignment.model.Note;

/**
 * Created by VjrutNAT on 10/29/2017.
 */

public class NoteDAO implements IDAOHandle<Note, Integer> {

    private static NoteDAO mInstance;
    private final NoteDataBase mNoteDataBase;

    public static NoteDAO getInstance(Context context){
        if (mInstance == null){
            mInstance = new NoteDAO(context);
        }
        return mInstance;
    }

    private NoteDAO(Context context){
        mNoteDataBase = NoteDataBase.getInstance(context);
    }


    @Override
    public List<Note> getAllElement() {
        Cursor result = mNoteDataBase.rawQuery(NoteDataBase.QUERY_GET_ALL_NOTE);
        ArrayList<Note> mNotes = new ArrayList<>();
        if (result != null && result.moveToFirst()){
            do {
                int id = result.getInt(result.getColumnIndex(NoteDataBase.TBL_NOTE_COLUMN_ID));
                String title = result.getString(result.getColumnIndex(NoteDataBase.TBL_NOTE_COLUMN_TITLE));
                String content = result.getString(result.getColumnIndex(NoteDataBase.TBL_NOTE_COLUMN_CONTENT));
                int color = result.getInt(result.getColumnIndex(NoteDataBase.TBL_NOTE_COLUMN_COLOR));
                long createDateTime = result.getLong(result.getColumnIndex(NoteDataBase.TBL_NOTE_COLUMN_TIME));
                long notifyDateTime = result.getLong(result.getColumnIndex(NoteDataBase.TBL_NOTE_COLUMN_NOTIFY));

                Note note = new Note(id, title, content, createDateTime, notifyDateTime, color);
                mNotes.add(note);
            }while (result.moveToNext());
        }
        return mNotes;
    }

    @Override
    public List<Note> getListById(Integer id) {
        return null;
    }

    @Override
    public boolean insert(Note obj, Integer id) {
        ContentValues cv = new ContentValues();
        cv.put(NoteDataBase.TBL_NOTE_COLUMN_TITLE, obj.getTitle().trim());
        cv.put(NoteDataBase.TBL_NOTE_COLUMN_CONTENT, obj.getContent().trim());
        cv.put(NoteDataBase.TBL_NOTE_COLUMN_COLOR, obj.getColor());
        cv.put(NoteDataBase.TBL_NOTE_COLUMN_TIME, obj.getCreateAlarm());
        cv.put(NoteDataBase.TBL_NOTE_COLUMN_NOTIFY, obj.getNotifyAlarm());
        long result = mNoteDataBase.insertRecord(NoteDataBase.TBL_NOTE, cv);
        boolean isSuccess;
        isSuccess = (result > -1);
        return isSuccess;
    }

    @Override
    public boolean update(Note obj, Integer id) {
        ContentValues cv = new ContentValues();
        cv.put(NoteDataBase.TBL_NOTE_COLUMN_TITLE, obj.getTitle().trim());
        cv.put(NoteDataBase.TBL_NOTE_COLUMN_CONTENT, obj.getContent().trim());
        cv.put(NoteDataBase.TBL_NOTE_COLUMN_COLOR, obj.getColor());
        cv.put(NoteDataBase.TBL_NOTE_COLUMN_TIME, obj.getCreateAlarm());
        cv.put(NoteDataBase.TBL_NOTE_COLUMN_NOTIFY, obj.getNotifyAlarm());
        long result = mNoteDataBase.updateRecord(NoteDataBase.TBL_NOTE, cv, NoteDataBase.TBL_NOTE_COLUMN_ID, new String[] {String.valueOf(obj.getId())});
        boolean isSuccess;
        isSuccess = result > -1;
        return isSuccess;
    }

    @Override
    public boolean delete(Integer id) {
        long result = mNoteDataBase.deletRecord(NoteDataBase.TBL_NOTE, NoteDataBase.TBL_NOTE_COLUMN_ID, new String[] {String.valueOf(id)});
        boolean isSuccess;
        isSuccess = result > 0;
        return isSuccess;
    }
}
