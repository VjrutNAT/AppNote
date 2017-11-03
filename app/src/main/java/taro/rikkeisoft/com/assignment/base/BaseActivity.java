package taro.rikkeisoft.com.assignment.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import taro.rikkeisoft.com.assignment.R;
import taro.rikkeisoft.com.assignment.database.NoteDAO;
import taro.rikkeisoft.com.assignment.interfaces.OnBackPressedListener;

/**
 * Created by VjrutNAT on 10/28/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Context mContext;

    protected final NoteDAO mNoteDAO = NoteDAO.getInstance(mContext);

    private OnBackPressedListener mOnBackPressedListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        settingToolBar();
    }

    private void settingToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }

    public void setOnBackPressedListener(OnBackPressedListener mOnBackPressedListener){
        this.mOnBackPressedListener = mOnBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null){
            mOnBackPressedListener.doBack();
        }else {
            super.onBackPressed();
        }
    }

    protected abstract int getLayout();
}
