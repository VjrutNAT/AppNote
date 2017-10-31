package taro.rikkeisoft.com.assignment.fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;

import taro.rikkeisoft.com.assignment.R;
import taro.rikkeisoft.com.assignment.adapter.ImageAdapter;
import taro.rikkeisoft.com.assignment.base.BaseFragment;
import taro.rikkeisoft.com.assignment.model.Note;
import taro.rikkeisoft.com.assignment.utils.Constant;

/**
 * Created by VjrutNAT on 10/28/2017.
 */

public class EditNoteFragment extends BaseFragment {

    public static EditNoteFragment newInstance(Note itemNote, int lastNoteId){
        Bundle args = new Bundle();
        args.putSerializable(Constant.KEY_NOTE_DETAIL, itemNote);
        args.putInt(Constant.KEY_LAST_NOTE_ID, lastNoteId);
        EditNoteFragment editNoteFragment = new EditNoteFragment();
        editNoteFragment.setArguments(args);
        return editNoteFragment;
    }


    @Override
    protected int getLayout() {
        return R.layout.note_option_activity;
    }

    @Override
    protected void setUpTextViewAndDateTime() {

    }

    @Override
    protected void showImage(ArrayList listImage) {
        new LoadImageOfNote().execute();
    }

    @Override
    protected int getHomeAsUpIndicator() {
        return 0;
    }

    @Override
    protected int getIdNoteToSave() {
        return 0;
    }

    @Override
    protected void setAlarm() {

    }

    @Override
    protected void saveNote(Note itemNoteToSave) {

    }

    private class LoadImageOfNote extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            listImagePath  = (ArrayList<String>) mImageDAO.getListById(getIdNoteToSave());
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(Void aVoid) {
            mImageAdapter = new ImageAdapter(listImagePath, getActivity());
            rvImageList.setAdapter(mImageAdapter);
            listImagePathOld = (ArrayList<String>) listImagePath.clone();
            super.onPostExecute(aVoid);
        }
    }
}
