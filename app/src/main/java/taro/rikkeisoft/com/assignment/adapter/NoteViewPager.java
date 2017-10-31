package taro.rikkeisoft.com.assignment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.SharedPreferencesCompat;
import android.widget.ArrayAdapter;

import taro.rikkeisoft.com.assignment.model.Note;

/**
 * Created by VjrutNAT on 10/28/2017.
 */

public class NoteViewPager extends FragmentPagerAdapter{

    private final ArrayAdapter<Note> mNotes;

    public NoteViewPager(FragmentManager fm, ArrayAdapter<Note> mNotes) {
        super(fm);
        this.mNotes = mNotes;
    }

    @Override
    public Fragment getItem(int position) {
        return ;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
