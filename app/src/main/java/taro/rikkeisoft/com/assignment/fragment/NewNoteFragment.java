package taro.rikkeisoft.com.assignment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;

import taro.rikkeisoft.com.assignment.R;
import taro.rikkeisoft.com.assignment.adapter.ImageAdapter;
import taro.rikkeisoft.com.assignment.base.BaseFragment;
import taro.rikkeisoft.com.assignment.model.Note;
import taro.rikkeisoft.com.assignment.utils.Constant;
import taro.rikkeisoft.com.assignment.utils.DateTimeUtils;

/**
 * A fragment extends base fragment and contains some other method for creating new note
 */

public class NewNoteFragment extends BaseFragment {

    public static final String TAG = NewNoteFragment.class.getName();


    public static NewNoteFragment newInstance(Note itemNote, int lastNoteId) {

        NewNoteFragment fragment = new NewNoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constant.KEY_NOTE_DETAIL, itemNote);
        args.putSerializable(Constant.KEY_LAST_NOTE_ID, lastNoteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_note;
    }

    @Override
    protected void saveNote(Note itemNoteToSave) {
        boolean result = mNoteDAO.insert(itemNoteToSave, getIdNoteToSave());

        if (result) {
            Toast.makeText(getActivity(), getString(R.string.success), Toast.LENGTH_SHORT).show();
            Intent intentRefreshHomeActivity = new Intent(Constant.ACTION_REFRESH_LIST);
            getActivity().sendBroadcast(intentRefreshHomeActivity);
        }

        for (int i = 0; i < listImagePath.size(); i++) {
            mImageDAO.insert(listImagePath.get(i), getIdNoteToSave());
        }
        getActivity().onBackPressed();
    }

    @Override
    protected void setUpTextViewAndDateTime() {
        tvCurrentTime.setText(DateTimeUtils.getCurrentDateTimeInStr(Constant.DATE_FORMAT));
        isFirstDateSpSelected = false;
        isFirstTimeSpSelected = false;
        selectedColor = ContextCompat.getColor(getActivity(), R.color.white);
        strDateSelection = DateTimeUtils.getCurrentDateTimeInStr(Constant.DATE_FORMAT);
        strTimeSelection = getString(R.string.sp_time_slot1);

    }

    @Override
    protected int getHomeAsUpIndicator() {
        return R.drawable.ic_arrow_back_black_24dp;
    }

    @Override
    protected void setAlarm() {
        setNotify();
    }

    @Override
    protected int getIdNoteToSave() {
        return mLastNoteId + 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void showImage(ArrayList lstImage) {
        lstImage = new ArrayList();
        mImageAdapter = new ImageAdapter(lstImage, getActivity());
        rvImageList.setAdapter(mImageAdapter);
    }

}
