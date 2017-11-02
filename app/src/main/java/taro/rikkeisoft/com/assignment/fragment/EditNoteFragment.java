package taro.rikkeisoft.com.assignment.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import taro.rikkeisoft.com.assignment.R;
import taro.rikkeisoft.com.assignment.adapter.ImageAdapter;
import taro.rikkeisoft.com.assignment.base.BaseFragment;
import taro.rikkeisoft.com.assignment.model.Note;
import taro.rikkeisoft.com.assignment.utils.Common;
import taro.rikkeisoft.com.assignment.utils.Constant;
import taro.rikkeisoft.com.assignment.utils.DateTimeUtils;

/**
 * Created by VjrutNAT on 10/28/2017.
 */

public class EditNoteFragment extends BaseFragment {

    private ImageView btShareNote;
    private ImageView btDeleteNote;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayout(), container, false);

        btDeleteNote = v.findViewById(R.id.bt_delete_note);
        btShareNote = v.findViewById(R.id.bt_share_note);

        btShareNote.setOnClickListener(this);
        btDeleteNote.setOnClickListener(this);
        return v;
    }

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
        return R.layout.edit_note_fragment;
    }

    @Override
    protected void setUpTextViewAndDateTime() {

        etContent.setText(mItemNote.getContent());
        etTitle.setText(mItemNote.getTitle());
        tvCurrentTime.setText(DateTimeUtils.getDateStrFromMilliseconds(mItemNote.getCreateAlarm(), Constant.DATE_FORMAT));
        strDateSelection = DateTimeUtils.getDateStrFromMilliseconds(mItemNote.getNotifyAlarm(), Constant.DATE_FORMAT);
        strTimeSelection = DateTimeUtils.getDateStrFromMilliseconds(mItemNote.getNotifyAlarm(), Constant.TIME_FORMAT);
        Common.writeLog("date", mItemNote.getNotifyAlarm() + "");
        Common.writeLog("date", strDateSelection + "/" + strTimeSelection);
        isFirstDateSpSelected = true;
        isFirstTimeSpSelected = true;
        selectedColor = mItemNote.getColor();
        ivBackGroup.setBackgroundColor(mItemNote.getColor());
        listTime.remove(3);
        listTime.add(strTimeSelection);
        spTime.setSelection(3);
        listDate.remove(3);
        listDate.add(strDateSelection);
        spDate.setSelection(3);
    }


    @Override
    protected void showImage(ArrayList listImage) {
        new LoadImageOfNote().execute();
    }

    @Override
    protected int getHomeAsUpIndicator() {
        return R.drawable.ic_arrow_back_black_24dp;
    }

    @Override
    protected int getIdNoteToSave() {
        return (int) mItemNote.getId();
    }

    @Override
    protected void setAlarm() {
        cancelNotify();
        setNotify();
    }

    @Override
    protected void saveNote(Note itemNoteToSave) {

        boolean result = mNoteDAO.update(itemNoteToSave, getIdNoteToSave());
        if (result){
            Toast.makeText(getActivity(), getString(R.string.success), Toast.LENGTH_SHORT).show();
            Intent intentRefreshHomeActivity  = new Intent(Constant.ACTION_REFRESH_LIST);
            getActivity().sendBroadcast(intentRefreshHomeActivity);
        }
        mImageDAO.delete(getIdNoteToSave());
        for (int i = 0; i < listImagePath.size(); i++){
            mImageDAO.insert(listImagePath.get(i), getIdNoteToSave());
        }
        getActivity().onBackPressed();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_share_note:
                shareNote();
                break;
            case R.id.bt_delete_note:
                showConfirmDeleteNoteDialog(getIdNoteToSave());
                break;
        }
    }

    private void shareNote() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = etContent.getText().toString();
        String shareSub = etTitle.getText().toString();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_using)));
    }

    private void showConfirmDeleteNoteDialog(final int noteId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(getString(R.string.delete_note_question));
        builder.setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNoteDAO.delete(noteId);
                Intent intent = new Intent(Constant.ACTION_REFRESH_LIST);
                getActivity().sendBroadcast(intent);
                dialog.dismiss();
                cancelNotify();
                getActivity().onBackPressed();
            }
        });
        builder.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
