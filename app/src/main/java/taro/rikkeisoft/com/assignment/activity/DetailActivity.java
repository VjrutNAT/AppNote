package taro.rikkeisoft.com.assignment.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import taro.rikkeisoft.com.assignment.R;
import taro.rikkeisoft.com.assignment.adapter.NoteViewPagerAdapter;
import taro.rikkeisoft.com.assignment.base.BaseActivity;
import taro.rikkeisoft.com.assignment.base.BaseFragment;
import taro.rikkeisoft.com.assignment.model.Note;
import taro.rikkeisoft.com.assignment.utils.Constant;

/**
 * Created by VjrutNAT on 10/25/2017.
 */

public class DetailActivity extends BaseActivity implements View.OnClickListener{

    private ViewPager mNoteViewPager;
    private ArrayList<Note> mNotes;
    private NoteViewPagerAdapter mNoteViewPagerAdapter;
    private int notePos;
    private ImageView btDeleteNote, btShareNote, btNextLeftNote, btNextRightNote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setupToolbar();
        getDataFromIntent();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mNotes.get(notePos).getTitle());
        }
        initEvents();
        setUpViewPager();
    }

    private void initView() {
        mNoteViewPager = (ViewPager) findViewById(R.id.vp_note);
        btNextRightNote = (ImageView) findViewById(R.id.bt_next_right_note);
        btNextLeftNote = (ImageView) findViewById(R.id.bt_next_left_note);
        btShareNote = (ImageView) findViewById(R.id.bt_share_note);
        btDeleteNote = (ImageView) findViewById(R.id.bt_delete_note);
    }

    private void initEvents(){
        btDeleteNote.setOnClickListener(this);
        btShareNote.setOnClickListener(this);
        btNextRightNote.setOnClickListener(this);
        btNextLeftNote.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_detail;
    }

    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    private void setUpViewPager() {
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
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(mNoteViewPagerAdapter.getPageTitle(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mNotes = (ArrayList<Note>) intent.getExtras().getSerializable(Constant.KEY_LIST_NOTE);
            notePos = intent.getExtras().getInt(Constant.KEY_NOTE_POSITION);
        }
    }

    private void shareNote() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = mNotes.get(mNotes.size() - 1).getTitle().toString();
        String shareSub = mNotes.get(mNotes.size() - 1).getContent().toString();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_using)));
    }

    private void showConfirmDeleteNoteDialog(final int noteId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(getString(R.string.delete_note_question));
        builder.setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNoteDAO.delete(noteId);
                Intent intent = new Intent(Constant.ACTION_REFRESH_LIST);
                DetailActivity.this.sendBroadcast(intent);
                dialog.dismiss();
                DetailActivity.this.onBackPressed();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_share_note:
                shareNote();
                break;
            case R.id.bt_delete_note:
                showConfirmDeleteNoteDialog(notePos);
                break;
            case R.id.bt_next_left_note:
                mNoteViewPager.setCurrentItem(notePos--);
                break;
            case R.id.bt_next_right_note:
                mNoteViewPager.setCurrentItem(notePos++);
                break;
        }
    }
}
