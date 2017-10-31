package taro.rikkeisoft.com.assignment.fragment;

import android.os.Bundle;

import taro.rikkeisoft.com.assignment.R;
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
    protected int getHomeAsUpIndicator() {
        return 0;
    }
}
