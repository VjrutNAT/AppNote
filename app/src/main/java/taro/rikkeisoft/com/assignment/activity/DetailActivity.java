package taro.rikkeisoft.com.assignment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import taro.rikkeisoft.com.assignment.R;
import taro.rikkeisoft.com.assignment.adapter.NoteViewPagerAdapter;
import taro.rikkeisoft.com.assignment.base.BaseActivity;
import taro.rikkeisoft.com.assignment.model.Note;
import taro.rikkeisoft.com.assignment.utils.Constant;

/**
 * Created by VjrutNAT on 10/25/2017.
 */

public class DetailActivity extends BaseActivity {

    private ViewPager mNoteViewPager;
    private ArrayList<Note> mNotes;
    private NoteViewPagerAdapter mNoteViewPagerAdapter;
    private int notePos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoteViewPager = (ViewPager) findViewById(R.id.vp_note);
        setupToolbar();
        getDataFromIntent();
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(mNotes.get(notePos).getTitle());
        }

        setUpViewPager();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_detail;
    }

    private void setupToolbar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    private void setUpViewPager(){
        mNoteViewPagerAdapter = new NoteViewPagerAdapter(getSupportFragmentManager(), mNotes);
        mNoteViewPager.setOffscreenPageLimit(3);
        mNoteViewPager.setAdapter(mNoteViewPagerAdapter);
        mNoteViewPager.setCurrentItem(notePos);
        mNoteViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (getSupportActionBar() != null){
                    getSupportActionBar().setTitle(mNoteViewPagerAdapter.getPageTitle(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getDataFromIntent(){
        Intent intent = getIntent();
        if (intent != null){
            mNotes = (ArrayList<Note>) intent.getExtras().getSerializable(Constant.KEY_LIST_NOTE);
            notePos = intent.getExtras().getInt(Constant.KEY_NOTE_POSITION);
        }
    }
}
