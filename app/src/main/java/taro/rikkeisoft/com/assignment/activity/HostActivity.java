package taro.rikkeisoft.com.assignment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import taro.rikkeisoft.com.assignment.R;
import taro.rikkeisoft.com.assignment.base.BaseActivity;
import taro.rikkeisoft.com.assignment.fragment.NewNoteFragment;
import taro.rikkeisoft.com.assignment.model.Note;
import taro.rikkeisoft.com.assignment.utils.Constant;

/**
 * Created by VjrutNAT on 10/31/2017.
 */

public class HostActivity extends BaseActivity{

    private Note itemNote;
    private int lastNoteId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NewNoteFragment newNoteFragment = NewNoteFragment.newInstance(itemNote, lastNoteId);
        showFragment(newNoteFragment, NewNoteFragment.TAG);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_host;
    }

    private void getDataFromIntent(){
        Intent intent = getIntent();
        itemNote = (Note) intent.getExtras().getSerializable(Constant.KEY_NOTE_DETAIL);
        lastNoteId = intent.getExtras().getInt(Constant.KEY_LAST_NOTE_ID);
    }

    private void showFragment(Fragment fragment, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.activity_host, fragment, tag);
        transaction.commit();
    }
}
